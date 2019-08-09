package keycloak.ino_lab;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.keycloak.authentication.actiontoken.ActionTokenContext;
import org.keycloak.authentication.actiontoken.ActionTokenHandler;
import org.keycloak.common.VerificationException;
import org.keycloak.events.EventType;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.AuthenticationManager;
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
        Object obj = null;
        if (user == null) {
            obj = "TOKEN HANLER USER NULLL";
        } else {
            GroupModel group = tokenContext.getRealm().getGroupById(token.getGroupID());
            user.joinGroup(group);
            obj = "Join Group finished";
        }
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Credentials", "include") 
                            .header("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization")
                            .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                            .type(MediaType.TEXT_PLAIN)
                            .entity(obj).build();
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