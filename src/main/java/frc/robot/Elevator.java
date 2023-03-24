package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorEncoderConstants;

public class Elevator {
    /* Constants */
    public static final int pulleyIndex = 5;
    public static final int leftIndex = 6;
    public static final int rightIndex = 7;
    public static final MotorType motorType = MotorType.kBrushless;
    public static final double rotateArmDownScale = 0.5;
    public static final double rotateArmUpScale = 1;
    public static final double elevatorExtendScale = 0.35;
    public static final double elevatorRetractScale = 0.35;

    /* Variables */
    public CANSparkMax elevatorMotorL = new CANSparkMax(leftIndex, motorType);
    public CANSparkMax elevatorMotorR = new CANSparkMax(rightIndex, motorType);
    public MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
    public CANSparkMax pulleyMotor = new CANSparkMax(pulleyIndex, motorType);
    public RelativeEncoder pulleyEncoder = pulleyMotor.getEncoder();
    public RelativeEncoder elevatorEncoderL = elevatorMotorL.getEncoder();
    public RelativeEncoder elevatorEncoderR = elevatorMotorR.getEncoder();

    public Elevator() {
        elevatorMotorL.setInverted(true);
        pulleyMotor.setIdleMode(IdleMode.kBrake);
    }

    /**
     * Runs the elevator and pulley to move to the set encoder positions for cones and cubes.
     * @param pov The POV angle from the controller.
     */
    public void dealWithPOV(int pov, boolean targetingCube) {
        switch (pov) {
            case 0: // up
                // high grid
                if (targetingCube) {
                    rotateArmTo(ElevatorEncoderConstants.highSP);
                    runElevatorTo(ElevatorEncoderConstants.highSE);
                } else {
                    rotateArmTo(ElevatorEncoderConstants.highTP);
                    runElevatorTo(ElevatorEncoderConstants.highTE);
                }
                break;
            case 90: // right
                // mid grid
                if (targetingCube) {
                    rotateArmTo(ElevatorEncoderConstants.midSP);
                    runElevatorTo(ElevatorEncoderConstants.midSE);
                } else {
                    rotateArmTo(ElevatorEncoderConstants.midTP);
                    runElevatorTo(ElevatorEncoderConstants.midTE);
                }
                break;
            case 180: // down
                // double player
                if (targetingCube) {
                    rotateArmTo(ElevatorEncoderConstants.doubleSP);
                    runElevatorTo(ElevatorEncoderConstants.doubleSE);
                } else {
                    rotateArmTo(ElevatorEncoderConstants.doubleTP);
                    runElevatorTo(ElevatorEncoderConstants.doubleTE);
                }
                break;
            case 270: // left
                // rest
                rotateArmTo(ElevatorEncoderConstants.restE);
                runElevatorTo(ElevatorEncoderConstants.restP);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the encoder positions of the pulley and elevator to 0.
     */
    public void zeroEncoders() {
        elevatorEncoderL.setPosition(0);
        elevatorEncoderR.setPosition(0);
        pulleyEncoder.setPosition(0);
    }

    public void extendArm(double speed) {
        elevatorMotors.set(speed);
    }
    /**
     * Extends the elevator, moving the hand away from the robot.
     */
    public void extendArm() {
        extendArm(elevatorExtendScale);
    }
    public void retractArm(double speed) {
        elevatorMotors.set(-speed);
    }
    /**
     * Retracts the elevator, bringing the hand towards the robot.
     */
    public void retractArm() {
        retractArm(elevatorRetractScale);
    }

    /**
     * Rotates the elevator arm down with given speed.
     * @param speed The speed at which the motor will spin, value from 0 to 1.
     */
    public void rotateArmDown(double speed) {
        pulleyMotor.set(speed);
    }
    /**
     * Rotates the elevator arm down at preset speed.
     */
    public void rotateArmDown() {
        rotateArmDown(rotateArmDownScale);
    }

    /**
     * Rotates the elevator arm up with given speed.
     * @param speed The speed at which the motor will spin, value from 0 to 1.
     */
    public void rotateArmUp(double speed) {
        pulleyMotor.set(-speed);
    }
    /**
     * Rotates the elevator arm up at preset speed.
     */
    public void rotateArmUp() {
        rotateArmUp(rotateArmUpScale);
    }

    /** Calls the stopMotor method of the elevator motors. */
    public void stopElevator() {
        elevatorMotors.stopMotor();
    }
    /** Calls the stopMotor method of the pulley. */
    public void stopPulley() {
        pulleyMotor.stopMotor();
    }
    /**
     * Calls the stopMotor method of the elevator motors and pulley.
     */
    public void stopAllMotors() {
        elevatorMotors.stopMotor();
        pulleyMotor.stopMotor();
    }

    /**
     * @return Position of encoder for left elevator motor.
     */
    public double getLeftPosition() {
        return elevatorEncoderL.getPosition();
    }
    /**
     * @return Position of encoder for right elevator motor.
     */
    public double getRightPosition() {
        return elevatorEncoderR.getPosition();
    }
    public double getAveragePosition() {
        return (getLeftPosition() + getRightPosition()) / 2;
    }
    /**
     * @return Posotion of encoder for pulley motor.
     */
    public double getPulleyPosition() {
        return pulleyEncoder.getPosition();
    }

    /**
     * Rotates the arm to the specified encoder position.
     * @param target The encoder position that the pulley motor will rotate to.
     */
    public void rotateArmTo(double target) {
        double difference = target - getPulleyPosition();
        double sign = Math.signum(difference);
        double differenceAbs = Math.abs(difference);
        double minimumDifferenceLevel = 1;
        double scaleDifferenceLevel = 30;
        double adjustedLevel = scaleDifferenceLevel-minimumDifferenceLevel;
        double lowestScaledSpeed = 0.2;
        double tempScale = 1;
        if (differenceAbs < minimumDifferenceLevel) {
            stopPulley();
        } else {
            if (differenceAbs < scaleDifferenceLevel) {
                tempScale = map(differenceAbs, minimumDifferenceLevel, adjustedLevel, lowestScaledSpeed, 1);
            }
            if (sign == -1) {
                rotateArmUp(rotateArmUpScale*tempScale);
            } else if (sign == 1) {
                rotateArmDown(rotateArmDownScale*tempScale);
            } else {
                stopPulley();
            }
        }
    }
    
    /**
     * Runs the elevator to the specified encoder position.
     * @param target The encoder position that the elevator will extend or retract to.
     */
    public void runElevatorTo(double target) {
        double difference = target - getAveragePosition();
        double sign = Math.signum(difference);
        double differenceAbs = Math.abs(difference);
        double minimumDifferenceLevel = 0.5;
        double scaleDifferenceLevel = 20;
        double adjustedLevel = scaleDifferenceLevel-minimumDifferenceLevel;
        double lowestScaledSpeed = 0.2;
        double tempScale = 1;
        if (differenceAbs < minimumDifferenceLevel) {
            stopElevator();
        } else {
            if (differenceAbs < scaleDifferenceLevel) {
                tempScale = map(differenceAbs, minimumDifferenceLevel, adjustedLevel, lowestScaledSpeed, 1);
            }
            if (sign == -1) {
                retractArm(elevatorRetractScale*tempScale);
            } else if (sign == 1) {
                extendArm(elevatorExtendScale*tempScale);
            } else {
                stopElevator();
            }
        }
    }

    /**
     * Map a double from one range to another.
     * @param x Double to map.
     * @param a Start of first range.
     * @param b End of first range.
     * @param c Start of second range.
     * @param d End of second range.
     * @return The mapped value x.
     */
    public double map(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a)*(d-c)+c;
    }
}