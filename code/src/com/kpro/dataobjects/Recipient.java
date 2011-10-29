package com.kpro.dataobjects;

/**
 * The recipients that a case can contain. See P3P specs for more info.
 * @author ernie
 *
 */
public enum Recipient {
	OURS,
	DELIVERY,
	SAME,
	OTHER_RECIPIENT,
	UNRELATED,
	PUBLIC;
}
