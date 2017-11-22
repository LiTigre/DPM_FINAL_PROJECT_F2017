package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;

/**
 * Class that searches the opposing zone for the flag.
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Search extends Thread {
	
	private static boolean captured = false;
	
	/**
	 * Constructor for the Search class.
	 * @param odometer Odometer created in the MainController class.
	 * @param driver Driver created in MainController.
	 * @since 1.1
	 */
	public Search() {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while(!(getCaptured())) {
			float redColor = MainController.getSearchLightValue(0);	//Red
			float greenColor = MainController.getSearchLightValue(1);	//Green
			float blueColor = MainController.getSearchLightValue(2);	//Blue
			if ( !(redColor >= 0.002 && greenColor >= 0.002 && blueColor >= 0.002)){
				if( redColor > (greenColor + blueColor)){  						// Red block or Yellow block
					if (redColor/2 > (greenColor + blueColor)){					// Red block
						Sound.beep();
						Sound.beep();
						Sound.beep();
						setCaptured(true);
					}
					else if(redColor/2 < (greenColor+blueColor)){				// Yellow block
						Sound.beep();
						Sound.beep();
						setCaptured(false);
					}
				}
				else if (redColor < (greenColor + blueColor)){					// White block or blue block
					if ( blueColor > redColor && blueColor > greenColor){		// Blue block
						Sound.beep();
						setCaptured(true);
					}
					else if ( blueColor < redColor && blueColor < greenColor){	// White block
						Sound.beep();
						Sound.beep();
						Sound.beep();
						Sound.beep();
						setCaptured(true);
					}
				}
			}
		}
	}
	
	/**
	 * Scans the block found to check whether it is the right color block or not.
	 * @since 1.1
	 */
	private void scanBlock() {
		
	}
	
	/**
	 * If the robot finds the block perform this. 
	 * @since 1.1
	 */
	private void captureBlock() {
		
	}
	
	/**
	 * Checks whether or not the robot has captured the block.
	 * @return captureStatus Returns true if the block has been captured, false otherwise. 
	 * @since 1.1
	 */
	public boolean getCaptured() {
		return captured;
	}
	

	/**
	 * Sets the value of the captured boolean. 
	 * @param captured Boolean true if the block has been captured, false otherwise. 
	 * @since 1.1
	 */
	private void setCaptured(boolean found) {
		captured = found; 
	}
	
	/**
	 * Checks whether the block being scanned is the flag. 
	 * @return Returns true if the block is the flag, false otherwise.
	 * @since 1.1
	 */
	//public boolean getBlock() {
	//	return foundBlock;
	//}
	
	/**
	 * Sets the values of the foundBlock boolean. 
	 * @param foundBlock True if the block is the flag, false otherwise. 
	 * @since 1.1
	 */
	private void setBlock(int flagColor) {
		
	}

	/**
	 * Checks if the robot is currently searching for the block. 
	 * @return True if it is searching, false otherwise.
	 * @since 1.1
	 */
	public boolean isSearching() {
		// TODO Auto-generated method stub
		return false;
	}
}
