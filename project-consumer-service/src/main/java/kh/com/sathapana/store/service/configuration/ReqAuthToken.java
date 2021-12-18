package kh.com.sathapana.store.service.configuration;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class ReqAuthToken extends AbstractAuthenticationToken {
    private String principal;
    public ReqAuthToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
    public ReqAuthToken(List<GrantedAuthority> auths, String princ, boolean authenticated) {
        this(auths);
        this.principal = princ;
        super.setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
