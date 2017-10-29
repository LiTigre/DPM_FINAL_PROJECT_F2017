package ca.mcgill.ecse211.datacollection;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * Collects data from the ultrasonic sensor and stores it in a file.
 * This requires and ssh connection to a computer.
 * @author Team 2
 * @version 1.0
 * @since 1.1
 */
public class UltrasonicSensorDataLogger {

	/**
	 * @param args used to specify the compiler this is a main and should run from here. 
	 * @throws InterruptedException exception necessary for this class to work.
	 * @throws FileNotFoundException if the file it is trying to write in cannot be found.
	 * @throws UnsupportedEncodingException exception necessary for this class to work. 
	 */
	public static void main(String[] args) throws InterruptedException,
	FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = null;
		writer = new PrintWriter("data1.csv", "UTF-8");
		
		EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(SensorPort.S4);
		float[] usData = new float[sensor.getDistanceMode().sampleSize()];
		UltrasonicPoller poller = new UltrasonicPoller(sensor.getDistanceMode(), usData);
		EV3LargeRegulatedMotor leftMotor =
			      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
		
		EV3LargeRegulatedMotor rightMotor =
			      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
		leftMotor.rotate(-4989, true);
		rightMotor.rotate(-4989, true);
		poller.start();
		try{
			for(int i = 0; i < 150; i++){
				int distance = poller.getDistance();
				
				System.out.print(String.format("%d: %d%n", System.currentTimeMillis(), distance));
				writer.write(distance + "\n");
				Thread.sleep(100);
			}
		}
		finally{
			writer.close();
			sensor.close();
		}
		System.exit(0);

	}

}
