package com.adamd.security;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * OAuth controller.
 * <p/>
 * Responsibilities:
 * <ul>
 * <li>Generation of the OAuth token.</li>
 * </ul>
 * 
 * @author davide, adamd
 * @version 1
 */
@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authmanager;

    @Autowired
    private UserDetailsService userDetailsService;

    // ========================================================================

    /**
     * @param username
     * @param password
     * @param response
     * @return
     */
    @RequestMapping(path = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OAuth2AccessToken authenticate(final String username, final String password,
            final HttpServletResponse response) {
        final Authentication authentication = authmanager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = UUID.randomUUID().toString();
        final String refreshToken = UUID.randomUUID().toString();

        final DefaultOAuth2AccessToken oauthToken = new DefaultOAuth2AccessToken(token);
        final DefaultOAuth2RefreshToken oauthRefreshToken = new DefaultOAuth2RefreshToken(refreshToken);

        long next10days = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10);
        oauthToken.setExpiration(new Date(next10days));
        oauthToken.setRefreshToken(oauthRefreshToken);
        oauthToken.setScope(new HashSet<String>(Arrays.asList("ADMIN")));

        final UserDetails user = userDetailsService.loadUserByUsername(username);

        return oauthToken;
    }

    @RequestMapping("/user")
    @ResponseBody
    public Principal getUser(final Principal user) {
        return user;
    }

    @RequestMapping(path = "/welcome")
    @ResponseBody
    public String getWelcomePage() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/signup")
    public void signUp(final UserDetailsImp user) {
        ((InMemoryUserDetailsManager) userDetailsService).createUser(user);
    }

}
