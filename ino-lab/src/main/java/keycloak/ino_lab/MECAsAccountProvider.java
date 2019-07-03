package keycloak.ino_lab;


import org.keycloak.forms.account.freemarker.FreeMarkerAccountProvider;
import org.keycloak.forms.account.freemarker.model.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.theme.BrowserSecurityHeaderSetup;
import org.keycloak.theme.FreeMarkerException;
import org.keycloak.theme.FreeMarkerUtil;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.*;

import org.keycloak.theme.Theme;
import java.io.IOException;

import org.jboss.logging.Logger;
import org.keycloak.theme.beans.AdvancedMessageFormatterMethod;
import org.keycloak.theme.beans.LocaleBean;
import org.keycloak.utils.MediaType;


public class MECAsAccountProvider extends FreeMarkerAccountProvider {
    private static final Logger logger = Logger.getLogger(MECAsAccountProvider.class);
    private boolean authorizationSupported;

    public MECAsAccountProvider(KeycloakSession session, FreeMarkerUtil freeMarker) {
        super(session, freeMarker);
        this.session = session;
        this.freeMarker = freeMarker;
    }

    public Response createResponse(MECAsAccountPages page) {
        Map<String, Object> attributes = new HashMap<>();

        if (this.attributes != null) {
            attributes.putAll(this.attributes);
        }

        Theme theme;
        try {
            theme = getTheme();
        } catch (IOException e) {
            logger.error("Failed to create theme", e);
            return Response.serverError().build();
        }

        Locale locale = session.getContext().resolveLocale(user);
        Properties messagesBundle = handleThemeResources(theme, locale, attributes);

        URI baseUri = uriInfo.getBaseUri();
        UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder();
        for (Map.Entry<String, List<String>> e : uriInfo.getQueryParameters().entrySet()) {
            baseUriBuilder.queryParam(e.getKey(), e.getValue().toArray());
        }
        URI baseQueryUri = baseUriBuilder.build();

        if (stateChecker != null) {
            attributes.put("stateChecker", stateChecker);
        }

        handleMessages(locale, messagesBundle, attributes);

        if (referrer != null) {
            attributes.put("referrer", new ReferrerBean(referrer));
        }

        if(realm != null){
            attributes.put("realm", new RealmBean(realm));
        }

        attributes.put("url", new UrlBean(realm, theme, baseUri, baseQueryUri, uriInfo.getRequestUri(), stateChecker));

        if (realm.isInternationalizationEnabled()) {
            UriBuilder b = UriBuilder.fromUri(baseQueryUri).path(uriInfo.getPath());
            attributes.put("locale", new LocaleBean(realm, locale, b, messagesBundle));
        }

        attributes.put("features", new FeaturesBean(identityProviderEnabled, eventsEnabled, passwordUpdateSupported, authorizationSupported));
        attributes.put("account", new AccountBean(user, profileFormData));

        switch (page) {
            case TOTP:
                attributes.put("totp", new TotpBean(session, realm, user, uriInfo.getRequestUriBuilder()));
                break;
            case FEDERATED_IDENTITY:
                attributes.put("federatedIdentity", new AccountFederatedIdentityBean(session, realm, user, uriInfo.getBaseUri(), stateChecker));
                break;
            case LOG:
                attributes.put("log", new LogBean(events));
                break;
            case SESSIONS:
                attributes.put("sessions", new SessionsBean(realm, sessions));
                break;
            case APPLICATIONS:
                attributes.put("applications", new ApplicationsBean(session, realm, user));
                attributes.put("advancedMsg", new AdvancedMessageFormatterMethod(locale, messagesBundle));
                break;
            case PASSWORD:
                attributes.put("password", new PasswordBean(passwordSet));
                break;
            case RESOURCES:
                if (!realm.isUserManagedAccessAllowed()) {
                    return Response.status(Status.FORBIDDEN).build();
                }
                attributes.put("authorization", new AuthorizationBean(session, user, uriInfo));
            case RESOURCE_DETAIL:
                if (!realm.isUserManagedAccessAllowed()) {
                    return Response.status(Status.FORBIDDEN).build();
                }
                attributes.put("authorization", new AuthorizationBean(session, user, uriInfo));
        }

        return processTemplate(theme, page, attributes, locale);
    }

    protected Response processTemplate(Theme theme, MECAsAccountPages page, Map<String, Object> attributes, Locale locale) {
        try {
            String result = freeMarker.processTemplate(attributes, Templates.getTemplate(page), theme);
            Response.ResponseBuilder builder = Response.status(status).type(MediaType.TEXT_HTML_UTF_8_TYPE).language(locale).entity(result);
            BrowserSecurityHeaderSetup.headers(builder, realm);
            return builder.build();
        } catch (FreeMarkerException e) {
            logger.error("Failed to process template", e);
            return Response.serverError().build();
        }
    }
}
