/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itl_energy.webclient.itl.model;

import java.util.List;

/**
 *
 * @author stewart
 */
public class Items<T> {
    private List<T> items;
    
    public Items() {
        
    }
    
    public Items(List<T> items) {
        this.items = items;
    }
    
    public List<T> getItems() {
        return items;
    }
}
