/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itl_energy.webclient.itl.util;

/**
 * An exception representing errors resulting from calling API methods.
 * @author stewart
 */
public class ApiException extends Exception {
    public ApiException(String message) {
        super(message);
    }
    
    public ApiException(Throwable cause) {
        super("Error communicating with remote API.", cause);
    }
}
