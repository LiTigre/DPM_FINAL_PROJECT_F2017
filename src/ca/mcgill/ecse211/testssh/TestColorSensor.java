package ca.mcgill.ecse211.testssh;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class TestColorSensor {
	
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
	}

}
