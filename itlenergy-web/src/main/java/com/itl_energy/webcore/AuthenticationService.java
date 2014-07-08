package com.itl_energy.webcore;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import com.itl_energy.model.User;
import com.itl_energy.repo.UserCollection;

/**
 * Provides a means of authenticating users.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class AuthenticationService {
    public static final String PARAM_NAME = "sgauth";
    
    @EJB
    UserCollection users;
    
    
    /**
     * Gets the ticket for the current context.
     * @return The ticket if available, or null.
     */
    public SecurityTicket getTicket(UriInfo uri) {
        MultivaluedMap<String,String> query = uri.getQueryParameters();
        String value = query.getFirst(PARAM_NAME);
        
        if (value == null)
            return null;
        else
            return SecurityTicket.parse(value, AuthenticationServiceConfig.key, AuthenticationServiceConfig.secret);
    }
    
    
    /**
     * Attempts to authenticate the given credentials and returns a ticket if 
     * successful.
     * @param username
     * @param password
     * @return The ticket or null.
     */
    public TicketModel createTicket(String username, String password) {
        User user = users.get(username, password);
        
        if (user == null)
            return null;
        
        Date expires = new Date();
        expires.setTime(expires.getTime() + SecurityTicket.EXPIRES_MS);
        Integer related = user.getRelatedId();
        
        SecurityTicket ticket = new SecurityTicket(expires, username, user.getRole(), related != null ? related : 0, false);
        String encrypted = ticket.encrypt(AuthenticationServiceConfig.key, AuthenticationServiceConfig.secret);
        return new TicketModel(encrypted, ticket.getExpires());
    }
    
    
    /**
     * Renews the current ticket, if any.
     * @return The new ticket, or null.
     */
    public TicketModel renewTicket(UriInfo uri) {
        SecurityTicket ticket = getTicket(uri);
        
        if (ticket == null)
            return null;
        
        Date expires = new Date();
        expires.setTime(expires.getTime() + SecurityTicket.EXPIRES_MS);
        ticket.setExpires(expires);
        ticket.setReissue(true);
        return new TicketModel(ticket.encrypt(AuthenticationServiceConfig.key, AuthenticationServiceConfig.secret), ticket.getExpires());
    }
    
    
    /**
     * Throws an exception if the current user does not have the required role.
     * @param role 
     */
    public void requireRole(UriInfo uri, String role) {
        SecurityTicket ticket = getTicket(uri);
        
        if (ticket == null || !ticket.getRole().equals(role))
            throw new NotAuthorizedException();
    }
    
    
    /**
     * Throws an exception if the current user is not related to the specified
     * hub.
     * @param role 
     */
    public void requireHub(UriInfo uri, int hubId) {
        SecurityTicket ticket = getTicket(uri);
        
        if (ticket == null || !ticket.getRole().equals(AuthRoles.ADMIN)
            && !(ticket.getRole().equals(AuthRoles.HUB)  && ticket.getRelatedId() != hubId))
            throw new NotAuthorizedException();
    }
    
    /**
     * Throws an exception if the current user is not related to the specified
     * site.
     * @param role 
     */
    public void requireSite(UriInfo uri, int siteId) {
        SecurityTicket ticket = getTicket(uri);
        
        if (ticket == null || !ticket.getRole().equals(AuthRoles.WEATHER_REPORTER)
            && !(ticket.getRole().equals(AuthRoles.WEATHER_REPORTER)  && ticket.getRelatedId() != siteId))
            throw new NotAuthorizedException();
    }
    
    /**
     * Throws an exception if the current ticket has been reissued or
     * is older than the specified age.
     * @param uri
     * @param maxSeconds 
     */
    public void requireRecentAdmin(UriInfo uri, int maxSeconds) {
        SecurityTicket ticket = getTicket(uri);
        
        if (ticket == null || !ticket.getRole().equals(AuthRoles.ADMIN) || ticket.getReissue())
            throw new NotAuthorizedException();
        
        long issueDate = ticket.getExpires().getTime() - SecurityTicket.EXPIRES_MS;
        long now = System.currentTimeMillis();
        
        if (issueDate + maxSeconds * 1000 < now) 
            throw new NotAuthorizedException();
    }
}
