package de.schroedel.slippy.entities;

import de.schroedel.slippy.entities.Entity;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import javax.swing.ImageIcon;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class Player extends Entity
{
	private final static int ICON_NORMAL = 0;
	private final static int ICON_HAPPY = 1;
	private final static int ICON_SAD = 2;

	private final static int STATE_NORMAL = 0;
	private final static int STATE_HAPPY = 1;
	private final static int STATE_SAD = 2;

	public float bounce;

	public float xVel;
	public float yVel;

	private float xVelMin;
	private float xVelMax;
	private float yVelMin;
	private float yVelMax;

	private float xAcc;
	private float yAcc;

	private List<ImageIcon> icons;

	private int state;
	private Timer timer;

	class Animation extends TimerTask
	{
		private int i = 0;

		@Override
		public void run()
		{
			ImageIcon normal = icons.get(ICON_NORMAL);
			ImageIcon happy = icons.get(ICON_HAPPY);
			ImageIcon sad = icons.get(ICON_SAD);

			if (state == Player.STATE_NORMAL)
				return;

			if (state == Player.STATE_HAPPY &&
				icon != happy)
			{
				icon = happy;
				++i;
			}
			else if (state == Player.STATE_HAPPY &&
				icon == happy)
			{
				icon = normal;
				++i;
			}

			if (state == Player.STATE_SAD &&
				icon != sad)
			{
				icon = sad;
				++i;
			}
			else if (state == Player.STATE_SAD &&
				icon == sad)
			{
				icon = normal;
				++i;
			}

			if (i == 6)
			{
				state = Player.STATE_NORMAL;
				icon = normal;
				i = 0;
			}
		};
	}

	public Player(float x, float y, List<ImageIcon> icons)
	{
		super(x, y, icons.get(ICON_NORMAL));

		this.icons = icons;

		bounce = 0.5f;
		xVel = 0;
		xVelMin = -6;
		xVelMax = 6;
		yVel = 0;
		yVelMin = -6;
		yVelMax = 6;

		timer = new Timer();
		timer.scheduleAtFixedRate(new Animation(), 0, 500);
	}

	public void happy()
	{
		icon = icons.get(ICON_HAPPY);
		state = STATE_HAPPY;

		playSound(getClass().getResource("/Collect_good.wav"));
	}

	public void sad()
	{
		icon = icons.get(ICON_SAD);
		state = STATE_SAD;

		playSound(getClass().getResource("/Collect_bad.wav"));
	}

	public void playSound(final URL sound)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				AudioInputStream stream;

				try
				{
					stream = AudioSystem.getAudioInputStream(sound);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}

				SourceDataLine sourceLine = null;
				AudioFormat audioFormat = stream.getFormat();

				DataLine.Info info = new DataLine.Info(
					SourceDataLine.class,
					audioFormat);

				try
				{
					sourceLine = (SourceDataLine) AudioSystem.getLine(info);
					sourceLine.open(audioFormat);
				}
				catch (LineUnavailableException e)
				{
					e.printStackTrace();
					System.exit(1);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}

				if (sourceLine == null)
					return;

				sourceLine.start();

				int nBytesRead = 0;
				byte[] abData = new byte[128000];

				while (nBytesRead != -1)
				{
					try
					{
						nBytesRead = stream.read(abData, 0, abData.length);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (nBytesRead >= 0)
					{
						@SuppressWarnings("unused")
						int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
					}
				}

				sourceLine.drain();
				sourceLine.close();
			}
		}.start();
	}

	public void moveUp()
	{
		yAcc = -0.1f;
	}

	public void moveLeft()
	{
		xAcc = -0.1f;
	}

	public void moveDown()
	{
		yAcc = 0.1f;
	}

	public void moveRight()
	{
		xAcc = 0.1f;
	}

	public void move(float dt)
	{
		if (dt > 10)
			return;

		float dxVel = xAcc * dt;
		float dyVel = yAcc * dt;

		if (dxVel + xVel < xVelMax)
			xVel += dxVel;

		if (dyVel + yVel < yVelMax)
			yVel += dyVel;

		x += xVel * dt;
		y += yVel * dt;

		if (xVel >= 1.0f)
			xAcc -= 0.005f;
		else if (xVel < -1.0f)
			xAcc += 0.005f;
		else
			xAcc = 0;

		if (yVel >= 1.0f)
			yAcc -= 0.005f;
		else if (yVel < -1.0f)
			yAcc += 0.005f;
		else
			yAcc = 0;
	}

	public synchronized void setXAcc(float xAcc)
	{
		this.xAcc = xAcc;
	}

	public synchronized void setYAcc(float yAcc)
	{
		this.yAcc = yAcc;
	}
}
