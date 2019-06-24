package keycloak.ino_lab;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class INORealmResourceProviderFactory implements RealmResourceProviderFactory {

    public static final String ID = "HELLO 1234";
    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new INORealmResourceProvider(session);
    }

    @Override
    public void init(Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return ID;
    }
 
}