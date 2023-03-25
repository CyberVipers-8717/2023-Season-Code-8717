package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import frc.robot.Constants.ElevatorEncoderConstants;
import frc.robot.Constants.ElevatorConstants;

public class Elevator {
    /* Variables */
    public CANSparkMax elevatorMotorL = new CANSparkMax(ElevatorConstants.leftIndex, ElevatorConstants.motorType);
    public CANSparkMax elevatorMotorR = new CANSparkMax(ElevatorConstants.rightIndex, ElevatorConstants.motorType);
    public MotorControllerGroup elevatorMotors = new MotorControllerGroup(elevatorMotorL, elevatorMotorR);
    public CANSparkMax pulleyMotor = new CANSparkMax(ElevatorConstants.pulleyIndex, ElevatorConstants.motorType);
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
    public void POVEncoderConstants(double SP, double SE, double TP, double TE, boolean targetingCube) {
        // code that can be repurposed for 3 of 4 POVs
        if (targetingCube) {
            rotateArmTo(SP);
            runElevatorTo(SE);
        } else {
            rotateArmTo(TP);
            runElevatorTo(TE);
        }
    }
    public void dealWithPOV(int pov, boolean targetingCube) {
        switch (pov) {
            case 0: // up
                // high grid
                POVEncoderConstants(ElevatorEncoderConstants.highSP, ElevatorEncoderConstants.highSE,
                    ElevatorEncoderConstants.highTP, ElevatorEncoderConstants.highTE, targetingCube);
                break;
            case 90: // right
                // mid grid
                POVEncoderConstants(ElevatorEncoderConstants.midSP, ElevatorEncoderConstants.midSE,
                    ElevatorEncoderConstants.midTP, ElevatorEncoderConstants.midTE, targetingCube);
                break;
            case 180: // down
                // double player
                POVEncoderConstants(ElevatorEncoderConstants.doubleSP, ElevatorEncoderConstants.doubleSE,
                    ElevatorEncoderConstants.doubleTP, ElevatorEncoderConstants.doubleTE, targetingCube);
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
    /* add comments here */
    public void extendArm(boolean _extends, double tempScale) {
        elevatorMotors.set(_extends ? tempScale * ElevatorConstants.elevatorExtendScale
            : -tempScale * ElevatorConstants.elevatorExtendScale);
    }
    /**
     * Rotates the elevator arm down with given speed.
     * @param speed The speed at which the motor will spin, value from 0 to 1.
     */
    public void rotateArmDown(boolean isDown, double tempScale) {
        pulleyMotor.set(isDown ? ElevatorConstants.rotateArmDownScale * tempScale
            : ElevatorConstants.rotateArmUpScale * tempScale);
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
        double lowestScaledSpeed = 0.2;
        double tempScale = 1;
        if (differenceAbs < minimumDifferenceLevel) {
            stopPulley();
        } else {
            if (differenceAbs < scaleDifferenceLevel) {
                tempScale = map(differenceAbs, minimumDifferenceLevel, scaleDifferenceLevel-minimumDifferenceLevel, lowestScaledSpeed, 1);
            }
            if (sign == -1) {
                rotateArmDown(false, tempScale);
            } else if (sign == 1) {
                rotateArmDown(true, tempScale);
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
        double lowestScaledSpeed = 0.2;
        double tempScale = 1;
        if (differenceAbs < minimumDifferenceLevel) {
            stopElevator();
        } else {
            if (differenceAbs < scaleDifferenceLevel) {
                tempScale = map(differenceAbs, minimumDifferenceLevel, scaleDifferenceLevel-minimumDifferenceLevel, lowestScaledSpeed, 1);
            }
            if (sign == -1) {
                extendArm(false, tempScale);
            } else if (sign == 1) {
                extendArm(true, tempScale);
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
    double map(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a)*(d-c)+c;
    }
}