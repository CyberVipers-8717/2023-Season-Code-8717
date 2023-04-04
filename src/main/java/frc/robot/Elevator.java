package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorConstants;

/*
 * set arm targets with POV
 * run arm to targets
 * 
 * move arm manually
 */

public class Elevator /*implements Sendable*/ {
  public static enum Item {Cube, Cone}
  public static enum Height {High, Mid, Ground, Rest, Double}
  public static Item targetItem = Item.Cube;
  public static Height targetHeight = Height.High;
  // private static double currentElevatorTarget;
  // private static double currentPulleyTarget;
  // public static boolean targetingCube = true;

  private static final double manualRotateDownScale = 0.5;
  private static final double manualRotateUpScale = 0.9;
  private static final double manualExtendScale = 0.35;
  private static final double manualRetractScale = 0.3;

  private static final double minimumElevatorDifference = 0.5;
  private static final double minimumPulleyDifference = 0.5;
  private static final double whenToScaleElevatorCommand = 15;
  private static final double whenToScalePulleyCommand = 25;
  private static final double maximumElevatorCommand = 0.85;
  private static final double maximumPulleyCommand = 0.85;

  private static CANSparkMax elevatorMotorL = new CANSparkMax(6, MotorType.kBrushless);
  private static CANSparkMax elevatorMotorR = new CANSparkMax(7, MotorType.kBrushless);
  private static MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
  private static CANSparkMax pulleyMotor = new CANSparkMax(5, MotorType.kBrushless);

  private static RelativeEncoder pulleyEncoder = pulleyMotor.getEncoder();
  private static RelativeEncoder elevatorEncoderL = elevatorMotorL.getEncoder();
  private static RelativeEncoder elevatorEncoderR = elevatorMotorR.getEncoder();

