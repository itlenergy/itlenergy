/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webapi;

import java.security.SecureRandom;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.itl_energy.model.User;
import com.itl_energy.repo.UserCollection;
import com.itl_energy.webcore.AuthRoles;
import com.itl_energy.webcore.ChangePasswordModel;

/**
 * The Web API RESTful interface to the weather observations collection.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Path("users")
@Stateless
public class UserResource extends ResourceBase<User> {
    private static final int MAX_TICKET_AGE = 60;
    
    public UserResource() {
        // bit of a kludge so get requests don't need recent admin.
        super.getRole = "GET_USERS";
    }
    
    @EJB
	UserCollection users;
	
	@PostConstruct
	void init() {
		super.collection = this.users;
	}
    
    
    @POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
	public Response add(User entity) {
        // since password is not serialized, set a random password.
        SecureRandom random = new SecureRandom();
        byte[] password = new byte[UserCollection.HASH_KEY_LENGTH / 8];
        random.nextBytes(password);
        entity.setPassword(password);
        return super.add(entity);
    }
    
    
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Override
	public Response update(User entity) {
        requireRole(updateRole);
		User original = collection.get(collection.getId(entity));
		if (original != null) {
            // don't change the password
            entity.setPassword(original.getPassword());
			collection.update(entity);
			return Response.noContent().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
    
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{id}/password")
    public Response changePassword(@PathParam("id") int id, ChangePasswordModel model) {
        auth.requireRecentAdmin(uriInfo, MAX_TICKET_AGE);
        User original = collection.get(id);
		if (original != null) {
            original.setPassword(users.getPasswordHash(original.getUsername(), model.getPassword()));
			return Response.noContent().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
    }
    
    
    @Override
    protected void requireRole(String role) {
        // for updates, override authentication to require
        // that an admin logged in recently
        if (!role.equals(super.getRole)) {
            auth.requireRecentAdmin(uriInfo, MAX_TICKET_AGE);
        } else {
            auth.requireRole(uriInfo, AuthRoles.ADMIN);
        }
    }
}