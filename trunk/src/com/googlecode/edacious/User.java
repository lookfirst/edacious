package com.googlecode.edacious;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Name	Description
 * id	The user identifier
 * firstname	The user's firstname
 * lastname		The user's lastname
 * shortname	The user's shortname
 * gender		The user's gender (0 for male, 1 for female)
 * fatmethod	Byte indicating the Body Composition Formula in use
 * birthdate	The user's birthdate in Epoch format (number of seconds since January 1st, 1970)
 *
 * @author jon
 */
public class User implements Serializable
{
	private Long id;
	private String firstname;
	private String lastname;
	private String shortname;
	private String publicKey;
	private boolean isFemale;
//	private String fatmethod;
	private Date birthDate;
	private boolean isPublic;

	/** */
	public User() {};

	/** */
	public User(Long id, String firstname, String lastname, String shortname, String publicKey, boolean isFemale, Date birthDate)
	{
		this(id, firstname, lastname, shortname, publicKey, isFemale, false, birthDate);
	}

	/** */
	public User(Long id, String firstname, String lastname, String shortname, String publicKey, boolean isFemale, boolean isPublic, Date birthDate)
	{
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.shortname = shortname;
		this.publicKey = publicKey;
		this.isFemale = isFemale;
		this.isPublic = isPublic;
		this.birthDate = birthDate;
	}

	/** */
	public String getFirstname()
	{
		return this.firstname;
	}

	/** */
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	/** */
	public String getLastname()
	{
		return this.lastname;
	}

	/** */
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	/** */
	public String getShortname()
	{
		return this.shortname;
	}

	/** */
	public void setShortname(String shortname)
	{
		this.shortname = shortname;
	}

	/** */
	public boolean isFemale()
	{
		return this.isFemale;
	}

	/** */
	public void setFemale(boolean isFemale)
	{
		this.isFemale = isFemale;
	}

	/** */
	public Date getBirthDate()
	{
		return this.birthDate;
	}

	/** */
	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	/** */
	public void setPublic(boolean isPublic)
	{
		this.isPublic = isPublic;
	}

	/** */
	public boolean isPublic()
	{
		return this.isPublic;
	}

	/** */
	public void setPublicKey(String publicKey)
	{
		this.publicKey = publicKey;
	}

	/** */
	public String getPublicKey()
	{
		return this.publicKey;
	}

	/** */
	public void setId(Long id)
	{
		this.id = id;
	}

	/** */
	public Long getId()
	{
		return this.id;
	}

	/** */
	@Override
	public String toString()
	{
		return "User [id=" + this.id + ", firstname=" + this.firstname + ", lastname="
				+ this.lastname + ", shortname=" + this.shortname + ", publicKey="
				+ this.publicKey + ", birthDate=" + this.birthDate + ", isFemale="
				+ this.isFemale + ", isPublic=" + this.isPublic + "]";
	}
}
