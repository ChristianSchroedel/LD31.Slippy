package de.schroedel.slippy;

import de.schroedel.slippy.Game;
import de.schroedel.slippy.GameObjects;
import de.schroedel.slippy.entities.Player;
import de.schroedel.slippy.entities.Collectable;
import de.schroedel.slippy.entities.Obstacle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;


public class Display extends JPanel
{
	private Game game;
	private Font font;

	private Rectangle2D rect;

	public Display(Game game)
	{
		this.game = game;

		font = new Font("Dialog", Font.BOLD, 25);
		rect = new Rectangle2D.Double();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		GameObjects gameObjects = game.getGameObjects();

		Player player = gameObjects.getPlayer();
		List<Collectable> colls = gameObjects.getCollectables();
		List<Obstacle> obstacles = gameObjects.getObstacles();

		try
		{
			for (Collectable coll : colls)
				g2.drawImage(
					coll.icon.getImage(),
					(int) coll.x,
					(int) coll.y,
					this);

			for (Obstacle obs : obstacles)
				g2.drawImage(
					obs.icon.getImage(),
					(int) obs.x,
					(int) obs.y,
					this);
		}
		catch (ConcurrentModificationException e)
		{
			return;
		}

		g2.drawImage(
			player.icon.getImage(),
			(int) player.x,
			(int) player.y,
			this);
	}
}
