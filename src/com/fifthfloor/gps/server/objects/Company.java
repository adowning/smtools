package com.fifthfloor.gps.server.objects;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.authorization.Role;
import org.vaadin.appfoundation.persistence.data.AbstractPojo;

import java.util.Iterator;
import java.util.LinkedList;
/**
 * Entity class for users. This class keeps information about registered users.
 * 
 * This class is initially provided by http://code.google.com/p/vaadin-appfoundation/
 * and has been modified to use with http://code.google.com/p/instant-webapp/
 * 
 * @author Kim
 * @author Mischa
 * 
 */
@Entity
@Table(name = "appcompany", uniqueConstraints = { @UniqueConstraint(columnNames = { "cname" } ) })
public class Company extends AbstractPojo {

    private static final long serialVersionUID = 4417119399127203109L;

    protected String cname = "";
    
    protected String owner = "asdf";
    // This field may contain the new password in clear text and must never be persisted  
        @Transient
    private String newPassword;

    protected String password = "";

    private String name = "";

    private String gpstype = "";
    
    private String email = "";

    private int failedLoginAttempts = 0;

    private boolean accountLocked = false;

    private String reasonForLockedAccount;
    
    private LinkedList<String> vehicleList = new LinkedList();
    
    private LinkedList<String> smsList = new LinkedList();

    
    
    public LinkedList<String> getSmsList() {
		return smsList;
	}

	public void addSms(String sms) {
		this.smsList.add(sms);
	}

	public LinkedList getVehicleList() {
		return vehicleList;
	}

	public void addVehicle(String veh) {
		this.vehicleList.add(veh);
	}
	
	public boolean hasVehicle(String veh){
		boolean b = false;
		if(vehicleList.contains(veh)){
			return true;
		}
		
		return b;
	}

	
    /**
     * Get the username of the user
     * 
     * @return User's username
     */
	
    public String getOwner() {
        return owner;
    }

    /**
     * Set the username for the user
     * 
     * @param username
     *            New username
     */
    public void setOwner(String ownername) {
        this.owner = ownername;
    }
    
    
    public String getCname() {
        return cname;
    }

    /**
     * Set the username for the user
     * 
     * @param username
     *            New username
     */
    public void setCname(String username) {
        this.cname = username;
    }

    /**
     * Get the (encrypted) password of the user
     * 
     * @return Encrypted password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Set the (encrypted) password of the user
     * 
     * @param password
     *            New hashed password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Convenience method to use with forms, as displaying a hashed 
     * password makes most of the time no sense and displaying a clear text 
     * password would violate most security policies 
     * 
     * @return Always an empty String
     */
    public String getPasswordAsString() {
        return "";
    }
    
    /**
     * Convenience method to simplify the usage of this class with Vaadin forms.
     * The "newPassword" property is intended to be a temporary in-memory helper.
     * <p/>
     * The password set here can be retrieved with {@link User#getNewPassword()}.
     * 
     * Use {@link User#setPassword(String)} to set the (encrypted) password 
     * that will be persisted
     * 
     * @param password
     *            New hashed password
     */
    public void setPasswordAsString(String password) {
        if (password != null && password.length() > 0) {
                this.newPassword = password;
        }
    }    

    /**
     * This is a convenience method to use this class with Vaadin forms.
     * Use this method to retrieve the value set with {@link User#setPasswordAsString(String)}
     * 
     * @return
     */
    public String getNewPassword() {
        return newPassword;
    }
    
    /**
     * Set the actual name of the user
     * 
     * @param name
     *            New name for the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the actual name of the user
     * 
     * @return Name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Set an email address for the user
     * 
     * @param email
     *            New email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's email address
     * 
     * @return User's email address
     */
    public String getEmail() {
        return email;
    }

	public String getGpstype() {
		return gpstype;
	}

	public void setGpstype(String gpstype) {
		this.gpstype = gpstype;
	}


}
