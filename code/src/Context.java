import java.util.Date;

/**
*	holds domain (from p3p), time, and other contextual information.
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

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	
	
}