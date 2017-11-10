package ca.mcgill.ecse211.datacollection;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

/**
 * Collects data from the color sensor and stores it in a file.
 * This requires and ssh connection to a computer.
 * @author Team 2
 * @version 1.0
 * @since 1.1
 */
public class ColorSensorDataLogger {
	
	/**
	 * @param args used to specify the compiler this is a main and should run from here. 
	 * @throws InterruptedException exception necessary for this class to work.
	 * @throws FileNotFoundException if the file it is trying to write in cannot be found.
	 * @throws UnsupportedEncodingException exception necessary for this class to work. 
	 */
	/*
	public static void main(String[] args) throws InterruptedException, FileNotFoundException,
	UnsupportedEncodingException {
		PrintWriter writer = null;
		writer = new PrintWriter("csData.csv", "UTF-8");
		
		EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S3);
		float[] colorData = new float[sensor.getRedMode().sampleSize()];
		ColorPoller poller = new ColorPoller(sensor.getRedMode(), colorData);
		EV3LargeRegulatedMotor leftMotor =
			      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("C"));
		
		EV3LargeRegulatedMotor rightMotor =
			      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("B"));
		poller.start();
		leftMotor.rotate(3742, true);
		rightMotor.rotate(3742, true);
		try{
			for(int i = 0; i < 150; i++){
				int redIntensity = poller.getRedIntensity();
				
				System.out.print(String.format("%d: %d%n", System.currentTimeMillis(), redIntensity));
				writer.write(redIntensity + "\n");
			}
		}
		finally{
			writer.close();
			sensor.close();
		}
		System.exit(0);
	} */

}
