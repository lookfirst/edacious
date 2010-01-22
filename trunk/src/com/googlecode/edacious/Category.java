package com.googlecode.edacious;

/**
 * Category table
 * Category	Description
 * 1	Measure
 * 2	Target
 *
 * @author jon
 */
public enum Category
{
	MEASURE("Measure", 1),
	TARGET("Target", 2);

	private String description;
	private int value;

	/** */
	private Category(String description, int value)
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
	public static Category valueOf(int ordinal)
	{
		Category result = null;
		switch (ordinal)
		{
			case 1:
				result = MEASURE;
				break;
			case 2:
				result = TARGET;
				break;
		}
		return result;
	}
}
