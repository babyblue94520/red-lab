package pers.clare.common.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pers.clare.common.result.ResultHolder;

import javax.servlet.*;
import java.io.IOException;

@Log4j2
@Component
public class AccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AccessFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            ResultHolder.clear();
        }
    }

    @Override
    public void destroy() {

    }
}
