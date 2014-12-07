package de.schroedel.slippy.entities;

import javax.swing.ImageIcon;


public class Entity
{
	public float x;
	public float y;
	public int width;
	public int height;
	public ImageIcon icon;

	public Entity(float x, float y, ImageIcon icon)
	{
		this.x = x;
		this.y = y;
		this.icon = icon;
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
	}

	public static boolean checkColission(Entity a, Entity b)
	{
		float xDiff = (a.x+a.width/2) - (b.x+b.width/2);
		float yDiff = (a.y+a.height/2) - (b.y+b.height/2);

		float distance = (float) Math.sqrt(xDiff*xDiff + yDiff*yDiff);

		if (distance <= a.width/2+b.width/2 &&
			distance <= a.height/2+b.height/2)
			return true;

		return false;
	}
}
