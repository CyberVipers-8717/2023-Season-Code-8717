package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorConstants;

public class Elevator {
  public static boolean targetingCube = true;

  private static final double manualRotateDownScale = 0.5;
  private static final double manualRotateUpScale = 0.9;
  private static final double manualExtendScale = 0.35;
  private static final double manualRetractScale = 0.3;

  private static final double minimumElevatorDifference = 0.5;
  private static final double minimumPulleyDifference = 1;
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

  public static void robotInit() {
      elevatorMotorL.setInverted(true);
      pulleyMotor.setIdleMode(IdleMode.kBrake);
  }

  /** Toggles the current targeted object between cone and cube. */
  public static void toggleTarget() {
    targetingCube = targetingCube ? false : true;
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

  /**
   * Runs the pulley motor to either rotate the arm down or up.
   * @param speed The speed that the pulley motor will be set to.
   */
  private static void runPulley(double speed) {
    pulleyMotor.set(speed);
  }

  /** Rotates the arm down at the manual rotation down speed. */
  public static void rotateDown() {
    runPulley(manualRotateDownScale);
  }

  /** Rotates the arm up at the manual rotation up speed. */
  public static void rotateUp() {
    runPulley(-manualRotateUpScale);
  }

  /**
   * Runs the elevator motors to either extend or retract the elevator.
   * @param speed The speed that the elevator motors will bet set to.
   */
  private static void runElevator(double speed) {
    elevatorMotors.set(speed);
  }

  /** Extends the elevator at the manual extension speed. */
  public static void extend() {
    runElevator(manualExtendScale);
  }

  /** Retracts the elevator at the manual extension speed. */
  public static void retract() {
    runElevator(-manualRetractScale);
  }

  /**
   * Rotates the arm to the specified encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   */
  public static void pulleyTo(double target) {
    Robot.moveMotorTo(target, getPulleyPosition(), pulleyMotor, minimumPulleyDifference, maximumPulleyCommand, whenToScalePulleyCommand);
  }

  /**
   * Extends, or retracts, the elevator to the specified encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   */
  public static void elevatorTo(double target) {
    Robot.moveMotorTo(target, getElevatorPosition(), elevatorMotors, minimumElevatorDifference, maximumElevatorCommand, whenToScaleElevatorCommand);
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

  /**
   * Wrapper method that rotates the arm and runs the elevator to the specified encoder positions.
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   */
  public static void armTo(double targetPulley, double targetElevator) {
    pulleyTo(targetPulley);
    elevatorTo(targetElevator);
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
        armToHigh();
        break;
      case 90: // right
        armToMid();
        break;
      case 180: // down
        armToDouble();
        break;
      case 270: // left
        armToRest();
        break;
    }
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