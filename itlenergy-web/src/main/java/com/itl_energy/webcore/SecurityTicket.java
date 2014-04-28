/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webcore;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Represents a ticket stored in an HTTP cookie that carries authentication info.
 * 
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
public class SecurityTicket {
    public static final String CHARSET = "UTF-8";
    public static final String ENCRYPTION_ALGORITHM = "AES";
    public static final String ENCRYPTION_ALGORITHM_WITH_PADDING = "AES/CBC/PKCS5Padding";
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final String HMAC_ALGORITHM = "HmacSHA1";
    public static final String RNG_ALGORITHM = "SHA1PRNG";
    public static final int HMAC_SIZE = 20;
    public static final int ENCRYPTION_BLOCK_SIZE = 16;
    
    public static final int EXPIRES_MINS = 20;
    public static final int EXPIRES_MS = EXPIRES_MINS * 60000;
    
    public static final int USERNAME_SIZE_LENGTH = 4;
    public static final int ROLE_SIZE_LENGTH = 4;
    public static final int RELATED_LENGTH = 4;
    public static final int EXPIRES_LENGTH = 8;
    public static final int REISSUE_LENGTH = 1;
    public static final int FIXED_SIZE = USERNAME_SIZE_LENGTH + ROLE_SIZE_LENGTH + RELATED_LENGTH + EXPIRES_LENGTH + REISSUE_LENGTH;
    
    private String username;
    private String role;
    private int relatedId;
    private Date expires;
    private boolean reissue;
    
    
    public static SecurityTicket parse(String value, SecretKeySpec key, SecretKeySpec secret) {
        byte[] raw = decrypt(value, key);
        
        // ticket has to be at least big enough for fixed fields and signature.
        if (raw.length < HMAC_SIZE + FIXED_SIZE)
            return null;
        
        ByteBuffer buffer = ByteBuffer.wrap(raw);
        byte[] signature = new byte[HMAC_SIZE];
        buffer.get(signature);
        
        // check signature
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secret);
            mac.update(raw, HMAC_SIZE, raw.length - HMAC_SIZE);
            byte[] compareSig = mac.doFinal();
            
