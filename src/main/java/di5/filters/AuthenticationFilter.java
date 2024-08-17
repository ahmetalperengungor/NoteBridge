package di5.filters;

import di5.helpers.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationFilter implements Filter {
    private List<String> passPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        passPaths = new ArrayList<>();
        passPaths.add("/notebridge/api/users/login");
        passPaths.add("/notebridge/api/users/signup");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // PASS THE AUTH FILTER
        if (passPaths.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        String token = authHeader.substring(7);
        try {
            if (JwtUtil.isTokenExpired(token)) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            }

            String username = JwtUtil.getUsernameFromToken(token);
            // Optionally set the username as a request attribute or security context
            httpRequest.setAttribute("username", username);


        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
