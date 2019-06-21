package ino;

import org.keycloak.Config.Scope;
import org.keycloak.authorization.AuthorizationProvider;
import org.keycloak.authorization.model.Policy;
import org.keycloak.authorization.policy.provider.PolicyProvider;
import org.keycloak.authorization.policy.provider.PolicyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.Provider;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;

public class TestPolicyProviderFactory implements PolicyProviderFactory {

    @Override
    public Provider create(KeycloakSession session) {
        return null;
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
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public PolicyProvider create(AuthorizationProvider authorization) {
        return null;
    }

    @Override
    public AbstractPolicyRepresentation toRepresentation(Policy policy, AuthorizationProvider authorization) {
        return null;
    }

    @Override
    public Class getRepresentationType() {
        return null;
    }

}
