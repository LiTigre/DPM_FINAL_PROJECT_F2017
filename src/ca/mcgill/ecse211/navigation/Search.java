package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.odometry.Odometer;
import lejos.robotics.SampleProvider;

/**
 * Class that searches the opposing zone for the flag.
 * @author Team 2
 * @version 1.1
 * @since 1.0
 */
public class Search implements Runnable {

	/**
	 * Constructor for the Search class.
	 * @param odometer Odometer created in the MainController class.
	 * @param driver Driver created in MainController.
	 * @since 1.1
	 */
	public Search(Odometer odometer, Driver driver) {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
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
	//public boolean isCaptured() {
	//	return captureStatus;
//	}
	

	/**
	 * Sets the value of the captured boolean. 
	 * @param captured Boolean true if the block has been captured, false otherwise. 
	 * @since 1.1
	 */
	private void setCaptured(boolean captured) {
		
	}
	
	/**
	 * Checks whether the block being scanned is the flag. 
	 * @return Returns true if the block is the flag, false otherwise.
	 * @since 1.1
	 */
	//public boolean isBlock() {
	//	return foundBlock;
	//}
	
	/**
	 * Sets the values of the foundBlock boolean. 
	 * @param foundBlock True if the block is the flag, false otherwise. 
	 * @since 1.1
	 */
	private void setFoundBlock(boolean foundBlock) {
		
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
