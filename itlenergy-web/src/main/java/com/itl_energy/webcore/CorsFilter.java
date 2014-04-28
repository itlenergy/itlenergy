package com.itl_energy.webcore;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;


/**
 * Enables Cross-Origin Resource Sharing by adding 
 * "Access-Control-Allow-Origin: *".
 * 
 * Copied from http://padcom13.blogspot.de/2011/09/cors-filter-for-java-applications.html
 * 
 * @author Gordon Mackenzie-Leigh
 */
@WebFilter("/api/*")
public class CorsFilter implements Filter {

	public CorsFilter() { }

    @Override
	public void init(FilterConfig fConfig) throws ServletException { }

    @Override
	public void destroy() {	}

    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse sr = (HttpServletResponse)response;
		sr.addHeader("Access-Control-Allow-Origin", "*");
        sr.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, X-Requested-With");
        sr.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
		chain.doFilter(request, response);
	}
}