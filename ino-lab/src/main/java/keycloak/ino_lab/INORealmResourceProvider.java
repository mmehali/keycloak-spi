package keycloak.ino_lab;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class INORealmResourceProvider implements RealmResourceProvider{

    private final KeycloakSession session;

    public INORealmResourceProvider (KeycloakSession session){
        this.session = session;
    }

    @Override
    public void close() {
    
    }

    @Override
    public Object getResource() {
        //return new INORealmResource(session);
        return this;
    }

    @GET
    @Path("mygroup")
    @Produces("text/plain; charset=utf-8")
    public String get() {
        String name = session.getContext().getRealm().getDisplayName();
        if (name == null) {
            name = session.getContext().getRealm().getName();
        }
        return "Hello " + name +" TEST"+"WORLD";
    }
    
}