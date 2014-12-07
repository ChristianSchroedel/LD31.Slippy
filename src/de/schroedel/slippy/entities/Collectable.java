package de.schroedel.slippy.entities;

import de.schroedel.slippy.entities.Entity;

import javax.swing.ImageIcon;


public class Collectable extends Entity
{
	public int value;

	public Collectable(float x, float y, ImageIcon icon, int value)
	{
		super(x, y, icon);

		this.value = value;
	}
}
