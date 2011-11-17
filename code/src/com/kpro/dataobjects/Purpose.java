package com.kpro.dataobjects;

/**
 * The purposes that a case can contain. See P3P specs for more info.
 * @author ernie
 *
 */
public enum Purpose {
	CURRENT,
	ADMIN,
	DEVELOP,
	TAILORING,
	PSEUDO_ANALYSIS,
	PSEUDO_DECISION,
	INDIVIDUAL_ANALYSIS,
	INDIVIDUAL_DECISION,
	CONTACT,
	HISTORICAL,
	TELEMARKETING,
	OTHER_PURPOSE,
	CUSTOMIZATION;
	
	private boolean optional = false;

	public void setOptional() {
		this.optional = true;
	}

	public boolean isOptional() {
		return this.optional;
	}
}
