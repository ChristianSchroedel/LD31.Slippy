package de.schroedel.slippy;

import de.schroedel.slippy.Display;
import de.schroedel.slippy.Game;

import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import javax.swing.OverlayLayout;

public class Window extends JFrame
{
	private Display display;
	private ImagePanel intro;

	private JLabel lblPoints;
	private JLabel lblTime;

	class ImagePanel extends JPanel
	{
		private Image image;
		private int x;
		private int y;

		public ImagePanel(Image image, int x, int y)
		{
			this.image = image;
			this.x = x;
			this.y = y;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.drawImage(image, x, y, this);
		}
	}

	public Window(int width, int height, String title, Game game)
	{
		display = new Display(game);
		display.setOpaque(false);

		ImagePanel background = new ImagePanel(new ImageIcon(
			getClass().getResource("/bg.png")).getImage(),
			0,
			0);
		background.setOpaque(false);

		intro = new ImagePanel(
			new ImageIcon(getClass().getResource("/title.png")).getImage(),
			width/2-507/2,
			height/2-229/2);
		intro.setOpaque(false);

		Container displayContainer = new Container();
		displayContainer.setLayout(new OverlayLayout(displayContainer));
		displayContainer.add(intro);
		displayContainer.add(display);
		displayContainer.add(background);

		lblPoints = new JLabel("Points " + game.getPoints());
		lblPoints.setFont(new Font("", Font.BOLD, 28));

		lblTime = new JLabel("Time " + game.getTime());
		lblTime.setFont(new Font("", Font.BOLD, 28));

		Container pointsContainer = new Container();
		pointsContainer.setLayout(new BorderLayout());
		pointsContainer.add(lblPoints, BorderLayout.WEST);
		pointsContainer.add(lblTime, BorderLayout.EAST);

		Container mainWindow = this.getContentPane();
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setLayout(new BorderLayout());
		mainWindow.add(displayContainer, BorderLayout.CENTER);
		mainWindow.add(pointsContainer, BorderLayout.SOUTH);

		this.setSize(width, height);
		this.setResizable(false);
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void update()
	{
		display.repaint();
	}

	public Display getDisplay()
	{
		return display;
	}

	public ImagePanel getIntro()
	{
		return intro;
	}

	public void updatePoints(long points)
	{
		lblPoints.setText("Points " + points);
	}

	public void updateTime(long time)
	{
		lblTime.setText("Time " + time);
	}
}
