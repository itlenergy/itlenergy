/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.repo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.itl_energy.model.User;

/**
 *
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@Stateless
public class UserCollection extends CollectionBase<User> {
    public static final String CHARSET = "UTF-8";
    public static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int HASH_ITERATION_COUNT = 1024;
    public static final int HASH_KEY_LENGTH = 512;
    
    
    public UserCollection() {
        super(User.class);
    }
    
    
    /**
     * Gets a user with the specified username.
     * @param username
     * @return The user or null if not found.
     */
    public User get(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> users = cq.from(User.class);
        Predicate p = cb.equal(users.get("username"), username);
        cq.where(p).select(users);
        List<User> results = em.createQuery(cq).getResultList();
        
        if (results.size() == 1)
            return results.get(0);
        else
            return null;
    }
    
    
    /**
     * Gets a user with the specified username and password.
     * @param username
     * @param password
     * @return The user or null if not found.
     */
    public User get(String username, String password) {
        User u = get(username);
        
        if (u == null || !Arrays.equals(u.getPassword(), getPasswordHash(username, password)))
            return null;
        else
            return u;
    }
    
    
    /**
     * Calculates the password hash for the given username and password.
     * @param username
     * @param password
     * @return 
     */
    public byte[] getPasswordHash(String username, String password) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            KeySpec ks = new PBEKeySpec(password.toCharArray(), username.getBytes(CHARSET), HASH_ITERATION_COUNT, HASH_KEY_LENGTH);
            SecretKey s = f.generateSecret(ks);
            return s.getEncoded();
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException|InvalidKeySpecException ex) {
            throw new RuntimeException("Failed to get password hash: " + ex.getMessage());
        }
    }

    
    @Override
    public int getId(User entity) {
        return entity.getUserId();
    }
}
