import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CountryMap extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2160867947103604406L;
	public City[] cities;
	public ArrayList<Color> city_colors;
	
	public int PAD = 50;
	public int MAX_X = 150;
	public int MAX_Y = 150;

	public CountryMap(City[] cityList, String name){
		super("The Map of the Amazing Country of " + name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Map map = new Map();
		add(map);
		setSize(600, 600);
		setVisible(true);
		cities = cityList;
		city_colors = new ArrayList<Color>();		
        for (int j = 0; j < cities.length; j++) {
            Color c = new Color((j*101)%200, (j*73)%200, (j*67)%200, 220);
            city_colors.add(c);
        }
	}

	public class Map extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setColor(Color.black);
			g.setFont(new Font("default", Font.PLAIN, 14));
			
            int w = getWidth();
            int h = getHeight();
            int pointSize = Math.max(Math.min(w, h)/60,10);

            double scale_x = (double)(w - 2*PAD)/MAX_X;
            double scale_y = (double)(h - 2*PAD)/MAX_Y;
            
			double xInc = (double) (w - 2 * PAD) / (MAX_X - 1);
			double scale = (double) (h - 2 * PAD) / MAX_Y;
			// Draw abcissa.
			int tickInc = MAX_X / 10;
			for (int i = 0; i <= MAX_X; i += tickInc) {
				int x = PAD + (int) (i * xInc);
				int y = h - PAD;
				g.drawString(Integer.toString(i), x - 5, y + 20);
				g2.draw(new Line2D.Double(x, y - 5, x, y + 5));
			}
			g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD / 2, h - PAD));
			AffineTransform orig = g2.getTransform();
			g2.rotate(-Math.PI / 2);
			g2.setColor(Color.black);
			g2.drawString("Latitude", -((h + PAD) / 2), PAD / 3);
			g2.setTransform(orig);

			// Draw ordinate.
			tickInc = MAX_Y / 10;
			
			for (int i = tickInc; i < h - PAD; i += tickInc) {
				int x = PAD;
				int closest_10 = (int)(Math.round((i / scale) / 10) * 10);

				int y = h - PAD - (int) (closest_10 * scale);
				if (y < PAD)
					break;
				String tickMark = Integer.toString(closest_10);
				int stringLen = (int) g2.getFontMetrics()
						.getStringBounds(tickMark, g2).getWidth();
				g.drawString(tickMark, x - stringLen - 8, y + 5);
				g2.draw(new Line2D.Double(x - 5, y, x + 5, y));
			}
			g2.draw(new Line2D.Double(PAD, PAD / 2, PAD, h - PAD));
			g.drawString("Longitude", (w - PAD) / 2, h - PAD + 40);
			
			for(int i =0; i<cities.length; i++){
				City city = cities[i];
				int stringLen = (int) g2.getFontMetrics()
						.getStringBounds(city.getName(), g2).getWidth();
				int posX = PAD + (int)((city.getPos().getX())*scale_x);
				int posY = h - PAD - (int)(city.getPos().getY()*scale_y);
				
				// draw links to neighbors
				Color c = new Color(30, 30, 30, 60);
				g2.setColor(c);
				for(City voisin: city.getNeighbours()){
					if(voisin!=null){
						int vPosX = PAD + (int)((voisin.getPos().getX())*scale_x);
						int vPosY = h - PAD - (int)(voisin.getPos().getY()*scale_y);
						g2.drawLine(posX, posY, vPosX, vPosY);
					}
				}
				
				// draw city
				g2.setColor(city_colors.get(i));
				g2.fill(new Ellipse2D.Double(posX-pointSize/2, posY-pointSize/2, pointSize, pointSize));
				
				// draw city name
				g.setFont(new Font("default", Font.BOLD, 14));
				if(posX - stringLen > PAD){
					posX -= stringLen;
				}
				g.drawString(city.getName(), posX, posY - 10);
			}
		}
	}
}
