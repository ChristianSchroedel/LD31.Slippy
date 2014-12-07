package de.schroedel.slippy;

import de.schroedel.slippy.Window;
import de.schroedel.slippy.GameObjects;
import de.schroedel.slippy.entities.Player;
import de.schroedel.slippy.entities.Collectable;
import de.schroedel.slippy.entities.Obstacle;
import de.schroedel.slippy.entities.Entity;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;


public class Controls implements KeyListener
{
	private Window window;
	private Game game;

	private List<OnCollectListener> collListeners;
	private List<OnObstacleListener> obsListeners;

	private List<Integer> keyEvents;

	public interface OnCollectListener
	{
		void onCollect(Collectable coll);
	}

	public interface OnObstacleListener
	{
		void onObstacle(Obstacle obs);
	}

	public Controls(Window window, Game game)
	{
		this.window = window;
		this.game = game;

		keyEvents = new ArrayList<Integer>();
		collListeners = new ArrayList<OnCollectListener>();
		obsListeners = new ArrayList<OnObstacleListener>();
	}

	public void register()
	{
		window.addKeyListener(this);
	}

	public void addOnCollectListener(OnCollectListener listener)
	{
		collListeners.add(listener);
	}

	public void addOnObstacleListener(OnObstacleListener listener)
	{
		obsListeners.add(listener);
	}

	public void processInputs(float dt)
	{
		Player player = game.getGameObjects().getPlayer();

		for (int code : keyEvents)
		{
			if (Game.state == Game.GAME_STARTING)
			{
				if (code == KeyEvent.VK_SPACE)
					game.start();
			}
			else if (Game.state == Game.GAME_RUNNING)
			{
				if (code == KeyEvent.VK_W)
					player.moveUp();
				else if (code == KeyEvent.VK_A)
					player.moveLeft();
				else if (code == KeyEvent.VK_S)
					player.moveDown();
				else if (code == KeyEvent.VK_D)
					player.moveRight();
				else if (code == KeyEvent.VK_ESCAPE)
				{
					game.stop();
					game.reset();
				}
				else if (code == KeyEvent.VK_P)
					game.stop();
			}
			else if (Game.state == Game.GAME_STOPPED)
			{
				if (code == KeyEvent.VK_P)
					game.start();
			}
		}

		if (Game.state != Game.GAME_RUNNING)
		{
			keyEvents.clear();
			return;
		}

		checkBounds(
			player,
			window.getDisplay().getWidth(),
			window.getDisplay().getHeight());
		checkCollectableColission(
			player,
			game.getGameObjects().getCollectables());
		checkObstacleColission(
			player,
			game.getGameObjects().getObstacles());
		player.move(dt);

		keyEvents.clear();
	}

	private void checkBounds(Player player, int width, int height)
	{
		if (player.x < 0)
		{
			player.x = 0;
			player.xVel = -player.xVel * player.bounce;
		}
		if (player.y < 0)
		{
			player.y = 0;
			player.yVel = -player.yVel * player.bounce;
		}
		if (player.x > width-player.width)
		{
			player.x = width-player.width;
			player.xVel = -player.xVel * player.bounce;
		}
		if (player.y > height-player.height)
		{
			player.y = height-player.height;
			player.yVel = -player.yVel * player.bounce;
		}
	}

	private void checkCollectableColission(
		Player player,
		List<Collectable> colls)
	{
		Collectable colidor = null;

		for (Collectable coll : colls)
		{
			if (Entity.checkColission(player, coll))
				colidor = coll;
		}

		if (colidor != null)
		{
			for (OnCollectListener listener : collListeners)
				listener.onCollect(colidor);
		}
	}

	private void checkObstacleColission(
		Player player,
		List<Obstacle> obstacles)
	{
		Obstacle colidor = null;

		for (Obstacle obs : obstacles)
		{
			if (Entity.checkColission(player, obs))
				colidor = obs;
		}

		if (colidor != null)
		{
			for (OnObstacleListener listener : obsListeners)
				listener.onObstacle(colidor);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();

		if (Game.state == Game.GAME_STARTING)
		{
			if (code == KeyEvent.VK_SPACE)
				keyEvents.add(code);

			return;
		}

		if (code == KeyEvent.VK_W ||
			code == KeyEvent.VK_A ||
			code == KeyEvent.VK_S ||
			code == KeyEvent.VK_D ||
			code == KeyEvent.VK_ESCAPE ||
			code == KeyEvent.VK_P)
			keyEvents.add(code);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}
}
