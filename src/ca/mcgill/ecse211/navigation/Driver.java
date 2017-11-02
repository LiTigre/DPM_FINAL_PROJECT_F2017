package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Main navigation class of the system. Contains all the basic travel methods necessary. 
 * @author Team 2
 * @version 1.2
 * @since 1.0
 */
public class Driver {
	
	/**
	 * Constructor for driver class.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @param WHEEL_RADIUS Wheels' radii set in the MainController class.
	 * @param TRACK Track size set in MainController class.
	 * @param odometer Odometer created in the MainController class.
	 * @since 1.1
	 */
	public Driver(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, double WHEEL_RADIUS,
				double TRACK, Odometer odometer) {
		
	}
	
	/**
	 * Travels to a point specified. 
	 * @param newX X position of the new point in centimeters.
	 * @param newY Y position of the new point in centimeters.
	 * @since 1.1
	 */
	public void travelTo(double newX, double newY) {
		
		double deltaY = newY - odometer.getY();
		double deltaX = newX - odometer.getX();

		double thetaD = Math.toDegrees(Math.atan2(deltaX, deltaY));
		double thetaTurn = thetaD - odometer.getTheta();
		
		if (thetaTurn < -180.0) {
			turnTo(360.0 + thetaTurn);
		}
		else if (thetaTurn > 180.0) {
			turnTo(thetaTurn - 360.0);
		}
		else {
			turnTo(thetaTurn);
		}
		
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(radius, distance), true);
		rightMotor.rotate(convertDistance(radius, distance), false);
		
		setTravelling(false);
	}
	
	/**
	 * Travels a certain distance specified. 
	 * @param distance Distance the robot needs to travel in centimeters.
	 * @since 1.1
	 */
	public void travelDistance(double distance) {
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(radius, distance), true);
		rightMotor.rotate(convertDistance(radius, distance), false);
		
		setTravelling(false);
	}
	
	/**
	 * Turn a specified amount. 
	 * @param angle Angle amount the robots needs to turn in degrees.
	 * @since 1.1
	 */
	public void turnDistance(double angle) {
		setTurning(true);
		
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		leftMotor.rotate(convertAngle(radius, width, theta), true);
		rightMotor.rotate(-convertAngle(radius, width, theta), true);
		
		setTurning(false);
	}
	
	/**
	 * Turns towards a point specified. 
	 * @param newX X position of the point (cm)
	 * @param newY Y position of the point (cm)
	 * @since 1.1
	 */
	public void turnTo(double newX, double newY) {
		
		double deltaY = newY - odometer.getY();
		double deltaX = newX - odometer.getX();

		double thetaD = Math.toDegrees(Math.atan2(deltaX, deltaY));
		double thetaTurn = thetaD - odometer.getTheta();
		
		if (thetaTurn < -180.0) {
			turnTo(360.0 + thetaTurn);

		}
		else if (thetaTurn > 180.0) {
			turnTo(thetaTurn - 360.0);
		}
		else {
			turnTo(thetaTurn);
		}
		
	}
	
	/**
	 * Checks if the robot is currently traveling. 
	 * @return boolean If it is traveling then it's true, otherwise false. 
	 * @since 1.1
	 */
	public boolean isTraveling() {
		return this.travelling;
	}
	
	/**
	 * Sets the traveling boolean. 
	 * @param travel True when it is traveling, false otherwise. 
	 * @since 1.1
	 */
	private void setTravelling(boolean travel) {
		travelling = travel;
	}
	
	/**
	 * Checks if the robot is currently turning.
	 * @return boolean If it is turning then it's true, otherwise false.
	 * @since 1.2 
	 */
	public boolean isTurning() {
		return this.turning;
	}
	
	/**
	 * Sets the turning boolean.
	 * @param turn True when it is turning, false otherwise.
	 */
	private void setTurning(boolean turn) {
		turning = turn;
	}
	
	/**
	 * Converts a distance specified into an angle.
	 * @param radius Radii of the wheels in cm. 
	 * @param distance Distance in cm.
	 * @return angle in radiant.
	 * @since 1.1
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	/**
	 * Converts an angle specified into the proper angle. 
	 * @param radius Radii of the wheels in cm.
	 * @param TRACK Track of the robot in cm. 
	 * @param angle Angle that will be converted in radiant.
	 * @return angle in radiant.
	 * @since 1.1 
	 */
	private static int convertAngle(double radius, double TRACK, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
