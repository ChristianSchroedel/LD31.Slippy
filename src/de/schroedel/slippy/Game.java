package de.schroedel.slippy;

import de.schroedel.slippy.Window;
import de.schroedel.slippy.Controls;
import de.schroedel.slippy.GameObjects;
import de.schroedel.slippy.entities.Player;
import de.schroedel.slippy.entities.Obstacle;
import de.schroedel.slippy.entities.Collectable;
import de.schroedel.slippy.entities.Entity;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;


public class Game implements
	Runnable,
	Controls.OnCollectListener,
	Controls.OnObstacleListener
{
	public static final int GAME_STARTING = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_STOPPED = 2;
	public static final int GAME_OVER = 3;

	private static final int BONUS_POINTS_MUL = 3000;
	private static final int MALUS_POINTS_MUL = -3000;

	private static final int INIT_WINDOW_WIDTH = 1200;
	private static final int INIT_WINDOW_HEIGHT = 800;

	private static final int NUM_COLLECTABLES = 7;
	private static final int NUM_OBSTACLES = 7;

	private static final int FPS = 60;
	private static final long OPTIMAL_TIME = 1000000000 / FPS;

	public static int state;
	private long points = 0;
	private long time = 60;

	private Window window;
	private Controls controls;
	private GameObjects gameObjects;
	private Timer timer;

	class CountDown extends TimerTask
	{
		@Override
		public void run()
		{
			if (Game.state == Game.GAME_RUNNING)
			{
				--time;
				window.updateTime(time);
			}

			if (time == 0)
				gameOver();
		};
	}

	public Game()
	{
		gameObjects = new GameObjects();
		window = new Window(
			INIT_WINDOW_WIDTH,
			INIT_WINDOW_HEIGHT,
			"Slippy - The hungry penguin",
			this);
		controls = new Controls(window, this);
		controls.register();
		controls.addOnCollectListener(this);
		controls.addOnObstacleListener(this);

		initGame();

		Thread th = new Thread(this);
		th.start();
	}

	private void initGame()
	{
		Game.state = GAME_STARTING;

		points = 0;
		time = 60;

		List<ImageIcon> icons = new ArrayList<ImageIcon>();
		icons.add(new ImageIcon(getClass().getResource("/penguin.png")));
		icons.add(new ImageIcon(getClass().getResource("/penguin_happy.png")));
		icons.add(new ImageIcon(getClass().getResource("/penguin_sad.png")));

		Player player = new Player(
			INIT_WINDOW_WIDTH/2,
			INIT_WINDOW_HEIGHT/2,
			icons);
		player.setXAcc(0.01f);
		player.setYAcc(0.01f);

		gameObjects.setPlayer(player);
		addCollectables(player);
		addObstacles(player);

		timer = new Timer();
		timer.scheduleAtFixedRate(new CountDown(), 0, 1000);
	}

	private void addCollectables(Player player)
	{
		List<Collectable> colls = gameObjects.getCollectables();

		int i = colls.size();
		Collectable coll = null;

		do
		{
			int index = (int) (Math.random()*6.0f);
			ImageIcon icon = getCollectableIcon(index);

			coll = new Collectable(
				(float) (Math.random()*(
					window.getDisplay().getWidth()-icon.getIconWidth())),
				(float) (Math.random()*(
					window.getDisplay().getHeight()-icon.getIconHeight())),
				icon,
				(index+1)*BONUS_POINTS_MUL);

			if (Entity.checkColission(coll, player))
				continue;

			if (colls.size() != 0)
			{
				boolean tooClose = false;

				for (Collectable other : colls)
				{
					if (Entity.checkColission(coll, other))
					{
						tooClose = true;
						break;
					}
				}

				if (tooClose)
					continue;
			}

			if (coll != null)
				colls.add(coll);

			++i;
		}
		while (i < NUM_COLLECTABLES);
	}

	private void addObstacles(Player player)
	{
		List<Collectable> colls = gameObjects.getCollectables();
		List<Obstacle> obstacles = gameObjects.getObstacles();

		int i = obstacles.size();
		Obstacle obs = null;

		do
		{
			int index = (int) (Math.random()*5.0f);
			ImageIcon icon = getObstacleIcon(index);

			obs = new Obstacle(
				(float) (Math.random()*(
					window.getDisplay().getWidth()-icon.getIconWidth())),
				(float) (Math.random()*(
					window.getDisplay().getHeight()-icon.getIconHeight())),
				icon,
				(index+1)*(MALUS_POINTS_MUL));

			if (Entity.checkColission(obs, player))
				continue;

			if (colls.size() != 0)
			{
				boolean tooClose = false;

				for (Collectable coll : colls)
				{
					if (Entity.checkColission(obs, coll))
					{
						tooClose = true;
						break;
					}
				}

				if (tooClose)
					continue;

				for (Obstacle other : obstacles)
				{
					if (Entity.checkColission(obs, other))
					{
						tooClose = true;
						break;
					}
				}

				if (tooClose)
					continue;
			}

			if (obs != null)
				obstacles.add(obs);

			++i;
		}
		while (i < NUM_OBSTACLES);
	}

	private ImageIcon getCollectableIcon(int index)
	{
		switch (index)
		{
			case 0:
				return new ImageIcon(getClass().getResource("/fish_1.png"));
			case 1:
				return new ImageIcon(getClass().getResource("/fish_2.png"));
			case 2:
				return new ImageIcon(getClass().getResource("/fish_3.png"));
			case 3:
				return new ImageIcon(getClass().getResource("/fish_4.png"));
			case 4:
				return new ImageIcon(getClass().getResource("/fish_5.png"));
			case 5:
			default:
				return new ImageIcon(getClass().getResource("/fish_6.png"));
		}
	}

	private ImageIcon getObstacleIcon(int index)
	{
		switch (index)
		{
			case 0:
				return new ImageIcon(getClass().getResource("/bad_1.png"));
			case 1:
				return new ImageIcon(getClass().getResource("/bad_2.png"));
			case 2:
				return new ImageIcon(getClass().getResource("/bad_3.png"));
			case 3:
				return new ImageIcon(getClass().getResource("/bad_4.png"));
			case 4:
			default:
				return new ImageIcon(getClass().getResource("/bad_5.png"));
		}
	}

	public GameObjects getGameObjects()
	{
		return gameObjects;
	}

	public synchronized long getPoints()
	{
		return points;
	}

	public synchronized long getTime()
	{
		return time;
	}

	public synchronized void start()
	{
		Game.state = GAME_RUNNING;

		window.getIntro().setVisible(false);
		window.updatePoints(points);
		window.updateTime(time);
	}

	public synchronized void stop()
	{
		Game.state = GAME_STOPPED;
	}

	public synchronized void gameOver()
	{
		Game.state = GAME_OVER;

		reset();
	}

	public synchronized void reset()
	{
		gameObjects.getCollectables().clear();
		timer.cancel();

		initGame();
		window.getIntro().setVisible(true);
	}

	@Override
	public void onCollect(Collectable coll)
	{
		gameObjects.getCollectables().remove(coll);
		gameObjects.getPlayer().happy();
		addCollectables(gameObjects.getPlayer());

		points += coll.value;

		window.updatePoints(points);
	}

	@Override
	public void onObstacle(Obstacle obs)
	{
		gameObjects.getObstacles().remove(obs);
		gameObjects.getPlayer().sad();
		addObstacles(gameObjects.getPlayer());

		points += obs.value;

		window.updatePoints(points);
	}

	@Override
	public void run()
	{
		long last = System.nanoTime();
		long now;
		long diff;
		float dt;

		int fps = 0;
		int lastFpsTime = 0;

		while (true)
		{
			now = System.nanoTime();
			diff = now - last;
			last = now;

			dt = diff/((float) OPTIMAL_TIME);
/*
			lastFpsTime += diff;
			++fps;

			if (lastFpsTime >= 1000000000)
			{
				System.out.println("FPS: " + fps);
				lastFpsTime = 0;
				fps = 0;
			}
*/
			controls.processInputs(dt);
			window.update();

			doSleep((last - System.nanoTime() + OPTIMAL_TIME)/1000000);
		}
	}

	private synchronized void doSleep(long time)
	{
		if (time < 0)
			return;

		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{

		}
	}
}
