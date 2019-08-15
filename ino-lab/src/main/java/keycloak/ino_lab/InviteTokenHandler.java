package keycloak.ino_lab;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.keycloak.authentication.actiontoken.ActionTokenContext;
import org.keycloak.authentication.actiontoken.ActionTokenHandler;
import org.keycloak.common.VerificationException;
import org.keycloak.events.EventType;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.messages.Messages;
import org.keycloak.sessions.AuthenticationSessionModel;

public class InviteTokenHandler implements ActionTokenHandler<InviteToken> {

    //We didn't use now, define session for other feature
    private final KeycloakSession session;

    public InviteTokenHandler(KeycloakSession session) {
        this.session = session;
    }

    
    @Override
    public Response handleToken(InviteToken token, ActionTokenContext<InviteToken> tokenContext) {
        UserModel user = tokenContext.getAuthenticationSession().getAuthenticatedUser();
        final KeycloakSession session = tokenContext.getSession();
        AuthenticationSessionModel authSession = tokenContext.getAuthenticationSession();

        if (user == null) {
            return session.getProvider(LoginFormsProvider.class)
                    .setAuthenticationSession(authSession)
                    .setError("Invalid request")
                    .createErrorPage(Response.Status.BAD_REQUEST);
        } else {
            GroupModel group = tokenContext.getRealm().getGroupById(token.getGroupID());
            user.joinGroup(group);
            return session.getProvider(LoginFormsProvider.class)
                    .setAuthenticationSession(authSession)
                    .setSuccess("Join to" + group.getName() +" group successfully")
                    .createInfoPage();
        }
    }

    @Override
    public boolean canUseTokenRepeatedly(InviteToken token, ActionTokenContext tokenContext) {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public Class<InviteToken> getTokenClass() {
        return InviteToken.class;
    }

    @Override
    public String getAuthenticationSessionIdFromToken(InviteToken token, ActionTokenContext<InviteToken> tokenContext,
            AuthenticationSessionModel currentAuthSession) {
        return null;
    }

    @Override
    public EventType eventType() {
        return null;
    }

    @Override
    public String getDefaultEventError() {
        return "TOKEN EVENT ERROR";
    }

    /////////////////////////////////////
    ////    Show msg when error      ////
    ////////////////////////////////////
    @Override
    public String getDefaultErrorMessage() {
        return "Something went wrong! Cannot use this token";
    }

    @Override
    public AuthenticationSessionModel startFreshAuthenticationSession(InviteToken token,
            ActionTokenContext<InviteToken> tokenContext) throws VerificationException {

        AuthenticationSessionModel authSession = tokenContext
                .createAuthenticationSessionForClient(token.getIssuedFor());
        authSession.setAuthNote(AuthenticationManager.END_AFTER_REQUIRED_ACTIONS, "true");

        return authSession;
    }

}