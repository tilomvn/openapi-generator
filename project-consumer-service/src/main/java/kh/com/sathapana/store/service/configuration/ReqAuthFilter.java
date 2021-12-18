package kh.com.sathapana.store.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.sathapana.store.service.utils.JWTTokenService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ReqAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JWTTokenService jwtTokenService;
    private final ObjectMapper mapper;

    public ReqAuthFilter(JWTTokenService jwtTokenService, ObjectMapper mapper) {
        this.jwtTokenService = jwtTokenService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var jwtToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(jwtToken)) {
            rejectRequest(request, response);
            return;
        }
        var claimMap = jwtTokenService.verifyAndGetPayload(jwtToken);
        if (MapUtils.isEmpty(claimMap)) {
            rejectRequest(request, response);
            return;
        }
        var role = claimMap.get("role");
        var userId = claimMap.get("userId");
        if (role == null || userId == null) {
            rejectRequest(request, response);
            return;
        }
        var roleName = role.asString();
        var auths = new ArrayList<GrantedAuthority>();
        if (!roleName.contains("ROLE_"))
            roleName = "ROLE_" + roleName;
        auths.add(new SimpleGrantedAuthority(roleName));
        var authToken = new ReqAuthToken(auths, userId.asString(), true);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    void rejectRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(request.getContentType());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
