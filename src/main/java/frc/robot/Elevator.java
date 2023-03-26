package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorEncoderConstants;

/*
 * Structure of Elevator class
 * variable declarations x
 *    manual speeds x
 *    auto speeds x
 * constructor x
 * encoders x
 *    get elevator x
 *    get pulley x
 *    zero positions x
 * dealWithPOV   maybe extract to separate joystick object
 *    rest
 *    double player
 *    mid section
 *    high section
 * special auto stuff
 *    rotate arm to position
 *    extend elevator to position
 * stopMotors x
 *    elevator x
 *    pulley x
 */

public class Elevator {
  private static final double manualRotateDownScale = 0.5;
  private static final double manualRotateUpScale = 0.9;
  private static final double manualExtendScale = 0.35;
  private static final double manualRetractScale = 0.3;
  private static final double autoRotateDownScale = 0.6;
  private static final double autoRotateUpScale = 1;
  private static final double autoExtendScale = 0.55;
  private static final double autoRetractScale = 0.55;

  private CANSparkMax elevatorMotorL = new CANSparkMax(6, MotorType.kBrushless);
  private CANSparkMax elevatorMotorR = new CANSparkMax(7, MotorType.kBrushless);
  public MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
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






// extend and retract

  /**
   * Runs the elevator motors to either extend or retract the elevator.
   * @param speed The speed that the elevator motors will run at.
   */
  public void runElevator(double speed) {
    elevatorMotors.set(speed);
  }

  /** Extends the elevator at the manual extension speed. */
  public void extendElevator() {
    runElevator(manualExtendScale);
  }

  /** Retracts the elevator at the manual extension speed. */
  public void retractElevator() {
    runElevator(manualRetractScale);
  }

// rotate down and up
// specific speeds
// manual speed



  /** Calls the stopMotor method of the elevator motors. */
  public void stopElevatorMotor() {
    elevatorMotors.stopMotor();
  }

  /** Calls the stopMotor method of the pulley motor. */
  public void stopPulleyMotor() {
    pulleyMotor.stopMotor();
  }

  /** Calls the stopMotor method of the elevator motors and pulley motor. */
  public void stopMotor() {
    stopElevatorMotor();
    stopPulleyMotor();
  }





    // /**
    //  * Runs the elevator and pulley to move to the set encoder positions for cones and cubes.
    //  * @param pov The POV angle from the controller.
    //  */
    // public void dealWithPOV(int pov, boolean targetingCube) {
    //     switch (pov) {
    //         case 0: // up
    //             // high grid
    //             if (targetingCube) {
    //                 rotateArmTo(ElevatorEncoderConstants.highSP);
    //                 runElevatorTo(ElevatorEncoderConstants.highSE);
    //             } else {
    //                 rotateArmTo(ElevatorEncoderConstants.highTP);
    //                 runElevatorTo(ElevatorEncoderConstants.highTE);
    //             }
    //             break;
    //         case 90: // right
    //             // mid grid
    //             if (targetingCube) {
    //                 rotateArmTo(ElevatorEncoderConstants.midSP);
    //                 runElevatorTo(ElevatorEncoderConstants.midSE);
    //             } else {
    //                 rotateArmTo(ElevatorEncoderConstants.midTP);
    //                 runElevatorTo(ElevatorEncoderConstants.midTE);
    //             }
    //             break;
    //         case 180: // down
    //             // double player
    //             if (targetingCube) {
    //                 rotateArmTo(ElevatorEncoderConstants.doubleSP);
    //                 runElevatorTo(ElevatorEncoderConstants.doubleSE);
    //             } else {
    //                 rotateArmTo(ElevatorEncoderConstants.doubleTP);
    //                 runElevatorTo(ElevatorEncoderConstants.doubleTE);
    //             }
    //             break;
    //         case 270: // left
    //             // rest
    //             rotateArmTo(ElevatorEncoderConstants.restE);
    //             runElevatorTo(ElevatorEncoderConstants.restP);
    //             break;
    //         default:
    //             break;
    //     }
    // }



    

