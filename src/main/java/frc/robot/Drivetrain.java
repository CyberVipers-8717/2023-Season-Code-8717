package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain implements Sendable {
  private static final double arcadeForwardScale = 1;
  private static final double arcadeRotationScale = 0.65;
  private static final double tankScale = 0.8;
  private static final double minimumEncoderDifference = 0.5;
  private static final double whenToScaleCommand = 5;
  private static final double maximumCommand = 0.25;

  public static boolean usingController = false;

  public static double savedLeftPosition = 0;
  public static double savedRightPosition = 0;

  public static boolean kReverseFlag = false;
  public static boolean kTankFlag = false;

  private static CANSparkMax motorLB = new CANSparkMax(1, MotorType.kBrushless);
  private static CANSparkMax motorLF = new CANSparkMax(2, MotorType.kBrushless);
  private static CANSparkMax motorRF = new CANSparkMax(3, MotorType.kBrushless);
  private static CANSparkMax motorRB = new CANSparkMax(4, MotorType.kBrushless);
  private static MotorControllerGroup leftMotors = new MotorControllerGroup(motorLB, motorLF);
  private static MotorControllerGroup rightMotors = new MotorControllerGroup(motorRB, motorRF);
  public static DifferentialDrive diffDrive = new DifferentialDrive(leftMotors, rightMotors);

  private static RelativeEncoder encoderLB = motorLB.getEncoder();
  private static RelativeEncoder encoderLF = motorLF.getEncoder();
  private static RelativeEncoder encoderRF = motorRF.getEncoder();
  private static RelativeEncoder encoderRB = motorRB.getEncoder();

  public static class Gyro implements Sendable {
    private static ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    public static double startingAngle = 0;
    public static double startingAngle180Offset = 180;
    public static double currentAngleTarget;

    /** Initializes the sendable. */
    @Override
    public void initSendable(SendableBuilder builder) {
      //builder.setSmartDashboardType("CustomGyro");
      builder.addDoubleProperty("Starting angle", () -> Gyro.startingAngle, null);
      builder.addDoubleProperty("Current angle", Gyro::getAngle, null);
      builder.addDoubleProperty("Angle target", () -> Gyro.currentAngleTarget, null);
      builder.addBooleanProperty("Facing target", () -> Gyro.facingAngle(Gyro.currentAngleTarget), null);
    }

    /** Initialization code. */
    public static void init() {
      calibrate();
    }

    /**
     * @return The current angle of the robot. The angle increases as the gyro turns clockwise
     * when looked at from above.
     */
    public static double getAngle() {
      return gyro.getAngle()%360;
    }

    /**
     * @return The 180 degree opposite of the target angle.
     */
    public static double get180Offset(double angle) {
      return (angle+180)%360;
    }
    
    /** Calibrates the gyro. */
    public static void calibrate() {
      gyro.calibrate();
    }
    
    /** Saves the starting angle of the robot and the 180 degree offset of it. */
    public static void saveStartingAngle() {
      startingAngle = getAngle();
      startingAngle180Offset = get180Offset(startingAngle);
    }

    /**
     * @param target The angle to check that the gyro is facing.
     * @return A boolean indicating if the gyro is facing the desired angle.
     */
    public static boolean facingAngle(double target) {
      return Math.abs((target%360)-Gyro.getAngle()) < Lime.minimumHeadingError;
    }
  }

  public static double currentLeftTarget;
  public static double currentRightTarget;

  /** Initializes the sendable. */
  @Override
  public void initSendable(SendableBuilder builder) {
    //builder.setSmartDashboardType("Drivetrain");
    builder.addDoubleProperty("Left position", Drivetrain::getLeftPosition, null);
    builder.addDoubleProperty("Right position", Drivetrain::getRightPosition, null);
    builder.addDoubleProperty("Left target", () -> Drivetrain.currentLeftTarget, null);
    builder.addDoubleProperty("Right target", () -> Drivetrain.currentRightTarget, null);
    builder.addBooleanProperty("Left at target", () -> Drivetrain.leftTrackAtPosition(Drivetrain.currentLeftTarget), null);
    builder.addBooleanProperty("Right at target", () -> Drivetrain.rightTrackAtPosition(Drivetrain.currentRightTarget), null);
  }

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
    motorLB.setInverted(true);
    motorLF.setInverted(true);
    setDriveIdle(IdleMode.kBrake);
    Gyro.init();
  }

  /**
   * Rotates the robot to face any given angle.
   * @param target The angle, in degrees, that the robot should face.
   */
  public static void rotateToCorrectAngle(double target) {
    Gyro.currentAngleTarget = target;
    double adjust = 0;
    double angleError = (target%360) - Gyro.getAngle();
    double abs = Math.abs(angleError);
    if (abs > Lime.minimumHeadingError) {
      adjust = Lime.controlConstant * abs + Lime.minCommand;
      adjust = Math.min(adjust, Lime.maxCommand);
      adjust = Math.copySign(adjust, angleError);
    }
    tank(-adjust, adjust);
  }

  /** Feeds the motor safety objects of the drive motors. */
  public static void feed() {
    diffDrive.feed();
  }

  /**
   * @return The average position of the left drive motor encoders.
   */
  public static double getLeftPosition() {
    return (encoderLB.getPosition() + encoderLF.getPosition())/2;
  }

  /**
   * @return The average position of the right drive motor encoders.
   */
  public static double getRightPosition() {
    return (encoderRB.getPosition() + encoderRF.getPosition())/2;
  }


  /** Sets every drive motor encoder position to 0. */
  public static void zeroDriveEncoders() {
    encoderLB.setPosition(0);
    encoderLF.setPosition(0);
    encoderRF.setPosition(0);
    encoderRB.setPosition(0);
  }

  /** Calls the arcadeDrive method of the internal {@link DifferentialDrive} object.
   * @param forward Robot's speed along the X axis. Forward is positive.
   * @param rotation Robot's rotation speed around the Z axis. Counterclockwise is positive.
   */
  public static void arcade(double forward, double rotation) {
    forward *= arcadeForwardScale;
    rotation *= arcadeRotationScale;
    if (kReverseFlag) forward *= -1;
    diffDrive.arcadeDrive(forward, rotation);
  }
  
  /** Calls the arcadeDrive method of the internal {@link DifferentialDrive} object.
   * @param vals A double array containing the forward and rotation values to be passed on to arcadeDrive method of the {@link DifferentialDrive} object.
  */
  public static void arcade(double[] vals) {
    arcade(vals[0], vals[1]);
  }

  /**
   * Calls the arcadeDrive method of the internal {@link DifferentialDrive} object.
   * @param stickL The {@link CustomJoystick} that the forward speed comes from.
   * @param stickR The {@link CustomJoystick} that the rotation speed comes from.
   */
  public static void arcade(CustomJoystick stickL, CustomJoystick stickR) {
    arcade(stickL.getX(), stickR.getY());
  }

  /** Calls the tankDrive method of the internal {@link DifferentialDrive} object.
   * @param left Robot's left side speed along the X axis. Forward is positive.
   * @param right Robot's right side speed along the X axis. Forward is positive.
   */
  public static void tank(double left, double right) {
    left *= tankScale;
    right *= tankScale;
    if (!kReverseFlag) diffDrive.tankDrive(left, right);
    else diffDrive.tankDrive(-right, -left);
  }

  /** Calls the tankDrive method of the internal {@link DifferentialDrive} object.
   * @param vals A double array containing the left and right values to be passed on to tankDrive method of the {@link DifferentialDrive} object.
  */
  public static void tank(double[] vals) {
    tank(vals[0], vals[1]);
  }

  /**
   * Calls the tankDrive method of the internal {@link DifferentialDrive} object.
   * @param stickL The {@link CustomJoystick} that the left track speed comes from.
   * @param stickR The {@link CustomJoystick} that the right track speed comes from.
   */
  public static void tank(CustomJoystick stickL, CustomJoystick stickR) {
    tank(stickL.getX(), stickR.getX());
  }

  /**
   * A drive method that will either call arcadeDrive or tankDrive depending on how the kTankFlag is set.
   * @param vals The first and second value to be passed into the arcadeDrive or tankDrive method. For
   * arcadeDrive, the values are forward speed and rotation speed. For tankDrive, the values are left track
   * speed and right track speed.
   */
  public static void generic(double[] vals) {
    if (!kTankFlag) arcade(vals);
    else tank(vals);
  }

  /** Align the robot to the center of the target on the limelight. */
  public static void limeAlign() {
    tank(Lime.autoCenter());
  }

  /** Sets the {@link IdleMode} of all drive motors.
   * @param mode The mode to set the motors to, either coast or brake.
   */
  public static void setDriveIdle(IdleMode mode) {
    motorLB.setIdleMode(mode);
    motorLF.setIdleMode(mode);
    motorRF.setIdleMode(mode);
    motorRB.setIdleMode(mode);
  }

  /**
   * @return The {@link IdleMode} that the drive motors are currently set to.
   */
  public static IdleMode getDriveIdle() {
    return motorLB.getIdleMode();
  }

  /** Toggles the {@link IdleMode} of the drive motors between coast and brake. */
  public static void toggleDriveIdle() {
    setDriveIdle(motorLB.getIdleMode() == IdleMode.kBrake ? IdleMode.kCoast : IdleMode.kBrake);
  }

  /** Toggles the kTankFlag between true and false. */
  public static void toggleTankFlag() {
    kTankFlag = kTankFlag ? false : true;
  }

  /** Toggles the kReverseFlag between true and false. */
  public static void toggleReverseFlag() {
    kReverseFlag = kReverseFlag ? false : true;
  }

  /** Toggles the control of the drivetrain between the joysticks and the controller. */
  public static void toggleDriverControl() {
    usingController = usingController ? false : true;
  }

  /** Sets both the kTankFlag and kReverseFlag to false. */
  public static void defaultFlags() {
    kTankFlag = false;
    kReverseFlag = false;
  }

  /**
   * Moves the robot's left and right tracks to obtain the desired left and right encoder positions.
   * @param left The encoder position for the left track to drive to.
   * @param right The encoder position for the right track to drive to.
   */
  public static void moveTracksTo(double left, double right) {
    Robot.moveMotorTo(left, getLeftPosition(), leftMotors, minimumEncoderDifference, maximumCommand, whenToScaleCommand);
    Robot.moveMotorTo(right, getRightPosition(), rightMotors, minimumEncoderDifference, maximumCommand, whenToScaleCommand);
  }

  /**
   * Checks if the robot's left track is at the desired encoder position.
   * @param target The encoder position the left track is checked against.
   * @return A boolean indicating if the robot's left track is at the specified encoder position.
   */
  private static boolean leftTrackAtPosition(double target) {
    return Robot.motorAtTarget(target, getLeftPosition(), minimumEncoderDifference);
  }

  /**
   * Checks if the robot's right track is at the desired encoder position.
   * @param target The encoder position the right track is checked against.
   * @return A boolean indicating if the robot's right track is at the specified encoder position.
   */
  private static boolean rightTrackAtPosition(double target) {
    return Robot.motorAtTarget(target, getRightPosition(), minimumEncoderDifference);
  }

  /**
   * Checks if both the robot's left and right tracks are at the desired encoder positions.
   * @param left The encoder position the left track is checked against.
   * @param right The encoder position the right track is checked against.
   * @return A boolean indicating if both the robot's left and right tracks are at the specified encoder positions.
   */
  public static boolean tracksAtPosition(double left, double right) {
    return leftTrackAtPosition(left) && rightTrackAtPosition(right);
  }

  /** Saves the current robot drive motor encoder positions to later be used when maintaining the robot's position. */
  public static void saveCurrentRobotPosition() {
    savedLeftPosition = getLeftPosition();
    savedRightPosition = getRightPosition();
  }

  /**
   * Method that, when called, will attempt to keep the robot at the saved drive motor encoder positions.
   * This is useful when balancing as the {@link IdleMode} of the drive encoders, when set to kBrake,
   * is not strong enough to keep the robot stationary should the balancing station be tilted.
   */
  public static void maintainRobotPosition() {
    moveTracksTo(savedLeftPosition, savedRightPosition);
  }

  /**
   * Maintains the robot's position at the saved encoder positions.
   * @param pressed A boolean indicating if the button to maintain the robot's position was pressed.
   */
  public static void maintainRobotPosition(boolean pressed) {
    if (pressed) saveCurrentRobotPosition();
    maintainRobotPosition();
  }

  /** Wrapper method to call stopMotor on the internal {@link DifferentialDrive} object. */
  public static void stopMotor() {
    diffDrive.stopMotor();
  }
}