package com.example.fashionstore_system.security.oauth;

import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String name = oAuth2User.getAttributes().get("name").toString();
//        String email = oAuth2User.getAttributes().get("email").toString();
//        User user = service.getCustomerByEmail(email);
//        if(user == null) {
//            //register
//            service.createNewUserAfterOAuthLoginSuccess(email, name, AuthenticationProvider.GOOGLE);
//        }else {
//            //update existing
//            service.updateUserAfterOAuthLoginSuccess(user, name, AuthenticationProvider.GOOGLE);
//        }
//
//        System.out.println(oAuth2User.getAttributes().get("email"));
//
//        super.onAuthenticationSuccess(request, response, authentication);
    }
}
