package keycloak.ino_lab;

import org.keycloak.models.KeycloakSession;
import javax.ws.rs.GET;
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
    @Path("TEST")
    @Produces(MediaType.APPLICATION_JSON)

    public Response test(){
        
        
        return Response.ok(session.getAttribute("email") +"TEST TATATEST").build();
    }
}