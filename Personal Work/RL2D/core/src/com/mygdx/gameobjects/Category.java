package com.mygdx.gameobjects;

/**
 * This is an enum for collision filtering.
 * 
 * @author mmekker
 *
 */
public enum Category {
	CATEGORY_WALL((short) 0X0001), CATEGORY_BALL((short) 0x0002), CATEGORY_CAR((short) 0x0004), CATEGORY_TARGET(
			(short) 0x0008);

	public short value;

	private Category(short value) {
		this.value = value;
	}
};
