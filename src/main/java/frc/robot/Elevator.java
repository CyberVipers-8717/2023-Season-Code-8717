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
  private static final double maximumElevatorCommand = 0.55;
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
    runElevator(manualRetractScale);
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
    runPulley(manualRotateUpScale);
  }

  /**
   * Rotates the arm to the specified encoder position.
   * @param target The encoder position that the pulley motor will run to match.
   */
  public void rotateArmTo(double target) {
    Robot.moveMotorTo(target, getPulleyPosition(), pulleyMotor, minimumPulleyDifference, maximumPulleyCommand, whenToScalePulleyCommand);
  }

  /**
   * Extends, or retracts, the elevator to the specified encoder position.
   * @param target The encoder position that the elevator motors will run to match.
   */
  public void runArmTo(double target) {
    Robot.moveMotorTo(target, getElevatorPosition(), elevatorMotors, minimumElevatorDifference, maximumElevatorCommand, whenToScaleElevatorCommand);
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
          rotateArmTo(ElevatorConstants.highSP);
          runArmTo(ElevatorConstants.highSE);
        } else {
          rotateArmTo(ElevatorConstants.highTP);
          runArmTo(ElevatorConstants.highTE);
        }
        break;
      case 90: // right
        // mid grid
        if (targetingCube) {
          rotateArmTo(ElevatorConstants.midSP);
          runArmTo(ElevatorConstants.midSE);
        } else {
          rotateArmTo(ElevatorConstants.midTP);
          runArmTo(ElevatorConstants.midTE);
        }
        break;
      case 180: // down
        // double player station
        if (targetingCube) {
          rotateArmTo(ElevatorConstants.doubleSP);
          runArmTo(ElevatorConstants.doubleSE);
        } else {
          rotateArmTo(ElevatorConstants.doubleTP);
          runArmTo(ElevatorConstants.doubleTE);
        }
        break;
      case 270: // left
        rotateArmTo(ElevatorConstants.restP);
        runArmTo(ElevatorConstants.restE);
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