            if (!Arrays.equals(compareSig, signature))
                return null;
        } catch (InvalidKeyException|NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to compute signature: " + ex.getMessage(), ex);
        }
        
        Date expires = new Date(buffer.getLong());
        
        // check expiry.
        if (expires.before(new Date()))
            return null;
        
        int usernameSize = buffer.getInt();
        int roleSize = buffer.getInt();
        int relatedId = buffer.getInt();
        boolean reissue = buffer.get() == 1;
        String username, role;
        
        // check the values are sensible.
        if (buffer.remaining() != usernameSize + roleSize)
            return null;
        
        byte[] usernameBytes = new byte[usernameSize];
        buffer.get(usernameBytes);
        
        byte[] roleBytes = new byte[roleSize];
        buffer.get(roleBytes);
        
        try {
            username = new String(usernameBytes, CHARSET);
            role = new String(roleBytes, CHARSET);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Failed to parse ticket: " + ex.getMessage(), ex);
        }
        
        return new SecurityTicket(expires, username, role, relatedId, reissue);
    }
    
    
    /**
     * Decrypts a base64-encoded ticket and returns the raw value.
     */
    public static byte[] decrypt(String value, SecretKeySpec key) {
        try {
            byte[] raw = Base64.decodeBase64(value);
            
            if (raw.length < ENCRYPTION_BLOCK_SIZE)
                return new byte[0];
            
            ByteBuffer buffer = ByteBuffer.wrap(raw);
            
            byte[] ivBytes = new byte[ENCRYPTION_BLOCK_SIZE];
            buffer.get(ivBytes);
            
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            Cipher aes = Cipher.getInstance(ENCRYPTION_ALGORITHM_WITH_PADDING);
            aes.init(Cipher.DECRYPT_MODE, key, iv);
            return aes.doFinal(raw, ENCRYPTION_BLOCK_SIZE, raw.length - ENCRYPTION_BLOCK_SIZE);
        } catch (IllegalBlockSizeException|BadPaddingException ex) {
            // bad ticket format probably
            return new byte[0];
        } catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|InvalidAlgorithmParameterException ex) {
            // all of these exceptions are problems with the init and there is no "nice" way to handle
            throw new RuntimeException("Failed to decrypt ticket: " + ex.getMessage(), ex);
        }
    }
    
    
    /**
     * Creates a new empty security ticket.
     */
    public SecurityTicket() {
    }
    
    
    /**
     * Creates a new security ticket with the given values.
     * @param expires
     * @param username
     * @param role
     * @param relatedId 
     */
    public SecurityTicket(Date expires, String username, String role, int relatedId, boolean reissue) {
        this.expires = expires;
        this.username = username;
        this.role = role;
        this.relatedId = relatedId;
        this.reissue = reissue;
    }
    
    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getRole() {
        return role;
    }

    
    public void setRole(String role) {
        this.role = role;
    }

    
    public int getRelatedId() {
        return relatedId;
    }

    
    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }
    
    
    public Date getExpires() {
        return expires;
    }
    
    
    public void setExpires(Date expires) {
        this.expires = expires;
    }
    
    
    public boolean getReissue() {
        return reissue;
    }
    
    
    public void setReissue(boolean reissue) {
        this.reissue = reissue;
    }
    
    
    /**
     * Gets the raw value of the ticket as a sequence of bytes.
     */
    public byte[] getRawValue() {
        try {
            byte[] usernameBytes = username.getBytes(CHARSET);
            byte[] roleBytes = role.getBytes(CHARSET);
            ByteBuffer raw = ByteBuffer.allocate(FIXED_SIZE+usernameBytes.length+roleBytes.length);
            raw.putLong(expires.getTime());
            raw.putInt(usernameBytes.length);
            raw.putInt(roleBytes.length);
            raw.putInt(relatedId);
            raw.put((byte)(reissue ? 1 : 0));
            raw.put(usernameBytes);
            raw.put(roleBytes);
            return raw.array();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Failed to get raw value: " + ex.getMessage(), ex);
        }
    }
    
    
    /**
     * Gets the raw value and appends a HMAC to verify the content.
     */
    public byte[] sign(SecretKeySpec secret) {
        try {
            byte[] rawValue = getRawValue();
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secret);
            byte[] signature = mac.doFinal(rawValue);
            ByteBuffer raw = ByteBuffer.allocate(rawValue.length + signature.length);
            raw.put(signature);
            raw.put(rawValue);
            return raw.array();
        } catch (InvalidKeyException|NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to compute signature: " + ex.getMessage(), ex);
        }
    }
    
    
    /**
     * Computes the signed ticket value and encrypts it, then converts to Base64
     * encoding.
     */
    public String encrypt(SecretKeySpec key, SecretKeySpec secret) {
        try {
            byte[] rawValue = sign(secret);
            
            SecureRandom rng = SecureRandom.getInstance(RNG_ALGORITHM);
            byte[] ivBytes = new byte[ENCRYPTION_BLOCK_SIZE];
            rng.nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            
            Cipher aes = Cipher.getInstance(ENCRYPTION_ALGORITHM_WITH_PADDING);
            aes.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted = aes.doFinal(rawValue);
            
            ByteBuffer buffer = ByteBuffer.allocate(ivBytes.length + encrypted.length);
            buffer.put(ivBytes);
            buffer.put(encrypted);
            
            return Base64.encodeBase64URLSafeString(buffer.array());
        } catch (NoSuchAlgorithmException|NoSuchPaddingException|IllegalBlockSizeException|BadPaddingException|InvalidKeyException|InvalidAlgorithmParameterException ex) {
            // all of these exceptions are problems with the init and there is no "nice" way to handle
            throw new RuntimeException("Failed to encrypt ticket: " + ex.getMessage(), ex);
        }
    }
}
