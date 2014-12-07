package de.schroedel.slippy.entities;

import de.schroedel.slippy.entities.Entity;

import javax.swing.ImageIcon;


public class Obstacle extends Entity
{
	public int value;

	public Obstacle(float x, float y, ImageIcon icon, int value)
	{
		super(x, y, icon);

		this.value = value;
	}
}