  /** Initializes the sendable. */
  // @Override
  // public void initSendable(SendableBuilder builder) {
  //   builder.setSmartDashboardType("Elevator");
  //   builder.addDoubleProperty("Elevator position", Elevator::getElevatorPosition, null);
  //   builder.addDoubleProperty("Pulley position", Elevator::getPulleyPosition, null);
  //   builder.addDoubleProperty("Elevator target", Elevator::getCurrentElevatorTarget, null);
  //   builder.addDoubleProperty("Pulley target", Elevator::getCurrentPulleyTarget, null);
  //   builder.addBooleanProperty("Elevator at target", () -> Elevator.elevatorAtPosition(Elevator.getCurrentElevatorTarget()), null);
  //   builder.addBooleanProperty("Pulley at target", () -> Elevator.pulleyAtPosition(Elevator.getCurrentPulleyTarget()), null);
  // }

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
      elevatorMotorL.setInverted(true);
      pulleyMotor.setIdleMode(IdleMode.kBrake);
  }

  /**
   * @return A double array containing the presets for the elevator motors and pulley motor.
   * They are in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getPresets() {
    if (targetItem == Item.Cube) {
      switch (targetHeight) {
        case High:
          return new double[] {ElevatorConstants.highSE, ElevatorConstants.highSP};
        case Mid:
          return new double[] {ElevatorConstants.midSE, ElevatorConstants.midSP};
        case Ground:
          return new double[] {ElevatorConstants.groundSE, ElevatorConstants.groundSP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleSE, ElevatorConstants.doubleSP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    } else {
      switch (targetHeight) {
        case High:
          return new double[] {ElevatorConstants.highTE, ElevatorConstants.highTP};
        case Mid:
          return new double[] {ElevatorConstants.midTE, ElevatorConstants.midTP};
        case Ground:
          return new double[] {ElevatorConstants.groundTE, ElevatorConstants.groundTP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleTE, ElevatorConstants.doubleTP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    }
  }

  /** Toggles the current targeted object between cone and cube. */
  public static void toggleTarget() {
    targetItem = targetItem == Item.Cube ? Item.Cone : Item.Cube;
  }

  public static void targetCube() {
    targetItem = Item.Cube;
  }

  public static void targetCone() {
    targetItem = Item.Cone;
  }

  public static void targetHigh() {
    targetHeight = Height.High;
  }

  public static void targetMid() {
    targetHeight = Height.Mid;
  }

  public static void targetGround() {
    targetHeight = Height.Ground;
  }

  public static void targetRest() {
    targetHeight = Height.Rest;
  }

  public static void targetDouble() {
    targetHeight = Height.Double;
  }

  /** Extends the elevator at the manual extension speed. */
  public static void extend() {
    elevatorMotors.set(manualExtendScale);
  }

  /** Retracts the elevator at the manual extension speed. */
  public static void retract() {
    elevatorMotors.set(-manualRetractScale);
  }

  /** Rotates the arm down at the manual rotation down speed. */
  public static void rotateDown() {
    pulleyMotor.set(manualRotateDownScale);
  }

  /** Rotates the arm up at the manual rotation up speed. */
  public static void rotateUp() {
    pulleyMotor.set(-manualRotateUpScale);
  }

  private static void elevatorTo(double target) {
    Robot.moveMotorTo(target, getElevatorPosition(), elevatorMotors, minimumElevatorDifference, maximumElevatorCommand, whenToScaleElevatorCommand);
  }

  private static void pulleyTo(double target) {
    Robot.moveMotorTo(target, getPulleyPosition(), pulleyMotor, minimumPulleyDifference, maximumPulleyCommand, whenToScalePulleyCommand);
  }

  public static void runArm() {
    
    // run elevator
    // run pulley
  }

  private static void armTo(double elevatorTarget, double pulleyTarget) {
    
  }

  private static void armTo(Height target) {

  }

  /**
   * Rotates the arm to the specified encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   */
  private static void pulleyToPos(double target) {
    
  }

  /**
   * Extends, or retracts, the elevator to the specified encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   */
  private static void elevatorToPos(double target) {
    
  }

  public static void pulleyTo(Height target) {
    pulleyTo(targetItem == Item.Cube ? ElevatorConstants.highSP : ElevatorConstants.highTP);
  }

  public static void elevatorTo(Height target) {
    elevatorTo(targetItem == Item.Cube ? ElevatorConstants.highSE : ElevatorConstants.highTE);
  }

  /**
   * Calculates if the pulley motor is at the target encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   * @return A boolean indicating whether the pulley motor is at its target encoder position.
   */
  public static boolean pulleyAtPosition(double target) {
    return Robot.motorAtTarget(target, getPulleyPosition(), minimumPulleyDifference);
  }

  /**
   * Calculates if the elevator motors are at the target encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   * @return A boolean indicating whether the elevator motors are at the target encoder position.
   */
  public static boolean elevatorAtPosition(double target) {
    return Robot.motorAtTarget(target, getElevatorPosition(), minimumElevatorDifference);
  }

  public static boolean pulleyAtPosition(double target) {
    return Robot.motorAtTarget(target, getPulleyPosition(), minimumPulleyDifference);
  }

  /**
   * Wrapper method that rotates the arm and runs the elevator to the specified encoder positions.
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   */
  public static void armTo(double targetPulley, double targetElevator) {
    pulleyTo(targetPulley);
    elevatorTo(targetElevator);
  }

  public static void armTo(double[] targets) {
    pulleyTo(targets[0]);
    elevatorTo(targets[1]);
  }

  public static void armTo(Height target) {
    targetHeight = target;
    armTo(getPresets());
    
  }

  /**
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   * @return A boolean indicating if both the elevator and pulley motors are at the specified encoder positions.
   */
  public static boolean armAtPosition(double targetPulley, double targetElevator) {
    return pulleyAtPosition(targetPulley) && elevatorAtPosition(targetElevator);
  }

  /** Wrapper method that rotates the arm and runs the elevator to the high grid position. */
  public static void armToHigh() {
    if (targetingCube) armTo(ElevatorConstants.highSP, ElevatorConstants.highSE);
    else armTo(ElevatorConstants.highTP, ElevatorConstants.highTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at high grid.
   */
  public static boolean armAtHigh() {
    if (targetingCube) return armAtPosition(ElevatorConstants.highSP, ElevatorConstants.highSE);
    else return armAtPosition(ElevatorConstants.highTP, ElevatorConstants.highTE);
  }

  /** Wrapper method that rotates the arm and runs the elevator to the mid grid position. */
  public static void armToMid() {
    if (targetingCube) armTo(ElevatorConstants.midSP, ElevatorConstants.midSE);
    else armTo(ElevatorConstants.midTP, ElevatorConstants.midTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at mid grid.
   */
  public static boolean armAtMid() {
    if (targetingCube) return armAtPosition(ElevatorConstants.midSP, ElevatorConstants.midSE);
    else return armAtPosition(ElevatorConstants.midTP, ElevatorConstants.midTE);
  }

  /** Wrapper method that rotates the arm and runs the elevator to the double player station. */
  public static void armToDouble() {
    if (targetingCube) armTo(ElevatorConstants.doubleSP, ElevatorConstants.doubleSE);
    else armTo(ElevatorConstants.doubleTP, ElevatorConstants.doubleTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at double player station.
   */
  public static boolean armAtDouble() {
    if (targetingCube) return armAtPosition(ElevatorConstants.doubleSP, ElevatorConstants.doubleSE);
    else return armAtPosition(ElevatorConstants.doubleTP, ElevatorConstants.doubleTE);
  }

  /** Wrapper method that rotates the arm and runs the elevator to the high grid position. */
  public static void armToRest() {
    armTo(ElevatorConstants.restP, ElevatorConstants.restE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at resting position.
   */
  public static boolean armAtRest() {
    return armAtPosition(ElevatorConstants.restP, ElevatorConstants.restE);
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
        runArm();
        break;
      case 90: // right
        targetMid();
        runArm();
        break;
      case 180: // down
        targetDouble();
        runArm();
        break;
      case 270: // left
        targetRest();
        runArm();
        break;
    }
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