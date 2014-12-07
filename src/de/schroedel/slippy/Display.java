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

		drawOverlay(game, g2);
	}

	private void drawOverlay(Game game, Graphics2D g2)
	{
		if (Game.state == Game.GAME_STARTING)
			drawWelcome(g2);
		else if (Game.state == Game.GAME_STOPPED)
			drawPause(g2);
	}

	private void drawWelcome(Graphics2D g2)
	{
		String welcome = "Welcome !!!";
		String startHint = "Hit 'Space' to start";

		drawCenteredString(welcome, g2);
		FontMetrics fm = g2.getFontMetrics();

		int x = (getWidth() - fm.stringWidth(startHint)) / 2;
		int y = getHeight()/2 + fm.getDescent();

		g2.drawString(startHint, x, y + fm.getHeight());
	}

	private void drawPause(Graphics2D g2)
	{
		drawCenteredString("Pause - Resume with 'P'", g2);
	}

	private void drawPoints(long points, Graphics2D g2)
	{
		String pointsString = "Points: " + points;
		FontMetrics fm = g2.getFontMetrics();

		g2.setFont(font);
		g2.setColor(Color.BLACK);

		int x = (getWidth() - fm.stringWidth(pointsString)) / 2;
		int y = getHeight() - fm.getDescent()*10;

		g2.drawString(pointsString, x, y);
	}

	private void drawTime(long time, Graphics2D g2)
	{
		String pointsString = "Time: " + time;
		FontMetrics fm = g2.getFontMetrics();

		g2.setFont(font);
		g2.setColor(Color.RED);

		int x = (getWidth() - fm.stringWidth(pointsString)) / 2;
		int y = fm.getDescent()*10;

		g2.drawString(pointsString, x, y);
	}

	private void drawCenteredString(String str, Graphics2D g2)
	{
		FontMetrics fm = g2.getFontMetrics();

		g2.setFont(font);
		g2.setColor(Color.BLACK);

		int x = (getWidth() - fm.stringWidth(str)) / 2;
		int y =
			(fm.getAscent() +
			(getHeight() - (fm.getAscent() + fm.getDescent())) / 2);

		g2.drawString(str, x, y);
	}
}
