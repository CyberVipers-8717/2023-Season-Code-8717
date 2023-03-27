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

  private CANSparkMax elevatorMotorL = new CANSparkMax(6, MotorType.kBrushless);
  private CANSparkMax elevatorMotorR = new CANSparkMax(7, MotorType.kBrushless);
  private MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
  private CANSparkMax pulleyMotor = new CANSparkMax(5, MotorType.kBrushless);

  private RelativeEncoder pulleyEncoder = pulleyMotor.getEncoder();
  private RelativeEncoder elevatorEncoderL = elevatorMotorL.getEncoder();
  private RelativeEncoder elevatorEncoderR = elevatorMotorR.getEncoder();

  public Elevator() {
      elevatorMotorL.setInverted(true);
      pulleyMotor.setIdleMode(IdleMode.kBrake);
  }

  /**
   * @return The average encoder position of the two elevator motors.
   */
  public double getElevatorPosition() {
    return (elevatorEncoderL.getPosition() + elevatorEncoderR.getPosition())/2;
  }

  /**
   * @return The encoder position of the pulley motor.
   */
  public double getPulleyPosition() {
    return pulleyEncoder.getPosition();
  }

  /**
   * Sets the encoder positions of the pulley and elevator to 0.
   */
  public void zeroEncoders() {
    elevatorEncoderL.setPosition(0);
    elevatorEncoderR.setPosition(0);
    pulleyEncoder.setPosition(0);
  }

  /**
   * Runs the elevator motors to either extend or retract the elevator.
   * @param speed The speed that the elevator motors will bet set to.
   */
  public void runElevator(double speed) {
    elevatorMotors.set(speed);
  }

  /** Extends the elevator at the manual extension speed. */
  public void extend() {
    runElevator(manualExtendScale);
  }

  /** Retracts the elevator at the manual extension speed. */
  public void retract() {
    runElevator(-manualRetractScale);
  }

  /**
   * Runs the pulley motor to either rotate the arm down or up.
   * @param speed The speed that the pulley motor will be set to.
   */
  public void runPulley(double speed) {
    pulleyMotor.set(speed);
  }

  /** Rotates the arm down at the manual rotation down speed. */
  public void rotateDown() {
    runPulley(manualRotateDownScale);
  }

  /** Rotates the arm up at the manual rotation up speed. */
  public void rotateUp() {
    runPulley(-manualRotateUpScale);
  }

  /**
   * Rotates the arm to the specified encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   */
  public void pulleyTo(double target) {
    Robot.moveMotorTo(target, getPulleyPosition(), pulleyMotor, minimumPulleyDifference, maximumPulleyCommand, whenToScalePulleyCommand);
  }

  /**
   * Extends, or retracts, the elevator to the specified encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   */
  public void elevatorTo(double target) {
    Robot.moveMotorTo(target, getElevatorPosition(), elevatorMotors, minimumElevatorDifference, maximumElevatorCommand, whenToScaleElevatorCommand);
  }

  /**
   * Calculates if the pulley motor is at the target encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   * @return A boolean indicating whether the pulley motor is at its target encoder position.
   */
  public boolean pulleyAtPosition(double target) {
    return Robot.motorAtTarget(target, getPulleyPosition(), minimumPulleyDifference);
  }

  /**
   * Calculates if the elevator motors are at the target encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   * @return A boolean indicating whether the elevator motors are at the target encoder position.
   */
  public boolean elevatorAtPosition(double target) {
    return Robot.motorAtTarget(target, getElevatorPosition(), minimumElevatorDifference);
  }

  /**
   * Wrapper method that rotates the arm and runs the elevator to the specified encoder positions.
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   */
  public void armTo(double targetPulley, double targetElevator) {
    pulleyTo(targetPulley);
    elevatorTo(targetElevator);
  }

  /**
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   * @return A boolean indicating if both the elevator and pulley motors are at the specified encoder positions.
   */
  public boolean armAtPositions(double targetPulley, double targetElevator) {
    return pulleyAtPosition(targetPulley) && elevatorAtPosition(targetElevator);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at high grid.
   */
  public boolean armAtHigh() {
    if (targetingCube) return armAtPositions(ElevatorConstants.highSP, ElevatorConstants.highSE);
    else return armAtPositions(ElevatorConstants.highTP, ElevatorConstants.highTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at mid grid.
   */
  public boolean armAtMid() {
    if (targetingCube) return armAtPositions(ElevatorConstants.midSP, ElevatorConstants.midSE);
    else return armAtPositions(ElevatorConstants.midTP, ElevatorConstants.midTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at double player station.
   */
  public boolean armAtDouble() {
    if (targetingCube) return armAtPositions(ElevatorConstants.doubleSP, ElevatorConstants.doubleSE);
    else return armAtPositions(ElevatorConstants.doubleTP, ElevatorConstants.doubleTE);
  }

  /**
   * @return A boolean indicating if both the elevator and pulley are at resting position.
   */
  public boolean armAtRest() {
    return armAtPositions(ElevatorConstants.restP, ElevatorConstants.restE);
  }

  /**
   * Runs the elevator and pulley to preset encoder positions for cones and cubes.
   * @param pov The POV angle from the controller.
   * @param targetingCube A boolean indicating if the current target is a cube, false indicates target is a cone.
   */
  public void handlePOV(int pov) {
    switch (pov) {
      case 0: // up
        // high grid
        if (targetingCube) {
          pulleyTo(ElevatorConstants.highSP);
          elevatorTo(ElevatorConstants.highSE);
        } else {
          pulleyTo(ElevatorConstants.highTP);
          elevatorTo(ElevatorConstants.highTE);
        }
        break;
      case 90: // right
        // mid grid
        if (targetingCube) {
          pulleyTo(ElevatorConstants.midSP);
          elevatorTo(ElevatorConstants.midSE);
        } else {
          pulleyTo(ElevatorConstants.midTP);
          elevatorTo(ElevatorConstants.midTE);
        }
        break;
      case 180: // down
        // double player station
        if (targetingCube) {
          pulleyTo(ElevatorConstants.doubleSP);
          elevatorTo(ElevatorConstants.doubleSE);
        } else {
          pulleyTo(ElevatorConstants.doubleTP);
          elevatorTo(ElevatorConstants.doubleTE);
        }
        break;
      case 270: // left
        pulleyTo(ElevatorConstants.restP);
        elevatorTo(ElevatorConstants.restE);
        break;
      default:
        break;
    }
  }

  /** Calls the stopMotor method of the elevator motors. */
  public void stopElevator() {
    elevatorMotors.stopMotor();
  }

  /** Calls the stopMotor method of the pulley motor. */
  public void stopPulley() {
    pulleyMotor.stopMotor();
  }

  /** Calls the stopMotor method of the elevator motors and pulley motor. */
  public void stopMotor() {
    stopElevator();
    stopPulley();
  }
}