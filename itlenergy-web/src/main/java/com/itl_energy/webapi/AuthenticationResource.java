package com.itl_energy.webapi;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import com.itl_energy.model.User;
import com.itl_energy.repo.UserCollection;
import com.itl_energy.webcore.AuthenticationService;
import com.itl_energy.webcore.LoginModel;
import com.itl_energy.webcore.TicketModel;

/**
 * The Web API RESTful interface to provide login / logout functions.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("auth")
@Stateless
public class AuthenticationResource {
    @Context UriInfo uriInfo;
    @EJB AuthenticationService auth;
    @EJB UserCollection users;
    
    
    @POST
    @Path("login")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response login(LoginModel dto) {
        TicketModel ticket = auth.createTicket(dto.getUsername(), dto.getPassword());
        
        if (ticket == null)
            return Response.status(Status.FORBIDDEN).build();
        else
            return Response.ok(ticket).build();
    }
    
    
    @GET
    @Path("renew")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response renew() {
        TicketModel ticket = auth.renewTicket(uriInfo);
        
        if (ticket == null)
            return Response.status(Status.FORBIDDEN).build();
        else
            return Response.ok(ticket).build();
    }
}
