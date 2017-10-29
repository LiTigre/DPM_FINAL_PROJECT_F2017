package ca.mcgill.ecse211.testssh;

import lejos.robotics.SampleProvider;

public class ColorPoller extends Thread {
  private SampleProvider cs;
  private float[] colorData;
  private int redIntensity;


  public ColorPoller(SampleProvider cs, float[] colorData) {
    this.cs = cs;
    this.colorData = colorData;
  }

  /*
   * Sensors now return floats using a uniform protocol. Need to convert US result to an integer
   * [0,255] (non-Javadoc)
   * 
   * @see java.lang.Thread#run()
   */
  public void run() {
	  while (true){
      cs.fetchSample(colorData, 0); // acquire data
      redIntensity = (int) (colorData[0] * 100.0); // extract from buffer, cast to int
      
	  }

    }
  public int getRedIntensity(){
	  return this.redIntensity;
  }
  }


