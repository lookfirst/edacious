package com.googlecode.edacious;

/**
 * Attribution status table
 * Attribution Mode	Description
 * 0	The measuregroup has been captured by a scale and is known to belong to this user (and is not ambiguous)
 * 1	The measuregroup has been captured by a scale but may belong to other users as well as this one (it is ambiguous)
 * 2	The measuregroup has been entered manually for this particular use
 * 4	The measuregroup has been entered manually during user creation (and may not be accurate)
 *
 * @author jon
 */
public enum AttributionStatus
{
	NOT_AMBIGUOUS("The data has been captured by a scale and is known to belong to this user (and is not ambiguous)", 0),
	AMBIGUOUS("The data has been captured by a scale but may belong to other users as well as this one (it is ambiguous)", 1),
	MANUAL("The data has been entered manually for this particular use", 2),
	USER_CREATION("The data has been entered manually during user creation (and may not be accurate)", 4);

	private String description;
	private int value;

	/** */
	private AttributionStatus(String description, int value)
	{
		this.description = description;
		this.value = value;
	}

	/** */
	public String getDescription()
	{
		return this.description;
	}

	/** */
	public int getValue()
	{
		return this.value;
	}

	/** */
	public static AttributionStatus valueOf(int ordinal)
	{
		AttributionStatus result = null;
		switch(ordinal)
		{
			case 0:
				result = NOT_AMBIGUOUS;
				break;
			case 1:
				result = AMBIGUOUS;
				break;
			case 2:
				result = MANUAL;
				break;
			case 4:
				result = USER_CREATION;
				break;
		}
		return result;
	}
}
