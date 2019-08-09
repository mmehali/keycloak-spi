package keycloak.ino_lab;


import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.resources.Cors;

import org.keycloak.common.util.Time;

import org.keycloak.services.resource.RealmResourceProvider;
import org.jboss.resteasy.spi.HttpRequest;

public class INORealmResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;
    private AuthResult auth;

    public INORealmResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {

    }

    @Override
    public Object getResource() {
        return this;
    }

    private Response createRes(Object obj) {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "include")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                .entity(obj).build();
                
    }

    @Path("mygroup")
    @OPTIONS
    @NoCache
    public Response preflight(final @Context HttpRequest request) {
        Cors cors = Cors.add(request, Response.ok()).auth().allowedMethods("GET", "POST", "DELETE", "HEAD", "OPTIONS").preflight();
        return cors.build();
    }

    @Path("invitetoken")
    @OPTIONS
    @NoCache
    public Response preflightToken(final @Context HttpRequest request) {
        Cors cors = Cors.add(request, Response.ok()).auth().allowedMethods("GET", "POST", "DELETE", "HEAD", "OPTIONS").preflight();
        return cors.build();
    }
    /////////////////////////////////////
    ////    CREATE INVITE TOKEN     ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/ino/invitetoken
    //Change "/ino" to another in INORealmResourceProviderFactory.java
    @POST
    @Path("invitetoken")
    public Response getInviteToken(@QueryParam("username") String username,@QueryParam("group") String group) {
        Object obj = "";
        String groupID = group;
        String groupName = session.getContext().getRealm().getGroupById(group).getName();

        this.auth = new AppAuthManager().authenticateBearerToken(session);
        //Login require. If not show "Unauthorization"
        if (this.auth == null) {
            obj = "Unauthorization";
        } else {
            UserModel user = session.users().getUserByUsername(username, session.getContext().getRealm());
            //InviteToken Format (userid, exp, compoundAuthenticationSessionId, groupid, groupname, email, username)
            InviteToken token = 
                new InviteToken(user.getId(), Time.currentTime()+600, auth.getSession().getId(), 
                                groupID, groupName, user.getEmail(), user.getUsername());

            //Generate token to JWT
            obj = token.serialize(session, session.getContext().getRealm(), session.getContext().getUri());
            if(obj ==null){
                obj = "Error cannot create token";
            }         
        } 
        return createRes(obj);
    }

    /////////////////////////////////////
    ////    RESPONSE GROUP INFO      ////
    ////    NAME ID USERinGroup     ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/ino/mygroup
    //Change "/ino" to another in INORealmResourceProviderFactory.java
    @GET
    @Path("mygroup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGROUP() throws Exception{
        Object obj = null;
        
        //Login require. If not show "Unauthorization"
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null) {
            obj = "Unauthorization";
        } else {
            UserModel user = auth.getUser();
            //ArrayList<GroupModel> mygroup = new ArrayList<GroupModel>();
            List<UserModel> members = null;
            
            //Create JSON contains name, id, members
            String json = "[";
            for (int i = 0; i < user.getGroups().size(); i++) {
                members = session.users().getGroupMembers(session.getContext().getRealm(), user.getGroups(i, user.getGroups().size()).iterator().next());
                json += "{";
                json += "\"name\":\""+user.getGroups(i, user.getGroups().size()).iterator().next().getName()+"\",";
                json += "\"id\":\""+user.getGroups(i, user.getGroups().size()).iterator().next().getId()+"\",";
                json += "\"members\":["; 
                for(int j = 0 ; j < members.size(); j++){
                    json += "\""+members.get(j).getUsername()+"\"";

                    if(j == members.size()-1)
                        json += "]";
                    else
                        json += ",";
                }
                if(i == user.getGroups().size()-1)
                    json += "}]";
                else
                    json += "},";
            }
            obj = json;
        }
        return createRes(obj);
    }

    /////////////////////////////////////
    ////         Join Group         ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/ino/mygroup
    //Change "/ino" to another in INORealmResourceProviderFactory.java
    @POST
    @Path("mygroup")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response joinGROUP(String req) {
        Object obj = null;
        //Login and Request require.
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null || req == null) {
            obj = "Unauthorization Or no request data. ";
        } else {
            UserModel user = auth.getUser();
            GroupModel group = null;
            for (int i = 0;; i++) {
                if (req.equals(session.getContext().getRealm().getGroups().get(i).getName())) {
                    group = session.getContext().getRealm().getGroups().get(i);
                    break;
                }
            }

            if (group == null) {
                obj = req + "GROUP NULL";
            } else {
                user.joinGroup(group);
                obj = "Join to" + group.getName() +" group successfully";
            }
        }
        return createRes(obj);
    }

    /////////////////////////////////////
    ////         Leave Group         ///
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/ino/mygroup
    //Change "/ino" to another in INORealmResourceProviderFactory.java
    @DELETE
    @Path("mygroup")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response leaveGROUP(String req) {
        Object obj = null;

        //Login and Request require.
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null || req == null) {
            obj = "Unauthorization Or no request data. ";
        } else {
            UserModel user = auth.getUser();
            GroupModel group = null;

            for (int i = 0;; i++) {
                if (req.equals(session.getContext().getRealm().getGroups().get(i).getName())) {
                    group = session.getContext().getRealm().getGroups().get(i);
                    break;
                }
            }

            if (group == null) {
                obj = req + "GROUP NULL";
            } else {
                user.leaveGroup(group);
                obj = "Leave to " + group.getName() +" group successfully";
            }
        }
        return createRes(obj);
    }

}