package com.itl_energy.webapi;

import java.net.URI;
import javax.ejb.EJB;

import javax.ejb.EJBTransactionRolledbackException;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.itl_energy.model.ItemList;
import com.itl_energy.repo.CollectionBase;
import com.itl_energy.webcore.AuthRoles;
import com.itl_energy.webcore.AuthenticationService;


/**
 * Provides common functionality for resources - CRUD methods interfacing with
 * a CollectionBase[TEntity].
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
public abstract class ResourceBase<TEntity> {
	protected CollectionBase<TEntity> collection;
    protected String getRole = AuthRoles.ADMIN;
    protected String addRole = AuthRoles.ADMIN;
    protected String deleteRole = AuthRoles.ADMIN;
    protected String updateRole = AuthRoles.ADMIN;
    
    
	@Context UriInfo uriInfo;
    @EJB protected AuthenticationService auth;
    
    
	/**
	 * Gets all entities in the underlying collection.
	 * @return A typed entity list with all items.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public ItemList<TEntity> getAll() { 
        requireRole(getRole);
		return new ItemList<>(collection.getAll());
	}
	
	
	/**
	 * Gets a single item specified by the given id.
	 * @param id The id of the entity to retrieve.
	 * @return The entity if found, or a 404 response.
	 */
	@GET
	@Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSingle(@PathParam("id")int id) {
        requireRole(getRole);
        TEntity entity = collection.get(id);
		if (entity != null) {
			return Response.ok(entity).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	
	/**
	 * Adds a new entity to the underlying collection.  
	 * @param entity The entity to add
	 * @return HTTP 201 Created with a Location header pointing to the new 
	 * 		entity, or HTTP 409 Conflict if an entity with the same key already
	 * 		exists.
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(TEntity entity) {
		try {
            requireRole(addRole);
			collection.insert(entity);
			// flush changes so Exception can be caught in this block
			//  - otherwise exception won't appear until end of request.
			collection.flush();
            Integer id = collection.getId(entity);
			// return the correct Location header.
			URI location = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();
			return Response.created(location).build();
		} catch (EJBTransactionRolledbackException e) {
			// if a duplicate key error happened, return HTTP 409 conflict
			Exception cause = e.getCausedByException();
			if (cause != null) {
				Throwable rootCause = cause.getCause();
				if (rootCause != null && rootCause.getClass() == PersistenceException.class) {
					return Response.status(Status.CONFLICT).build();
				}
			}
			throw e;
		}
	}
	
	
	/**
	 * Finds an entity with the same key as the supplied entity, then updates
	 * the other fields to match the new entity.
	 * @param entity The new values to set
	 * @return HTTP 204 No Content on success or HTTP 404 if entity is not found.
	 */
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response update(TEntity entity) {
        requireRole(updateRole);
		TEntity original = collection.get(collection.getId(entity));
		if (original != null) {
			collection.update(entity);
			return Response.noContent().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	
	/**
	 * Removes the entity with the specified id from the underlying collection.
	 * @param id The id of the entity to remove.
	 * @return HTTP 204 No Content on success or HTTP 404 if entity is not found.
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id")int id) {
        requireRole(deleteRole);
		TEntity entity = collection.get(id);
		if (entity != null) {
			collection.remove(entity);
			return Response.noContent().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
    
    
    /**
     * Throws an exception if the current user does not have the required role.
     * @param role 
     */
    protected void requireRole(String role) {
        if (role == null)
            return;
        auth.requireRole(uriInfo, role);
    }
    
    
    /**
     * Throws an exception if the current user is not related to the specified 
     * hub.
     * @param role 
     */
    protected void requireHub(int hubId) {
        auth.requireHub(uriInfo, hubId);
    }
    
    
    /**
     * Throws an exception if the current user is not related to the specified 
     * site.
     * @param role 
     */
    protected void requireSite(int siteId) {
        auth.requireSite(uriInfo, siteId);
    }
}
