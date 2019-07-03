package keycloak.ino_lab;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.forms.account.AccountProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.Provider;
import org.keycloak.theme.FreeMarkerUtil;

public class MECAsAccountProviderFactory implements AccountProviderFactory {
    private FreeMarkerUtil freeMarker;
    private static final Logger logger = Logger.getLogger(MECAsAccountProvider.class);

    @Override
    public Provider create(KeycloakSession session) {
        logger.error("e");
        try {
            return new MECAsAccountProvider(session, freeMarker);
        } catch (java.lang.NullPointerException e) {
            freeMarker = new FreeMarkerUtil();
            return new MECAsAccountProvider(session, freeMarker);
        }

    }

    @Override
    public void init(Config.Scope scope) {
        freeMarker = new FreeMarkerUtil();
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {
        freeMarker = null;
    }

    @Override
    public String getId() {
        return "mecas";
    }
}
