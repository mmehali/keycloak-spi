package keycloak.ino_lab;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.actiontoken.ActionTokenHandler;
import org.keycloak.authentication.actiontoken.ActionTokenHandlerFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class InviteTokenHandlerFactory implements ActionTokenHandlerFactory<InviteToken> {

    //Handler token that have TOKEN_TYPE same with this ID
    public static final String ID = "invitation";

    @Override
    public ActionTokenHandler<InviteToken> create(KeycloakSession session) {
        return new InviteTokenHandler(session);
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