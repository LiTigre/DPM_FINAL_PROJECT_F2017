package ca.mcgill.ecse211.navigation;

import java.util.Timer;
import java.util.TimerTask;

import ca.mcgill.ecse211.controller.MainController;
import ca.mcgill.ecse211.odometry.LightCorrection;
import ca.mcgill.ecse211.odometry.Odometer;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Main navigation class of the system. Contains all the basic travel methods necessary. 
 * @author Team 2
 * @version 1.5
 * @since 1.0
 */
public class Driver {
	
	// Objects 
	/** Odometer object created in the main controller. */
	private final Odometer odometer;
	/** Left motor object created in the main controller. */
	private EV3LargeRegulatedMotor leftMotor;
	/** Right motor object created in the main controller. */
	private EV3LargeRegulatedMotor rightMotor;
	
	
	// Constants
	/** @see MainController#WHEEL_RADIUS */
	private static final double WHEEL_RADIUS = MainController.WHEEL_RADIUS;
	/** @see MainController#TRACK */
	public double TRACK = MainController.TRACK;
	/** The forward speed of the robot in (degrees/second) */
	public final int FORWARD_SPEED = 175;
	/** The rotational speed of the robot in (degrees/second) */
	public final int ROTATE_SPEED = 100;
	
	
	// Booleans
	/** Boolean that indicates whether the robot is currently traveling. */
	private boolean travelling;
	/** Boolean that indicates whether the robot is currently turning. */
	private volatile boolean turning;
	
	public static volatile double destinationX;
	public static volatile double destinationY;
	
	
	/**
	 * Constructor for driver class.
	 * @param leftMotor Left wheel's motor created in the MainController class. 
	 * @param rightMotor Right wheel's motor created in the MainController class. 
	 * @param odometer Odometer created in the MainController class.
	 * @since 1.1
	 */
	public Driver(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Odometer odometer) {
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
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
		
		LightCorrection.doCorrection = true;
		
		Driver.destinationX = newX;
		Driver.destinationY = newY;
		
		double deltaY = newY - odometer.getY();
		double deltaX = newX - odometer.getX();

		double thetaD = Math.toDegrees(Math.atan2(deltaX, deltaY));
		double thetaTurn = thetaD - odometer.getTheta();
		
		if (thetaTurn < -180.0) {
			turnDistanceSynchronous(360.0 + thetaTurn);
		}
		else if (thetaTurn > 180.0) {
			turnDistanceSynchronous(thetaTurn - 360.0);
		}
		else {
			if (thetaTurn < 0) {
				turnDistanceSynchronous(thetaTurn + 360);
			}
			else {
				turnDistanceSynchronous(thetaTurn);
			}
		}
		
		while(getWheelsMoving());
		
		setSpeed(FORWARD_SPEED);
		
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		
		setTravelling(false);
	}
	
	/**
	 * Travels to a point specified while only moving straight
	 * @param newX X position of the new point in centimeters.
	 * @param newY Y position of the new point in centimeters.
	 * @since 1.1
	 */
	public void travelToStraight(double newX, double newY) {
		
		Driver.destinationX = newX;
		Driver.destinationY = newY;
		
		double deltaY = newY - odometer.getY();
		double deltaX = newX - odometer.getX();
		
		setSpeed(FORWARD_SPEED);
		
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		
		setTravelling(true);
		
		leftMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(WHEEL_RADIUS, distance), true);
		
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
	 * Makes both wheels go forward for unspecified distance or time. 
	 * @since 1.5
	 */
	public void forward() {
		setSpeed(FORWARD_SPEED);
		
		leftMotor.forward();
		rightMotor.forward();
	}
	
	/**
	 * Makes both wheels go backward for unspecified distance or time. 
	 * @since 1.5
	 */
	public void backward() {
		setSpeed(FORWARD_SPEED);
		
		leftMotor.backward();
		rightMotor.backward();
	}
	
	/**
	 * Makes both wheels go forward for unspecified distance or time. 
	 * @param speed The speed of the wheels in degrees per second. 
	 * @since 1.4
	 */
	public void forward(int speed) {
		setSpeed(speed);
		
		leftMotor.forward();
		rightMotor.forward();
	}
	
	/**
	 * Turn a specified amount. 
	 * @param angle Angle amount the robots needs to turn in degrees.
	 * @since 1.5
	 */
	public void turnDistanceSynchronous(double angle) {
		setTurning(true);
		
		setSpeed(ROTATE_SPEED);
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				LightCorrection.doCorrection = true;
			}
		}, 2 * 1000);
		
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, angle), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, angle), false);
		setTurning(false);
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
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, angle), true);
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
			turnDistanceSynchronous(360.0 + thetaTurn);

		}
		else if (thetaTurn > 180.0) {
			turnDistanceSynchronous(thetaTurn - 360.0);
		}
		else {
			turnDistanceSynchronous(thetaTurn);
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
		setTravelling(false);
		setTurning(false);
	}
	
	/**
	 * Force stop of the both motors. 
	 * @since 1.5
	 */
	public void instantStopAsync() {
		leftMotor.stop(true);
		rightMotor.stop(true);
		setTravelling(false);
		setTurning(false);
	}
	
	/**
	 * This makes setting the speed of both wheels easier.
	 * @param speed Speed to set both wheels to.
	 * @since 1.3
	 */
	public void setSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}
	
	/**
	 * Getter to check if the robot is currently moving. 
	 * @return True if both wheels are moving, false otherwise.
	 * @since 1.4
	 */
	public boolean getWheelsMoving() {
		return (leftMotor.isMoving() && rightMotor.isMoving());
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
		this.travelling = travel;
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
	 * @since 1.2
	 */
	private void setTurning(boolean turn) {
		this.turning = turn;
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
