package keycloak.ino_lab;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class INORealmResource{
    private final KeycloakSession session ;

    public INORealmResource(KeycloakSession session){
        this.session = session;
    }
    
    @GET
    @Path("")
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public String getInviteLink() {
        return "This is your invite link";
    }

    @POST
    @Path("")
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    public String createInviteLink() {
        return "Invite link created";
    }
}