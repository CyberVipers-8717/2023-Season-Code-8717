package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Elevator implements Sendable {
  public static enum Item {Cube, Cone}
  public static enum Height {High, Mid, Ground, Rest, Double}
  public static Item targetItem = Item.Cube;
  public static Height targetHeight = Height.High;

  private static CANSparkMax elevatorMotorL = new CANSparkMax(6, MotorType.kBrushless);
  private static CANSparkMax elevatorMotorR = new CANSparkMax(7, MotorType.kBrushless);
  private static MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
  private static CANSparkMax pulleyMotor = new CANSparkMax(5, MotorType.kBrushless);

  private static RelativeEncoder pulleyEncoder = pulleyMotor.getEncoder();
  private static RelativeEncoder elevatorEncoderL = elevatorMotorL.getEncoder();
  private static RelativeEncoder elevatorEncoderR = elevatorMotorR.getEncoder();

  /** Initializes the sendable. */
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Elevator");
    builder.addDoubleProperty("Elevator position", Elevator::getElevatorPosition, null);
    builder.addDoubleProperty("Pulley position", Elevator::getPulleyPosition, null);
    builder.addStringProperty("Elevator target name", ElevatorPresets::getNameOfHeight, null);
    builder.addDoubleArrayProperty("Elevator target values", ElevatorPresets::getTargetPresets, null);
  }

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
      elevatorMotorL.setInverted(true);
      pulleyMotor.setIdleMode(IdleMode.kBrake);
  }

  /** Toggles the current targeted object between cone and cube. */
  public static void toggleTarget() {
    targetItem = targetItem == Item.Cube ? Item.Cone : Item.Cube;
  }

  /** Sets the current target object to a cube. */
  public static void targetCube() {
    targetItem = Item.Cube;
  }

  /** Sets the current target object to a cone. */
  public static void targetCone() {
    targetItem = Item.Cone;
  }

  /** Sets the current target height to high grid. */
  public static void targetHigh() {
    targetHeight = Height.High;
  }

  /** Sets the current target height to mid grid. */
  public static void targetMid() {
    targetHeight = Height.Mid;
  }

  /** Sets the current target height to ground. */
  public static void targetGround() {
    targetHeight = Height.Ground;
  }

  /** Sets the current target height to rest. */
  public static void targetRest() {
    targetHeight = Height.Rest;
  }

  /** Sets the current target height to double player station. */
  public static void targetDouble() {
    targetHeight = Height.Double;
  }

  private static final double manuExtend = 0.4;
  /** Extends the elevator at the manual extension speed. */
  public static void extend() {
    elevatorMotors.set(manuExtend);
  }

  private static final double manuRetract = 0.3;
  /** Retracts the elevator at the manual extension speed. */
  public static void retract() {
    elevatorMotors.set(-manuRetract);
  }

  private static final double manuRotDown = 0.5;
  /** Rotates the arm down at the manual rotation down speed. */
  public static void rotateDown() {
    pulleyMotor.set(manuRotDown);
  }

  private static final double manuRotUp = 0.9;
  /** Rotates the arm up at the manual rotation up speed. */
  public static void rotateUp() {
    pulleyMotor.set(-manuRotUp);
  }

  private static final double minEleDiff = 0.5;
  // the minimum difference in the current elevator encoder position and the target one to begin moving the motor
  private static final double whenEleCom = 15;
  // the encoder difference to begin scaling the command down so as not to overshoot the target
  private static final double maxEleCom = 0.85;
  // the maximum command that can be given

  /**
   * Runs the elevator motors until their encoder positions match the target encoder position.
   * @param target A double indicating the target encoder position of the elevator motors.
   */
  private static void elevatorTo(double target) {
    Robot.moveMotorTo(target, getElevatorPosition(), elevatorMotors, minEleDiff, maxEleCom, whenEleCom);
  }

  private static final double minPulDiff = 0.5;
  // the minimum difference in the current pulley encoder position and the target one to begin moving the motor
  private static final double whenPulCom = 25;
  // the encoder difference to begin scaling the command down so as not to overshoot the target
  private static final double maxPulCom = 0.85;
  // the maximum command that can be given

  /**
   * Runs the pulley motor until its encoder position matches the target encoder position.
   * @param target A double indicating the target encoder position of the pulley motor.
   */
  private static void pulleyTo(double target) {
    Robot.moveMotorTo(target, getPulleyPosition(), pulleyMotor, minPulDiff, maxPulCom, whenPulCom);
  }
  
  /**
   * Runs the elevator and pulley motors until they match the target encoder positions.
   * @param elevatorTarget A double indicating the target encoder position of the elevator motors.
   * @param pulleyTarget A double indicating the target encoder position of the pulley motor.
   */
  public static void armTo(double elevatorTarget, double pulleyTarget) {
    elevatorTo(elevatorTarget);
    pulleyTo(pulleyTarget);
  }

  /**
   * Wrapper method that calls the armTo(double double) method given a double array.
   * @param targets A double array indicating the target elevator and pulley encoder positions.
   * The array is in the form of double[] {elevatorTarget, pulleyTarget}.
   */
  private static void armTo(double[] targets) {
    elevatorTo(targets[0]);
    pulleyTo(targets[1]);
  }

  /** Runs the elevator and pulley motors to the saved target height and object presets. */
  public static void runArm() {
    armTo(ElevatorPresets.getTargetPresets());
  }

  /**
   * Calculates if the elevator motors are at the target encoder position.
   * @param target A double indicating the target encoder position of the elevator motors.
   * @return A boolean indicating if the elevator motors are at an acceptable distance from the target encoder position.
   */
  private static boolean elevatorAtPos(double target) {
    return Robot.motorAtTarget(target, getElevatorPosition(), minEleDiff);
  }

  /**
   * Calculates if the pulley motor is at the target encoder position.
   * @param target A double indicating the target encoder position of the pulley motor.
   * @return A boolean indicating if the pulley motor is at an acceptable distance from the target encoder position.
   */
  private static boolean pulleyAtPos(double target) {
    return Robot.motorAtTarget(target, getPulleyPosition(), minPulDiff);
  }

  /**
   * Calculates if the elevator and pulley motors are at the target encoder positions.
   * @param elevatorTarget A double indicating the target encoder position of the elevator motors.
   * @param pulleyTarget A double indicating the target encoder position of the pulley motor.
   * @return A boolean indicating if the elevator and pulley motors are at an acceptable distance from the target encoder positions.
   */
  private static boolean armAtPos(double elevatorTarget, double pulleyTarget) {
    return elevatorAtPos(elevatorTarget) && pulleyAtPos(pulleyTarget);
  }

  /**
   * Wrapper method that calls the armAtPos(double double) method given a double array.
   * @param targets A double array indicating the target elevator and pulley encoder positions.
   * The array is in the form of double[] {elevatorTarget, pulleyTarget}.
   * @return A boolean indicating if the elevator and pulley motors are at an acceptable distance from the target encoder positions.
   */
  private static boolean armAtPos(double[] targets) {
    return armAtPos(targets[0], targets[1]);
  }

  /**
   * @return A boolean indicating if the elevator and pulley motors are at the saved target height and object presets.
   */
  public static boolean armAtTargets() {
    return armAtPos(ElevatorPresets.getTargetPresets());
  }

  /**
   * Runs the elevator and pulley to preset encoder positions for cones and cubes.
   * @param pov The POV angle from the controller.
   * @param targetingCube A boolean indicating if the current target is a cube, false indicates target is a cone.
   */
  public static void handlePOV(int pov) {
    switch (pov) {
      case 0: // up
        targetHigh();
        break;
      case 90: // right
        targetMid();
        break;
      case 180: // down
        targetDouble();
        break;
      case 270: // left
        targetRest();
        break;
    }
    runArm();
  }

  /**
   * @return The average encoder position of the two elevator motors.
   */
  public static double getElevatorPosition() {
    return (elevatorEncoderL.getPosition() + elevatorEncoderR.getPosition())/2;
  }

  /**
   * @return The encoder position of the pulley motor.
   */
  public static double getPulleyPosition() {
    return pulleyEncoder.getPosition();
  }

  /**
   * Sets the encoder positions of the pulley and elevator to 0.
   */
  public static void zeroEncoders() {
    elevatorEncoderL.setPosition(0);
    elevatorEncoderR.setPosition(0);
    pulleyEncoder.setPosition(0);
  }

  /** Calls the stopMotor method of the elevator motors. */
  public static void stopElevator() {
    elevatorMotors.stopMotor();
  }

  /** Calls the stopMotor method of the pulley motor. */
  public static void stopPulley() {
    pulleyMotor.stopMotor();
  }

  /** Calls the stopMotor method of the elevator motors and pulley motor. */
  public static void stopMotor() {
    stopElevator();
    stopPulley();
  }
}