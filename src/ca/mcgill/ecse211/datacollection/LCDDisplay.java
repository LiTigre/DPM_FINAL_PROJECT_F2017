package ca.mcgill.ecse211.datacollection;

import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.lcd.TextLCD;
import lejos.robotics.SampleProvider;

/**
 * Displays data information on the LCD display of the robot. Used for testing and debugging.
 * @author Team 2
 * @version 1.0
 * @since 1.0
 */
public class LCDDisplay {
	private static final long DISPLAY_PERIOD = 250;
	private Odometer odometer;
	private TextLCD screen;
	private SampleProvider usSensor;
	private float[] usData;

	
	public LCDDisplay(Odometer odometer, TextLCD screen, SampleProvider usSensor, float[] usData) {
		this.odometer = odometer;
		this.screen = screen;
		this.usSensor = usSensor;
		this.usData = usData;
	}

	public void run() {
		long displayStart, displayEnd;
		double[] position = new double[3];

		// clear the display once
		screen.clear();

		while (true) {
			displayStart = System.currentTimeMillis();
			// clear the lines for displaying information
			screen.clear();
			screen.drawString("X:              ", 0, 0);
			screen.drawString("Y:              ", 0, 1);
			screen.drawString("T:              ", 0, 2);
			screen.drawString("D:       		  ", 0, 3);

			// get the odometry information
			odometer.getPosition(position, new boolean[] { true, true, true });

			// display odometry information
			for (int i = 0; i < 3; i++) {
				screen.drawString(formattedDoubleToString(position[i], 2), 3, i);
			}
			// display distance information
			screen.drawString(formattedDoubleToString(getDistanceValue(), 2), 3, 3);

			// throttle the LCDDisplay
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {

				}
			}
		}
	}

	private double getDistanceValue() {
		usSensor.fetchSample(usData, 0);
		return usData[0] * 100;
	}

	private static String formattedDoubleToString(double x, int places) {
		String result = "";
		String stack = "";
		long t;
		// put in a minus sign as needed
		if (x < 0.0)
			result += "-";
		// put in a leading 0
		if (-1.0 < x && x < 1.0)
			result += "0";
		else {
			t = (long) x;
			if (t < 0)
				t = -t;
			while (t > 0) {
				stack = Long.toString(t % 10) + stack;
				t /= 10;
			}
			result += stack;
		}
		// put the decimal, if needed
		if (places > 0) {
			result += ".";
			// put the appropriate number of decimals
			for (int i = 0; i < places; i++) {
				x = Math.abs(x);
				x = x - Math.floor(x);
				x *= 10.0;
				result += Long.toString((long) x);
			}
		}
		return result;
	}

}
