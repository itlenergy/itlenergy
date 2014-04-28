package com.itl_energy.webcore;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Deals with NotAuthorizedException nicely.
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Provider
public class NotAuthorizedHandler implements ExceptionMapper<javax.ejb.EJBTransactionRolledbackException> {
    
    @Override
    public Response toResponse(javax.ejb.EJBTransactionRolledbackException exn) {
        Throwable cause = getRootCause(exn);
        
        if (cause.getClass() == NotAuthorizedException.class)
            return Response.status(403).build();
        else
            throw exn;
    }
    
    
    private Throwable getRootCause(Throwable t) {
        Throwable ret = null;
        Throwable cause = t;
        
        while (cause != null) {
            ret = cause;
            cause = cause.getCause();
        }
        
        return ret;
    }
}