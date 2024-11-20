package com.pitang.desafio.tcepe.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class JsonRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String uri = request.getRequestURI();
        final String contentType = request.getContentType();

        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")) {
            return true;
        }

        if (Objects.isNull(contentType) || !contentType.equals("application/json")) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            response.getWriter()
                    .write("{ \"error\": \"Only application/json is accepted.\"," +
                            "    \"erroCode\" \"4879\"" +
                              "}");
            return false;
        }

        return true;
    }
}

