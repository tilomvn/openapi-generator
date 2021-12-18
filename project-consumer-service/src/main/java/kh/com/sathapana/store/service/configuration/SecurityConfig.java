package kh.com.sathapana.store.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.sathapana.store.service.utils.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JWTTokenService jwtTokenService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilterAfter(new ReqAuthFilter(jwtTokenService, objectMapper), SessionManagementFilter.class);
    }
}
