package keycloak.ino_lab;

public class Templates {
    public static String getTemplate(MECAsAccountPages page) {
        switch (page) {
            case ACCOUNT:
                return "account.ftl";
            case PASSWORD:
                return "password.ftl";
            case TOTP:
                return "totp.ftl";
            case FEDERATED_IDENTITY:
                return "federatedIdentity.ftl";
            case LOG:
                return "log.ftl";
            case SESSIONS:
                return "sessions.ftl";
            case APPLICATIONS:
                return "applications.ftl";
            case RESOURCES:
                return "resources.ftl";
            case RESOURCE_DETAIL:
                return "resource-detail.ftl";
            default:
                throw new IllegalArgumentException();
        }
    }
}
