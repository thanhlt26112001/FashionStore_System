package com.example.fashionstore_system.security;

import com.example.fashionstore_system.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.sql.DataSource;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.CACHE;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailServiceImpl userService;
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
//         Get AuthenticationManager Bean
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    private static final ClearSiteDataHeaderWriter.Directive[] SOURCE =
            {CACHE, COOKIES};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/changePassword").authenticated()
                .antMatchers("/myorder").hasAnyRole("CUSTOMER")
                .antMatchers("/myorder/*").hasAnyRole("CUSTOMER")
                .antMatchers("/cart/checkout").hasAnyRole("CUSTOMER")
                .antMatchers("/admin/product").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/listPromotions").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/order").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/category/listCategory").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/customer").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/staff").hasAnyRole("STAFF","MANAGER","ADMIN")
                .antMatchers("/admin/admin/category/listCategory/add").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/admin/listPromotions/add").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/admin/product/new").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/customer/new").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/newStaff").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/product/saveImage/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/product/save").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/customer/save").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/customer/saveEdit").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/saveStaff").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/saveStaffEdit").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/listPromotions/saveOrUpdate").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/listPromotions/editAndSave").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/category/listCategory/save").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/category/listCategory/editAndSave").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/saveOrderEdit").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/category/delete/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/product/delete/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/customer/delete/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/deleteStaff/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/delete/*").hasAnyRole("MANAGER","ADMIN")
                .antMatchers("/admin/product/deleteImage/*").hasAnyRole("MANAGER","ADMIN")
                .anyRequest().permitAll()
                .and()
                    .formLogin()
                    .permitAll()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/doLogin")
                    .failureUrl("/loginFail")
                .and()
                    .logout()
                    .permitAll()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/404");
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/check_username")
                .antMatchers("/check_email")
                .antMatchers("/cart/reduceAmount")
                .antMatchers("/css/*", "/js/*", "/images/*")
                .antMatchers("/templates/login.html");
    }

}
