package com.googlecode.edacious;

import java.io.IOException;

/**
 * Code	Description
 * 0	Operation was successfull
 * 2555	An unknown error occured
 * 247	The userid provided is absent, or incorrect
 * 250	The userid and publickey provided do not match, or the user does not share its data.
 * 264	The email address provided is either unknown or invalid
 * 100	The hash is missing, invalid, or does not match the provided email
 * 293	The callback URL is either absent or incorrect
 * 294	No such subscription could be deleted
 * 286	No such subscription was found
 * 284	Temporary failure on Withings side (saw this in the forums)
 *
 * @author jon
 */
public class EdaciousException extends Exception
{
	private String message;
	private int code;

	/** */
	public EdaciousException(IOException ex)
	{
		this.initCause(ex);
	}

	/** */
	public EdaciousException(String message, int code)
	{
		this.message = message;
		this.code = code;
	}

	/** */
	@Override
	public String getMessage()
	{
		return this.message;
	}

	/** */
	public int getCode()
	{
		return this.code;
	}
}
