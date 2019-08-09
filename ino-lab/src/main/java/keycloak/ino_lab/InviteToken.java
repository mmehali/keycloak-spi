package keycloak.ino_lab;

import org.keycloak.authentication.actiontoken.DefaultActionToken;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteToken extends DefaultActionToken {

    public static final String TOKEN_TYPE = "invite-token";

    private static final String JSON_FIELD_GROUP_ID = "groupId";
    private static final String JSON_FIELD_GROUP_NAME = "groupName";
    private static final String JSON_FIELD_EMAIL = "email";
    private static final String JSON_FIELD_USERNAME = "username";

    @JsonProperty(value = JSON_FIELD_GROUP_ID)
    private String groupID;

    @JsonProperty(value = JSON_FIELD_GROUP_NAME)
    private String groupName;

    @JsonProperty(value = JSON_FIELD_USERNAME)
    private String username;

    @JsonProperty(value = JSON_FIELD_EMAIL)
    private String email;

    public InviteToken(String userId, int absoluteExpirationInSecs, String compoundAuthenticationSessionId,
            String groupId, String groupName, String email, String username) {
        super(userId, TOKEN_TYPE, absoluteExpirationInSecs, null, compoundAuthenticationSessionId);
        this.groupID = groupId;
        this.groupName = groupName;
        this.email = email;
        this.username = username;
    }

    private InviteToken() {
        // you must have this private constructor for deserializer
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}