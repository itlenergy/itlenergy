/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webcore;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for login action.
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@XmlRootElement
public class ChangePasswordModel {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
