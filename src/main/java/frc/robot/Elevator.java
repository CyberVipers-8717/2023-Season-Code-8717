package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.ElevatorConstants.*;

public class Elevator {
  public static boolean targetingCube = true;

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

  /** Retracts or extends the elevator at the manual extension speed. */
  public void extend(boolean isExtending) {
    runElevator(isExtending ? ElevatorConstants.manualExtendScale : ElevatorConstants.manualRetractScale);
  }

  /**
   * Runs the pulley motor to either rotate the arm down or up.
   * @param speed The speed that the pulley motor will be set to.
   */
  public void runPulley(double speed) {
    pulleyMotor.set(speed);
  }

  /** Rotates the arm up or down at the manual rotation down speed. */
  public void rotateDown(boolean isDown) {
    runPulley(isDown ? ElevatorConstants.manualRotateDownScale : ElevatorConstants.manualRotateUpScale);
  }

  /**
   * Rotates the arm to the specified encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   */
  public void pulleyTo(double target) {
    RobotMethods.moveMotorTo(target, getPulleyPosition(), pulleyMotor,
    ElevatorConstants.minimumPulleyDifference, ElevatorConstants.maximumPulleyCommand, ElevatorConstants.whenToScalePulleyCommand);
  }

  /**
   * Extends, or retracts, the elevator to the specified encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   */
  public void elevatorTo(double target) {
    RobotMethods.moveMotorTo(target, getElevatorPosition(), elevatorMotors,
    ElevatorConstants.minimumElevatorDifference, ElevatorConstants.maximumElevatorCommand, ElevatorConstants.whenToScaleElevatorCommand);
  }

  /**
   * Calculates if the pulley motor is at the target encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   * @return A boolean indicating whether the pulley motor is at its target encoder position.
   */
  public boolean pulleyAtPosition(double target) {
    return RobotMethods.motorAtTarget(target, getPulleyPosition(), ElevatorConstants.minimumPulleyDifference);
  }

  /**
   * Calculates if the elevator motors are at the target encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   * @return A boolean indicating whether the elevator motors are at the target encoder position.
   */
  public boolean elevatorAtPosition(double target) {
    return RobotMethods.motorAtTarget(target, getElevatorPosition(), ElevatorConstants.minimumElevatorDifference);
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

  public void armAtPresetHelper(boolean atPresetHelper, double squarePulley,
    double squareElevator, double targetPulley, double triangleElevator) {
      if (targetingCube) atPresetHelper = armAtPositions(squarePulley, squareElevator);
      else atPresetHelper = armAtPositions(targetPulley, triangleElevator);
    }

  public void handlePOVHelper(boolean targetingCube, double squarePulley,
    double squareElevator, double targetPulley, double triangleElevator) {
      pulleyTo(targetingCube ? squareElevator : triangleElevator);
      pulleyTo(targetingCube ? squarePulley : targetPulley);
    }
  /**
   * @param targetPulley The encoder position that the pulley motor will run to.
   * @param targetElevator The encoder position that the elevator motors will run to.
   * @return A boolean indicating if both the elevator and pulley motors are at the specified encoder positions.
   */
  public boolean armAtPositions(double targetPulley, double targetElevator) {
    return pulleyAtPosition(targetPulley) && elevatorAtPosition(targetElevator);
  }

  public boolean armAtPreset(int preset) {
    boolean atPreset = false;
    switch (preset) {
      case SymbolicPresets._high:
        armAtPresetHelper(atPreset, ElevatorConstants.highSP, ElevatorConstants.highSE, ElevatorConstants.highTP, ElevatorConstants.highTE);
        break;
      case SymbolicPresets._medium:
        armAtPresetHelper(atPreset, ElevatorConstants.midSP, ElevatorConstants.midSE, ElevatorConstants.midTP, ElevatorConstants.midTE);
        break;
      case SymbolicPresets._double:
        armAtPresetHelper(atPreset, ElevatorConstants.doubleSP, ElevatorConstants.doubleSE, ElevatorConstants.doubleTP, ElevatorConstants.doubleTE);
        break;
      case SymbolicPresets._rest:
        armAtPresetHelper(atPreset, ElevatorConstants.restP, ElevatorConstants.restE, ElevatorConstants.restP, ElevatorConstants.restE);
        break;
      default: break;
    }
    return atPreset;
  }
  
  /**
   * Runs the elevator and pulley to preset encoder positions for cones and cubes.
   * @param pov The POV angle from the controller.
   * @param targetingCube A boolean indicating if the current target is a cube, false indicates target is a cone.
   */
  public void handlePOV(int pov) {
    switch (pov) {
      case SymbolicPresets._up: // 0
        // high grid
        handlePOVHelper(targetingCube, ElevatorConstants.highSP, ElevatorConstants.highSE, ElevatorConstants.highTP, ElevatorConstants.highTE);
        break;
      case SymbolicPresets._right: // 90
        // mid grid
        handlePOVHelper(targetingCube, ElevatorConstants.midSP, ElevatorConstants.midSE, ElevatorConstants.midTP, ElevatorConstants.midTE);
        break;
      case SymbolicPresets._down: // 180
        // double player station
        handlePOVHelper(targetingCube, ElevatorConstants.doubleSP, ElevatorConstants.doubleSE, ElevatorConstants.doubleTP, ElevatorConstants.doubleTE);
        break;
      case SymbolicPresets._left: // 270
        handlePOVHelper(targetingCube, ElevatorConstants.restP, ElevatorConstants.restE, ElevatorConstants.restP, ElevatorConstants.restE);
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