/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itl_energy.webcore;

import com.itl_energy.model.IsoDateAdapter;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * DTO response for login action.
 * @author Gordon Mackenzie-Leigh <gordon@stugo.co.uk>
 */
@XmlRootElement
public class TicketModel {
    private String ticket;
    @XmlJavaTypeAdapter(IsoDateAdapter.class)
    private Date expires;
    
    public TicketModel() {
    }
    
    
    public TicketModel(String ticket, Date expires) {
        this.ticket = ticket;
        this.expires = expires;
    }
    

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
