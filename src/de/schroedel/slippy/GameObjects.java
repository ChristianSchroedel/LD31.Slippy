package de.schroedel.slippy;

import de.schroedel.slippy.entities.Player;
import de.schroedel.slippy.entities.Obstacle;
import de.schroedel.slippy.entities.Collectable;

import java.util.List;
import java.util.ArrayList;


public class GameObjects
{
	private Player player;
	private List<Obstacle> obstacles;
	private List<Collectable> collectables;

	public GameObjects()
	{
		collectables = new ArrayList<Collectable>();
		obstacles = new ArrayList<Obstacle>();
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}

	public List<Collectable> getCollectables()
	{
		return collectables;
	}

	public void addCollectable(Collectable collectable)
	{
		collectables.add(collectable);
	}

	public List<Obstacle> getObstacles()
	{
		return obstacles;
	}
}
