package com.csu.bakery.config;

import com.csu.bakery.model.Account;
import com.csu.bakery.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    if ("/oauth2/authorization/github".equals(request.getServletPath())) {
                        config.setAllowedOrigins(List.of("http://localhost:5173"));
                        config.setAllowedMethods(List.of("GET"));
                    }
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/github")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            Map<String, Object> attributes = oauth2User.getAttributes();
                            int github_id = (int) attributes.get("id");
                            String github_login = (String) attributes.get("login");
                            String email = (String) attributes.get("email");

                            Account account=accountService.searchGithubAccount(github_id);
                            //处理用户数据
                            if(account==null) {
                                account= new Account(github_login,email);
                                accountService.insertGithubAccount(account);
                                accountService.insertGithub(account.getUserid(),github_id,github_login);
                            }

                            Long userid = account.getUserid();
                            String username = account.getUsername();
                            accountService.setLoginType(userid,"github");

                            String jwt = jwtUtil.generateToken(userid,username,account.getTokenVersion());
                            response.sendRedirect("/auth/github?token=" + jwt);
                        })
                );
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }
}