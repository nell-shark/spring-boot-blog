package com.nellshark.springbootblog.filter;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.nellshark.springbootblog.wrapper.CharResponseWrapper;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        ServletResponse newResponse = servletResponse;

        if (servletRequest instanceof HttpServletRequest) {
            newResponse = new CharResponseWrapper((HttpServletResponse) servletResponse);
        }

        filterChain.doFilter(servletRequest, newResponse);

        if (newResponse instanceof CharResponseWrapper) {
            String text = newResponse.toString();
            if (text != null) {
                HtmlCompressor htmlCompressor = new HtmlCompressor();
                servletResponse.getWriter().write(htmlCompressor.compress(text));
            }
        }
    }
}
