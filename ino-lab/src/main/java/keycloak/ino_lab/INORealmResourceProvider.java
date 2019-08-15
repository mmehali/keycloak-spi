package keycloak.ino_lab;


import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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

    private Response createRes(Object obj, int status) {
        return Response .status(status).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "include")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                .entity(obj).build();
                
    }

    @Path("groups")
    @OPTIONS
    @NoCache
    public Response preflight(final @Context HttpRequest request) {
        Cors cors = Cors.add(request, Response.ok()).auth().allowedMethods("GET", "POST", "DELETE", "HEAD", "OPTIONS").preflight();
        return cors.build();
    }

    @Path("invitations")
    @OPTIONS
    @NoCache
    public Response preflightToken(final @Context HttpRequest request) {
        Cors cors = Cors.add(request, Response.ok()).auth().allowedMethods("GET", "POST", "DELETE", "HEAD", "OPTIONS").preflight();
        return cors.build();
    }
    /////////////////////////////////////
    ////    CREATE INVITE TOKEN     ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/mecas/invitations
    @POST
    @Path("invitations")
    public Response getInviteToken(@QueryParam("username") String username,@QueryParam("group") String group) {
        String groupID = group;
        String groupName = session.getContext().getRealm().getGroupById(group).getName();
        int status = 200;
        JsonObjectBuilder builder = Json.createObjectBuilder();
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        //Login require. If not show "Unauthorization"
        if (this.auth == null) {
            builder.add("error", "Unauthorization");
            status = 401;
        } else {
            UserModel user = session.users().getUserByUsername(username, session.getContext().getRealm());
            //InviteToken Format (userid, exp, compoundAuthenticationSessionId, groupid, groupname, email, username)
            InviteToken token = 
                new InviteToken(user.getId(), Time.currentTime()+600, auth.getSession().getId(), 
                                groupID, groupName, user.getEmail(), user.getUsername());

            builder.add("token", token.serialize(session, session.getContext().getRealm(), session.getContext().getUri()));
        }
        return createRes(builder.build(), status);
    }

    /////////////////////////////////////
    ////    RESPONSE GROUP INFO      ////
    ////    NAME ID USERinGroup     ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/mecas/groups
    @GET
    @Path("groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGROUP() throws Exception{
        //Login require. If not show "Unauthorization"
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("error", "Unauthorization");
            return createRes(builder.build(), 401);
        } else {
            UserModel user = auth.getUser();
            JsonArrayBuilder groupsBuilder = Json.createArrayBuilder();

            for (GroupModel group : user.getGroups()) {
                JsonObjectBuilder groupBuilder = Json.createObjectBuilder();
                groupBuilder.add("id", group.getId());
                groupBuilder.add("name", group.getId());
                JsonArrayBuilder membersBuilder = Json.createArrayBuilder();
                for (UserModel member : session.users().getGroupMembers(session.getContext().getRealm(), group)) {
                    membersBuilder.add(member.getUsername());
                }
                groupBuilder.add("members", membersBuilder.build());
                groupsBuilder.add(groupBuilder.build());
            }
            return createRes(groupsBuilder.build(), 200);
        }
    }

    /////////////////////////////////////
    ////         Join Group         ////
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/mecas/mygroup
    @POST
    @Path("groups")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response joinGROUP(String req) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        int status = 200;
        //Login and Request require.
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null) {
            builder.add("error", "Unauthorization");
            status = 401;
        } else if (req == null) {
            builder.add("error", "Invalid request data");
            status = 400;
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
                builder.add("error", "Invalid group");
                status = 400;
            } else {
                user.joinGroup(group);
                builder.add("success", "Join to" + group.getName() +" group successfully");
            }
        }
        return createRes(builder.build(), status);
    }

    /////////////////////////////////////
    ////         Leave Group         ///
    ////////////////////////////////////
    //<Keycloak_HOME>/auth/realms/<Realm_Name>/mecas/mygroup
    @DELETE
    @Path("groups")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response leaveGROUP(String req) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        int status = 200;

        //Login and Request require.
        this.auth = new AppAuthManager().authenticateBearerToken(session);
        if (this.auth == null) {
            builder.add("error", "Unauthorization");
            status = 401;
        } else if (req == null) {
            builder.add("error", "Invalid request data");
            status = 400;
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
                builder.add("error", "Invalid group");
                status = 400;
            } else {
                user.leaveGroup(group);
                user.joinGroup(group);
                builder.add("success", "Leave to" + group.getName() +" group successfully");
            }
        }
        return createRes(builder.build(),status);
    }

}