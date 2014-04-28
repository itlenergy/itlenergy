package com.itl_energy.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;


/**
 * The base class for all collections.  Defines common functionality.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 *
 */
public abstract class CollectionBase<TEntity> {
	private final Class<TEntity> entityClass;
	
	@PersistenceContext
	protected EntityManager em;
	
	
	/**
	 * Creates a new instance with the specified entity manager.
	 * @param em
	 * @param entityName
	 */
	protected CollectionBase(Class<TEntity> entityClass) {
		this.entityClass = entityClass;
	}
    
    
    /**
     * Gets the name of the entity class.
     * @return String
     */
    protected String getEntityName() {
        return this.entityClass.getSimpleName();
    }
    
    
    protected TypedQuery<TEntity> createNamedQuery(String name) {
        return em.createNamedQuery(getEntityName() + "." + name, entityClass);
    }
	
	
	/**
	 * Gets an entity with the given id.
	 * 
	 * @param id
	 * @return A single entity.
	 */
	public TEntity get(@PathParam("id")int id){
		return em.find(entityClass, id);
	}
	
	
	/**
	 * Gets all the entities from the collection.
	 * @return A list of entities.
	 */
	public List<TEntity> getAll() {
		return createNamedQuery("findAll").getResultList();
	}
	
	
	/**
	 * Saves an entity to the collection.
	 * @param entity
	 * @throws javax.persistence.EntityExistsException if the entity already exists.
	 */
	public void insert(TEntity entity){
		em.persist(entity);
	}
    
    
    /**
	 * Saves an entity to the collection.
	 * @param entity
	 * @throws javax.persistence.EntityExistsException if the entity already exists.
	 */
	public void insert(List<TEntity> entities){
        for (TEntity entity : entities) {
            em.persist(entity);
        }
	}
	
	
	/**
	 * Updates an existing entity in the collection.
	 * @param entity
	 */
	public void update(TEntity entity){
		em.merge(entity);
	}
    
    
    /**
	 * Updates an existing entity in the collection.
	 * @param entity
	 */
	public void update(List<TEntity> entities){
        for (TEntity entity : entities) {
            em.merge(entity);
        }
	}
	
	
	/**
	 * Removes an existing entity from the collection.
	 * @param entity
	 */
	public void remove(TEntity entity){
		em.remove(entity);
	}
	
	
	/**
	 * Removes the entity specified by the given id.
	 * @param id
	 */
	public void removeById(int id){
		TEntity entity = get(id);
		remove(entity);
	}
	
	
	/**
	 * Gets the underlying EnitityManager transaction.
	 * @return
	 */
	public EntityTransaction getTransaction(){
		return em.getTransaction();
	}
	
	/**
	 * Persists changes to the underlying database immediately.
	 */
	public void flush() {
		em.flush();
	}
	
	
	/**
	 * Helper method which gets the value of the id for the entity.
	 * @param The entity to get the id for.
	 * @return
	 */
	public abstract int getId(TEntity entity);
}