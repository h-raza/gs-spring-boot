package com.example.springboot;


import com.google.common.collect.ImmutableList;
import io.grpc.netty.shaded.io.netty.handler.codec.http.cors.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.Collections;


@EnableWebSecurity
        @Configuration
        class WebSecurityConfig extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity http) throws Exception {



                http.authorizeRequests().antMatchers("/h2/**").permitAll()
                        .and().csrf().ignoringAntMatchers("/h2/**")
                        .and().headers().frameOptions().sameOrigin();

//                http.cors().and().authorizeRequests().antMatchers("/**").permitAll()
//                        .and().csrf().ignoringAntMatchers("/**")
//                        .and().headers().frameOptions().sameOrigin();


                http.csrf().disable()
                       .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(HttpMethod.POST,"/firebaseCreateOrder/").permitAll()
                        .antMatchers(HttpMethod.GET,"/getOrderFirebase/").permitAll()
                        .antMatchers(HttpMethod.POST, "/createUser").permitAll()
                        .antMatchers(HttpMethod.POST, "/user").permitAll()
                        .antMatchers(HttpMethod.GET,"/getBuyOrder/").permitAll()
//                      .antMatchers(HttpMethod.POST,"/api/signIn" ).permitAll()
//                      .antMatchers(HttpMethod.GET,"/api/signIn" ).permitAll()
//                        .antMatchers(HttpMethod.POST,"/api/signUp" ).permitAll()
//                        .antMatchers(HttpMethod.GET,"/api/signUp" ).permitAll()
                        .antMatchers(HttpMethod.GET,"/stomp/**" ).permitAll()
                        .antMatchers(HttpMethod.POST,"/stomp/**" ).permitAll()
                        .anyRequest().authenticated();

            }




        }



