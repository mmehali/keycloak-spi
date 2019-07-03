package keycloak.ino_lab;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.keycloak.forms.account.AccountProvider;
import org.keycloak.forms.account.freemarker.FreeMarkerAccountProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.theme.FreeMarkerUtil;

public class INORealmResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;
    private final AuthResult auth;
    public INORealmResourceProvider(KeycloakSession session) {
        this.session = session;
        this.auth = new AppAuthManager().authenticateBearerToken(session, session.getContext().getRealm());
    }

    @Override
    public void close() {

    }

    @Override
    public Object getResource() {
        // return new INORealmResource(session);
        return this;
    }

    @GET
    @Path("mygroup")
    @Produces("text/plain; charset=utf-8")
    public String get() { 
        //String groupName = session.getContext().getRealm().getGroups().get(0).getName();
        //UserModel user = session.users().getUserByUsername("tar", realm);
//        UserModel user = this.auth.getUser();
//        AccountProvider account = this.session.getProvider(AccountProvider.class);
            this.get
        return "Hello, your group is " +  this.auth;
    }

    private void checkRealmAdmin() {
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getToken().getRealmAccess() == null || !auth.getToken().getRealmAccess().isUserInRole("admin")) {
            throw new ForbiddenException("Does not have realm admin role");
        }
    }

    @Path("test")
    public INORealmResource getCompanyResource() {
        return new INORealmResource(session);
    }

    // Same like "companies" endpoint, but REST endpoint is authenticated with Bearer token and user must be in realm role "admin"
    // Just for illustration purposes
    @Path("test-auth")
    public INORealmResource getCompanyResourceAuthenticated() {
        checkRealmAdmin();
        return new INORealmResource(session);
    }

}