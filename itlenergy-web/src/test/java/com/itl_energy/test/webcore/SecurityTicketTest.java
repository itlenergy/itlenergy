package com.itl_energy.test.webcore;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import com.itl_energy.webcore.SecurityTicket;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
public class SecurityTicketTest {
    /**
     * Tests that a ticket can be generated and re-parsed
     */
    @Test
    public void RoundTripTest() throws NoSuchAlgorithmException {
        // get an expiry date of one day later
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        
        KeyGenerator keygen = KeyGenerator.getInstance(SecurityTicket.ENCRYPTION_ALGORITHM);
        byte[] keyBytes = keygen.generateKey().getEncoded();
        SecretKeySpec key = new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityTicket.ENCRYPTION_ALGORITHM);
        
        byte[] secretBytes = keygen.generateKey().getEncoded();
        SecretKeySpec secret = new SecretKeySpec(secretBytes, 0, secretBytes.length, SecurityTicket.ENCRYPTION_ALGORITHM);
        
        SecurityTicket ticket = new SecurityTicket(cal.getTime(), "fred", "admin", 0, false);
        String encrypted = ticket.encrypt(key, secret);
        
        SecurityTicket decrypted = SecurityTicket.parse(encrypted, key, secret);
        assertNotNull(decrypted);
        
        assertEquals(ticket.getExpires(), decrypted.getExpires());
        assertEquals(ticket.getUsername(), decrypted.getUsername());
        assertEquals(ticket.getRole(), decrypted.getRole());
        assertEquals(ticket.getReissue(), decrypted.getReissue());
    }
}