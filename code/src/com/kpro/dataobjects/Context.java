package com.kpro.dataobjects;
import java.util.Date;

/**
 *	Holds domain (from p3p), time, and other contextual information.
 */

public class Context
{
	private Date accessTime; //the time the p3p policy was last encountered by the end user
	private Date creationTime; //the time the p3p policy was created
	private String domain; //the domain of the p3p policy
	
	public Context(Date accessTime, Date creationTime, String domain) {
		this.accessTime = accessTime;
		this.creationTime = creationTime;
		this.domain = domain;
	}

	/**
	 * Gets the date of when the object was last accessed
	 * @return Date
	 */
	public Date getAccessTime() {
		return accessTime;
	}

	/**
	 * Sets the date of when the object was last accessed
	 * @param Date accessTime
	 */
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	/**
	 * Gets the date of when the object was created
	 * @return Date
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the date of when the object was created
	 * @param Date creationTime
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the specific domain for this context
	 * @return String
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the specific domain for this contact
	 * @param String domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
	
}