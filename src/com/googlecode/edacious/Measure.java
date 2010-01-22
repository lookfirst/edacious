package com.googlecode.edacious;

import java.io.Serializable;

/**
 * A measure is an actual measurement of something. It has three parameters:
 * A type which can be one of the ones specified in the Measure types table.
 * A value which is the actual value of the measure (integer).
 * A unit which represents the power of 10 that has to be multipled by value to find the actual data (integer).
 *
 * @author jon
 */
public class Measure implements Serializable
{
	private static final double POUND = 0.453592;
	private static final double INCH = 0.0254;
	private static final double FOOT = 12;

	private int value;
	private int type;
	private int unit;

	private Double actualValue;

	/** */
	public Measure() {};

	/** */
	public Measure(int value, int type, int unit)
	{
		this.value = value;
		this.type = type;
		this.unit = unit;
	}

	/** */
	public int getValue()
	{
		return this.value;
	}

	/** */
	public void setValue(int value)
	{
		this.value = value;
		this.actualValue = this.getActualValue();
	}

	/** */
	public int getType()
	{
		return this.type;
	}

	/** */
	public void setType(int type)
	{
		this.type = type;
	}

	/** */
	public int getUnit()
	{
		return this.unit;
	}

	/** */
	public void setUnit(int unit)
	{
		this.unit = unit;
		this.actualValue = this.getActualValue();
	}

	/**
	 * real_value = value * 10^unit
	 *
	 * As an example, the following JSON measure { "value": 79300, "unit": -3, "type": 1 } means
	 * "This is a fat measurement of 79,3 Kg".
	 */
	public Double getActualValue()
	{
		if (this.actualValue == null)
		this.actualValue = this.value * Math.pow(10, this.unit);

		return this.actualValue;
	}

	/**
	 * Returns a nicely formatted string.
	 */
	public String getMeasure()
	{
		return this.getMeasure(null);
	}

	/**
	 * Returns a nicely formatted string based on the requested
	 * System.
	 */
	public String getMeasure(MeasureSystem system)
	{
		StringBuilder result = new StringBuilder();

		if ((this.getType() == MeasureType.WEIGHT.getType()) ||
			(this.getType() == MeasureType.FAT_FREE_MASS.getType()) ||
			(this.getType() == MeasureType.FAT_MASS_WEIGHT.getType()))
		{
			if (system == null)
				throw new IllegalStateException("MeasureSystem needs to be selected.");

			if (this.getType() == MeasureType.WEIGHT.getType())
				result.append(MeasureType.WEIGHT.getDescription());
			if (this.getType() == MeasureType.FAT_FREE_MASS.getType())
				result.append(MeasureType.FAT_FREE_MASS.getDescription());
			if (this.getType() == MeasureType.FAT_MASS_WEIGHT.getType())
				result.append(MeasureType.FAT_MASS_WEIGHT.getDescription());

			result.append(" ");

			if (system == MeasureSystem.IMPERIAL)
			{
				result.append(this.roundToHundred(this.getActualValue() / POUND));
				result.append(" lb");
			}
			else
			{
				result.append(this.roundToHundred(this.getActualValue()));
				result.append(" Kg");
			}
		}
		else if (this.getType() == MeasureType.SIZE.getType())
		{
			if (system == null)
				throw new IllegalStateException("MeasureSystem needs to be selected.");

			result.append(MeasureType.SIZE.getDescription());
			result.append(" ");
			if (system == MeasureSystem.IMPERIAL)
			{
				double inches = Math.round(this.getActualValue() / INCH);
				int foot = Double.valueOf(Math.floor(inches / FOOT)).intValue();
				int inch = Double.valueOf(inches - (FOOT * foot)).intValue();
				result.append(foot);
				result.append(" ft ");
				result.append(inch);
				result.append(" in");
				System.out.println(result);
			}
			else
			{
				result.append(this.roundToHundred(this.getActualValue()));
				result.append(" m");
			}
		}
		else if (this.getType() == MeasureType.FAT_RATIO.getType())
		{
			result.append(MeasureType.FAT_RATIO.getDescription());
			result.append(" ");
			result.append(this.getActualValue());
		}
		else
		{
			result.append("Unknown type");
		}

		return result.toString();
	}

	/** */
	public enum MeasureSystem
	{
		IMPERIAL, SI;
	}

	public static void main(String[] args)
	{
		Measure m1 = new Measure(79300, 1, -3);
		System.out.println(m1.getMeasure(MeasureSystem.IMPERIAL));
		System.out.println(m1.getMeasure(MeasureSystem.SI));
		System.out.println("----------------------------");

		Measure m2 = new Measure(173, 4, -2);
		System.out.println(m2.getMeasure(MeasureSystem.IMPERIAL));
		System.out.println(m2.getMeasure(MeasureSystem.SI));
		System.out.println("----------------------------");

		Measure m3 = new Measure(652, 5, -1);
		System.out.println(m3.getMeasure(MeasureSystem.IMPERIAL));
		System.out.println(m3.getMeasure(MeasureSystem.SI));
		System.out.println("----------------------------");

		Measure m4 = new Measure(178, 6, -1);
		System.out.println(m4.getMeasure(MeasureSystem.IMPERIAL));
		System.out.println(m4.getMeasure(MeasureSystem.SI));
		System.out.println("----------------------------");

		Measure m5 = new Measure(14125, 8, -3);
		System.out.println(m5.getMeasure(MeasureSystem.IMPERIAL));
		System.out.println(m5.getMeasure(MeasureSystem.SI));
		System.out.println("----------------------------");
	}

	/** */
	@Override
	public String toString()
	{
		return "Measure [actualValue=" + this.getActualValue() + ", type=" + this.type +
				", unit=" + this.unit + ", value=" + this.value +
				", imperial: " + this.getMeasure(MeasureSystem.IMPERIAL) +
				", si: " + this.getMeasure(MeasureSystem.SI) +
				"]";
	}

	/**
	 * GWT does not support BigDecimal (yet), so lets fudge a hack.
	 *
	 * 152.81574630945872
	 * 152.82
	 */
	public double roundToHundred(double val)
	{
		String str = Double.toString(val);
		int index = str.indexOf('.');
		String prefixChunk = str.substring(0, index);
		String decimalChunk = str.substring(index + 1);
		if (decimalChunk.length() > 3)
			decimalChunk = decimalChunk.substring(0, 3);
		double chunkDbl = Math.round(Double.valueOf(decimalChunk) * Math.pow(10, -1)) * Math.pow(10, -2);
		return (Double.valueOf(prefixChunk) + chunkDbl);
	}
}
