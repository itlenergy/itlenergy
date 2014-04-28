/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webcore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Config loader for authentication service.
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
class AuthenticationServiceConfig {
    static SecretKeySpec key;
    static SecretKeySpec secret;


    /**
     * Static initialiser to load keys from config.
     */
    static {
        // load keys from web.xml
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            String keyText = (String)env.lookup("TicketEncryptionKey");
            String secretText = (String)env.lookup("TicketSecret");

            MessageDigest digest = MessageDigest.getInstance(SecurityTicket.HASH_ALGORITHM);
            digest.update(keyText.getBytes());
            key = new SecretKeySpec(digest.digest(), 0, 16, SecurityTicket.ENCRYPTION_ALGORITHM);

            digest = MessageDigest.getInstance(SecurityTicket.HASH_ALGORITHM);
            digest.update(secretText.getBytes());
            secret = new SecretKeySpec(digest.digest(), SecurityTicket.HMAC_ALGORITHM);
        } catch (NamingException|NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to load configuration settings", e);
        }
    }
}