package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
  private static final double arcadeForwardScale = 0.95;
  private static final double arcadeRotationScale = 0.65;
  private static final double tankScale = 0.8;
  private static final double minimumEncoderDifference = 0.5;
  private static final double beginScalingDifference = 5;
  private static final double minimumCommand = 0.2;
  private static final double maximumCommand = 0.75;

  public static double savedLeftPosition = 0;
  public static double savedRightPosition = 0;

  public static boolean kReverseFlag = false;
  public static boolean kTankFlag = false;

  private CANSparkMax motorLB = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax motorLF = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax motorRF = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax motorRB = new CANSparkMax(4, MotorType.kBrushless);
  private MotorControllerGroup leftMotors = new MotorControllerGroup(motorLB, motorLF);
  private MotorControllerGroup rightMotors = new MotorControllerGroup(motorRB, motorRF);
  public DifferentialDrive diffDrive = new DifferentialDrive(leftMotors, rightMotors);

  private RelativeEncoder encoderLB = motorLB.getEncoder();
  private RelativeEncoder encoderLF = motorLF.getEncoder();
  private RelativeEncoder encoderRF = motorRF.getEncoder();
  private RelativeEncoder encoderRB = motorRB.getEncoder();
  
  /** Construct a custom Drivetrain object. */
  public Drivetrain() {
    motorLB.setInverted(true);
    motorLF.setInverted(true);
    setDriveIdle(IdleMode.kBrake);
  }

  /**
   * @return The average position of the left drive motor encoders.
   */
  public double getLeftPosition() {
    return (encoderLB.getPosition() + encoderLF.getPosition())/2;
  }

  /**
   * @return The average position of the right drive motor encoders.
   */
  public double getRightPosition() {
    return (encoderRB.getPosition() + encoderRF.getPosition())/2;
  }

  /** Sets every drive motor encoder position to 0. */
  public void zeroDriveEncoders() {
    encoderLB.setPosition(0);
    encoderLF.setPosition(0);
    encoderRF.setPosition(0);
    encoderRB.setPosition(0);
  }

  /** Calls the arcadeDrive method of the internal {@link DifferentialDrive} object.
   * @param forward Robot's speed along the X axis. Forward is positive.
   * @param rotation Robot's rotation speed around the Z axis. Counterclockwise is positive.
   */
  public void arcade(double forward, double rotation) {
    forward *= arcadeForwardScale;
    rotation *= arcadeRotationScale;
    if (kReverseFlag) forward *= -1;
    diffDrive.arcadeDrive(forward, rotation);
  }
  
  /** Calls the arcadeDrive method of the internal {@link DifferentialDrive} object.
   * @param vals A double array containing the forward and rotation values to be passed on to arcadeDrive method of the {@link DifferentialDrive} object.
  */
  public void arcade(double[] vals) {
    arcade(vals[0], vals[1]);
  }

  /** Calls the tankDrive method of the internal {@link DifferentialDrive} object.
   * @param left Robot's left side speed along the X axis. Forward is positive.
   * @param right Robot's right side speed along the X axis. Forward is positive.
   */
  public void tank(double left, double right) {
    left *= tankScale;
    right *= tankScale;
    if (!kReverseFlag) diffDrive.tankDrive(left, right);
    else diffDrive.tankDrive(-right, -left);
  }

  /** Calls the tankDrive method of the internal {@link DifferentialDrive} object.
   * @param vals A double array containing the left and right values to be passed on to tankDrive method of the {@link DifferentialDrive} object.
  */
  public void tank(double[] vals) {
    tank(vals[0], vals[1]);
  }

  /** Sets the {@link IdleMode} of all drive motors.
   * @param mode The mode to set the motors to, either coast or brake.
   */
  public void setDriveIdle(IdleMode mode) {
    motorLB.setIdleMode(mode);
    motorLF.setIdleMode(mode);
    motorRF.setIdleMode(mode);
    motorRB.setIdleMode(mode);
  }

  /**
   * @return The {@link IdleMode} that the drive motors are currently set to.
   */
  public IdleMode getDriveIdle() {
    return motorLB.getIdleMode();
  }

  /** Toggles the {@link IdleMode} of the drive motors between coast and brake. */
  public void toggleDriveIdle() {
    setDriveIdle(motorLB.getIdleMode() == IdleMode.kBrake ? IdleMode.kCoast : IdleMode.kBrake);
  }

  /** Toggles the kTankFlag between true and false. */
  public void toggleTankFlag() {
    kTankFlag = kTankFlag ? false : true;
  }

  /** Toggles the kReverseFlag between true and false. */
  public void toggleReverseFlag() {
    kReverseFlag = kReverseFlag ? false : true;
  }

  /** Sets both the kTankFlag and kReverseFlag to false. */
  public void defaultFlags() {
    kTankFlag = false;
    kReverseFlag = false;
  }

  /**
   * Moves the robot's left and right tracks to obtain the desired left and right encoder positions.
   * @param left The encoder position for the left track to drive to.
   * @param right The encoder position for the right track to drive to.
   */
  public void moveTracksTo(double left, double right) {
    moveTrackTo(left, getLeftPosition(), leftMotors);
    moveTrackTo(right, getRightPosition(), rightMotors);
  }

  /**
   * A method that scales a double from range [a,b] to range [c,d].
   * @param x The double to be scaled.
   * @param a The lower bound of the first range.
   * @param b The upper bound of the first range.
   * @param c The lower bound of the second range.
   * @param d The upper bound of the second range.
   * @return The input double mapped onto the range [c,d].
   */
  public double map(double x, double a, double b, double c, double d) {
    return (x-a)/(b-a)*(d-c)+c;
  }

  /**
   * A method that returns a scaled speed for the tracks when automatically targetting an encoder position.
   * This is used so as to not overshoot the desired encoder position as the tracks slow down as they nears their targets.
   * @param difference The difference in the target encoder position and the track's current encoder position.
   * @return A scaled speed that is to be passed onto the track's motors.
   */
  public double scaleTempSpeed(double difference) {
    double scaled = map(Math.abs(difference), minimumEncoderDifference, beginScalingDifference-minimumEncoderDifference, minimumCommand, maximumCommand);
    return Math.copySign(scaled, Math.signum(difference));
  }

  /**
   * A method for moving a track to a desired encoder position.
   * @param target The target encoder position to move the track to.
   * @param currPos The current encoder position of the track to be moved.
   * @param motors The group of motors that make up the track, either the left track motors or the right track motors.
   */
  public void moveTrackTo(double target, double currPos, MotorControllerGroup motors) {
    double diff = target - currPos;
    double abs = Math.abs(diff);
    if (abs < minimumEncoderDifference) {
      motors.stopMotor();
    } else {
      if (diff != 0) motors.set((abs < beginScalingDifference) ? scaleTempSpeed(diff) : Math.signum(diff));
      else motors.stopMotor();
    }
  }

  /** Saves the current robot drive motor encoder positions to later be used when maintaining the robot's position. */
  public void saveCurrentRobotPosition() {
    savedLeftPosition = getLeftPosition();
    savedRightPosition = getRightPosition();
  }

  /**
   * Method that, when called, will attempt to keep the robot at the saved drive motor encoder positions.
   * This is useful when balancing as the {@link IdleMode} of the drive encoders, when set to kBrake,
   * is not strong enough to keep the robot stationary should the balancing station be tilted.
   */
  public void maintainRobotPosition() {
    moveTracksTo(getLeftPosition(), getRightPosition());
  }

  /** Wrapper method to call stopMotor on the internal {@link DifferentialDrive} object. */
  public void stopMotor() {
    diffDrive.stopMotor();
  }
}