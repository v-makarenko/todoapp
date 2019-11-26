package com.example.todoapp.config.security;

import com.example.todoapp.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

/**
 * Custom filter to replace UsernamePasswordAuthenticationFilter
 * to be able to retrieve username and password from JSON
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private String jsonUsername;
    private String jsonPassword;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = null;

        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (!StringUtils.isEmpty(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
            password = this.jsonPassword;
        } else {
            password = super.obtainPassword(request);
        }

        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = null;

        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (!StringUtils.isEmpty(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
            username = this.jsonUsername;
        } else {
            username = super.obtainUsername(request);
        }

        return username;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);       // We use this
        if (!StringUtils.isEmpty(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
            try {
                /*
                 * HttpServletRequest can be read only once
                 */
                StringBuffer sb = new StringBuffer();
                String line = null;

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                //json transformation
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = mapper.readValue(sb.toString(), UserDto.class);

                this.jsonUsername = userDto.getUsername();
                this.jsonPassword = new String(userDto.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(jsonUsername,
            jsonPassword);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}