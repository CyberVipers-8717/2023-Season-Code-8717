package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/*
 * Structure of Drivetrain class
 * variable declarations
 * constructor
 * encoders
 *    get left average
 *    get right average
 *    zero positions
 * arcade
 * tank
 * driveIdle
 *    set
 *    get
 *    toggle
 * toggle tankFlag
 * toggle reverseFlag
 * default settings
 *    tankFlag = false
 *    reverseFlag = false
 * special auto stuff
 *    move wheels to specific encoder positions
 * stopMotors
 */

public class Drivetrain {
  private static final double arcadeForwardScale = 0.85;
  private static final double arcadeRotationScale = 0.5;
  private static final double tankScale = 0.8;

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

  //to eventually write correctly

  // public void moveWheelsTo(double left, double right) {
  //     moveLeftWheelTo(left);
  //     moveRightWheelTo(right);
  // }

  // public void moveLeftWheelTo(double target) {
  //     double diff = target - getLeft(); 
  //     double sign = Math.signum(diff);
  //     double absDiff = Math.abs(diff);
  //     double minDiff = 0.5;
  //     double scaleDiff = 5;
  //     double adjusted = scaleDiff - minDiff;
  //     double lowest = 0.2;
  //     double temp = 1;
  //     if (absDiff < minDiff) {
  //         leftMotors.stopMotor();
  //     } else {
  //         if (absDiff < scaleDiff) {
  //             temp = map(absDiff, minDiff, adjusted, lowest, 1);
  //         }
  //         if (sign == -1) {
  //             leftMotors.set(0.175*temp);
  //         } else if (sign == 1) {
  //             leftMotors.set(-0.175*temp);
  //         } else {
  //             leftMotors.stopMotor();
  //         }
  //     }
  // }

  // public void moveRightWheelTo(double target) {
  //     double diff = target - getRight();
  //     double sign = Math.signum(diff);
  //     double absDiff = Math.abs(diff);
  //     double minDiff = 0.5;
  //     double scaleDiff = 5;
  //     double adjusted = scaleDiff - minDiff;
  //     double lowest = 0.2;
  //     double temp = 1;
  //     if (absDiff < minDiff) {
  //         rightMotors.stopMotor();
  //     } else {
  //         if (absDiff < scaleDiff) {
  //             temp = map(absDiff, minDiff, adjusted, lowest, 1);
  //         }
  //         if (sign == -1) {
  //             rightMotors.set(-0.175*temp);
  //         } else if (sign == 1) {
  //             rightMotors.set(0.175*temp);
  //         } else {
  //             rightMotors.stopMotor();
  //         }
  //     }
  // }

  public double map(double x, double a, double b, double c, double d) {
      return (x-a)/(b-a)*(d-c)+c; // (absDiff - minDiff)/(adjusted - minDiff)*(1 - lowest)+ lowest -- limiter
  }

  /** Wrapper method to call stopMotor on the internal {@link DifferentialDrive} object. */
  public void stopMotor() {
    diffDrive.stopMotor();
  }
}