/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webcore;

/**
 *
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("The current user is not authorized.");
    }
}
