package com.googlecode.edacious;

/**
 * Measure types table
 * Type	Description
 * 1	Weight (Kg)
 * 4	Size (meter)
 * 5	Fat Free Mass (Kg)
 * 6	Fat Ratio (%)
 * 8	Fat Mass Weight (Kg)
 *
 * @author jon
 */
public enum MeasureType
{
	WEIGHT("Weight", 1),
	SIZE("Size", 4),
	FAT_FREE_MASS("Fat Free Mass", 5),
	FAT_RATIO("Fat Ratio (%)", 6),
	FAT_MASS_WEIGHT("Fat Mass Weight", 8);

	private String description;
	private int type;

	/** */
	private MeasureType(String description, int type)
	{
		this.description = description;
		this.type = type;
	}

	/** */
	public String getDescription()
	{
		return this.description;
	}

	/** */
	public int getType()
	{
		return this.type;
	}
}
