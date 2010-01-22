package com.googlecode.edacious;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Measure group content fields
 * Name	Description
 * grpid	A unique grpid (Group Id), useful for performing synchronization tasks.
 * attrib	An attrib (Attribute), defining the way the measure was attributed to the user. It may take the values shown in the Attribution status table below.
 * date		The date (EPOCH format) at which the measure was taken or entered.
 * category	The category of the group. A measure group can represent either measures captured by the BodyScale or objectives set by the user. The category field indicates for each measure group whether the measures in the group are measurements or targets. Refer to the Category values table below.
 * measures	An array of measures that belong to this particular measure group.
 *
 * @author jon
 */
public class MeasureGroup implements Serializable
{
	private int groupId;
	private AttributionStatus attributionStatus;
	private Date creationDate;
	private Category category;
	private List<Measure> measures;

	/** */
	public MeasureGroup() {}

	/** */
	public MeasureGroup(int groupId, AttributionStatus attributionStatus, Date creationDate, Category category, List<Measure> measures)
	{
		this.groupId = groupId;
		this.attributionStatus = attributionStatus;
		this.creationDate = creationDate;
		this.category = category;
		this.measures = measures;
	}

	/** */
	public int getGroupId()
	{
		return this.groupId;
	}

	/** */
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	/** */
	public AttributionStatus getAttributionStatus()
	{
		return this.attributionStatus;
	}

	/** */
	public void setAttributionStatus(AttributionStatus attributionStatus)
	{
		this.attributionStatus = attributionStatus;
	}

	/** */
	public Date getCreationDate()
	{
		return this.creationDate;
	}

	/** */
	public void setCreationDate(Date creationDate)
	{
		creationDate = this.creationDate;
	}

	/** */
	public Category getCategory()
	{
		return this.category;
	}

	/** */
	public void setCategory(Category category)
	{
		this.category = category;
	}

	/** */
	public List<Measure> getMeasures()
	{
		return this.measures;
	}

	/** */
	public void setMeasures(List<Measure> measures)
	{
		this.measures = measures;
	}

	/** */
	@Override
	public String toString()
	{
		return "MeasureGroup [attributionStatus=" + this.attributionStatus
				+ ", category=" + this.category + ", creationDate=" + this.creationDate
				+ ", groupId=" + this.groupId + ", measures=" + this.measures + "]";
	}
}
