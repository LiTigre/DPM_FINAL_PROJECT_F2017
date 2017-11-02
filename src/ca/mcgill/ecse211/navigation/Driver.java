package ca.mcgill.ecse211.navigation;

import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Main navigation class of the system. Contains all the basic travel methods necessary. 
 * @author Team 2
 * @version 1.3
 * @since 1.0
 */
public class Driver {
	
	private Odometer odometer;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;
	
	private double WHEEL_RADIUS;
	private double TRACK;
	private int FORWARD_SPEED;
	private int ROTATE_SPEED;
	
	private boolean travelling;
	private boolean turning;
	
	/**
	 * Constructor for driver class.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @param WHEEL_RADIUS Wheels' radii set in the MainController class.
	 * @param TRACK Track size set in MainController class.
	 * @param odometer Odometer created in the MainController class.
	 * @since 1.1
	 */
	public Driver(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Odometer odometer, 
					double WHEEL_RADIUS, double TRACK, int FORWARD_SPEED, int ROTATE_SPEED) {
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
		this.WHEEL_RADIUS = WHEEL_RADIUS;
		this.TRACK = TRACK;
		this.FORWARD_SPEED = FORWARD_SPEED;
		this.ROTATE_SPEED = ROTATE_SPEED;
		
		this.travelling = false;
		this.turning = false;
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
			turnDistance(360.0 + thetaTurn);
		}
		else if (thetaTurn > 180.0) {
			turnDistance(thetaTurn - 360.0);
		}
		else {
			turnDistance(thetaTurn);
		}
		
		setSpeed(FORWARD_SPEED);
		
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(WHEEL_RADIUS, distance), false);
		
		setTravelling(false);
	}
	
	/**
	 * Travels a certain distance specified. 
	 * @param distance Distance the robot needs to travel in centimeters.
	 * @since 1.1
	 */
	public void travelDistance(double distance) {
		setSpeed(FORWARD_SPEED);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(WHEEL_RADIUS, distance), false);
		
		setTravelling(false);
	}
	
	/**
	 * Turn a specified amount. 
	 * @param angle Angle amount the robots needs to turn in degrees.
	 * @since 1.1
	 */
	public void turnDistance(double angle) {
		setTurning(true);
		
		setSpeed(ROTATE_SPEED);
		
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, angle), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, angle), false);
		
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
			turnDistance(360.0 + thetaTurn);

		}
		else if (thetaTurn > 180.0) {
			turnDistance(thetaTurn - 360.0);
		}
		else {
			turnDistance(thetaTurn);
		}
		
	}
	
	/**
	 * Constant rotation of the wheels. This makes the robot turn on itself indefinitely. 
	 * @since 1.3
	 */
	public void rotate() {
		setSpeed(ROTATE_SPEED);
		
		leftMotor.forward();
		rightMotor.backward();
	}
	
	/**
	 * Force stop of the both motors. 
	 * @since 1.3
	 */
	public void instantStop() {
		leftMotor.stop();
		rightMotor.stop();
	}
	
	/**
	 * This makes setting the speed of both wheels easier.
	 * @param speed Speed to set both wheels to.
	 * @since 1.3
	 */
	private void setSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
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
	private static int convertAngle(double radius, double track, double angle) {
		return convertDistance(radius, Math.PI * track * angle / 360.0);
	}
}