    // public void extendArm(double speed) {
    //     elevatorMotors.set(speed);
    // }
    // /**
    //  * Extends the elevator, moving the hand away from the robot.
    //  */
    // public void extendArm() {
    //     extendArm(elevatorExtendScale);
    // }
    // public void retractArm(double speed) {
    //     elevatorMotors.set(-speed);
    // }
    // /**
    //  * Retracts the elevator, bringing the hand towards the robot.
    //  */
    // public void retractArm() {
    //     retractArm(elevatorRetractScale);
    // }

    // /**
    //  * Rotates the elevator arm down with given speed.
    //  * @param speed The speed at which the motor will spin, value from 0 to 1.
    //  */
    // public void rotateArmDown(double speed) {
    //     pulleyMotor.set(speed);
    // }
    // /**
    //  * Rotates the elevator arm down at preset speed.
    //  */
    // public void rotateArmDown() {
    //     rotateArmDown(rotateArmDownScale);
    // }

    // /**
    //  * Rotates the elevator arm up with given speed.
    //  * @param speed The speed at which the motor will spin, value from 0 to 1.
    //  */
    // public void rotateArmUp(double speed) {
    //     pulleyMotor.set(-speed);
    // }
    // /**
    //  * Rotates the elevator arm up at preset speed.
    //  */
    // public void rotateArmUp() {
    //     rotateArmUp(rotateArmUpScale);
    // }



    

    // /**
    //  * Rotates the arm to the specified encoder position.
    //  * @param target The encoder position that the pulley motor will rotate to.
    //  */
    // public void rotateArmTo(double target) {
    //     double difference = target - getPulleyPosition();
    //     double sign = Math.signum(difference);
    //     double differenceAbs = Math.abs(difference);
    //     double minimumDifferenceLevel = 1;
    //     double scaleDifferenceLevel = 30;
    //     double adjustedLevel = scaleDifferenceLevel-minimumDifferenceLevel;
    //     double lowestScaledSpeed = 0.2;
    //     double tempScale = 1;
    //     if (differenceAbs < minimumDifferenceLevel) {
    //         stopPulley();
    //     } else {
    //         if (differenceAbs < scaleDifferenceLevel) {
    //             tempScale = map(differenceAbs, minimumDifferenceLevel, adjustedLevel, lowestScaledSpeed, 1);
    //         }
    //         if (sign == -1) {
    //             rotateArmUp(rotateArmUpScale*tempScale);
    //         } else if (sign == 1) {
    //             rotateArmDown(rotateArmDownScale*tempScale);
    //         } else {
    //             stopPulley();
    //         }
    //     }
    // }
    
    // /**
    //  * Runs the elevator to the specified encoder position.
    //  * @param target The encoder position that the elevator will extend or retract to.
    //  */
    // public void runElevatorTo(double target) {
    //     double difference = target - getElevatorPosition();
    //     double sign = Math.signum(difference);
    //     double differenceAbs = Math.abs(difference);
    //     double minimumDifferenceLevel = 0.5;
    //     double scaleDifferenceLevel = 20;
    //     double adjustedLevel = scaleDifferenceLevel-minimumDifferenceLevel;
    //     double lowestScaledSpeed = 0.2;
    //     double tempScale = 1;
    //     if (differenceAbs < minimumDifferenceLevel) {
    //         stopElevator();
    //     } else {
    //         if (differenceAbs < scaleDifferenceLevel) {
    //             tempScale = map(differenceAbs, minimumDifferenceLevel, adjustedLevel, lowestScaledSpeed, 1);
    //         }
    //         if (sign == -1) {
    //             retractArm(elevatorRetractScale*tempScale);
    //         } else if (sign == 1) {
    //             extendArm(elevatorExtendScale*tempScale);
    //         } else {
    //             stopElevator();
    //         }
    //     }
    // }

    // /**
    //  * Map a double from one range to another.
    //  * @param x Double to map.
    //  * @param a Start of first range.
    //  * @param b End of first range.
    //  * @param c Start of second range.
    //  * @param d End of second range.
    //  * @return The mapped value x.
    //  */
    // public double map(double x, double a, double b, double c, double d) {
    //     return (x-a)/(b-a)*(d-c)+c;
    // }








}