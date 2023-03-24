package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Drivetrain {
    /* Constants */
    public static final int leftBackIndex = 1;
    public static final int leftFrontIndex = 2;
    public static final int rightFrontIndex = 3;
    public static final int rightBackIndex = 4;
    public static final MotorType driveMotorType = MotorType.kBrushless;
    public static final double arcadeRotationScale = 0.5;
    public static final double arcadeForwardScale = 0.85;
    public static final double tankScale = 0.75;

    /* Variables */
    public boolean robotInTank = false;
    public boolean robotInReverse = false;
    public CANSparkMax motorLB = new CANSparkMax(leftBackIndex, driveMotorType);
    public CANSparkMax motorLF = new CANSparkMax(leftFrontIndex, driveMotorType);
    public CANSparkMax motorRF = new CANSparkMax(rightFrontIndex, driveMotorType);
    public CANSparkMax motorRB = new CANSparkMax(rightBackIndex, driveMotorType);
    public MotorControllerGroup leftMotors = new MotorControllerGroup(motorLB, motorLF);
    public MotorControllerGroup rightMotors = new MotorControllerGroup(motorRB, motorRF);
    public RelativeEncoder leftEncoder;
    public RelativeEncoder rightEncoder;
    public DifferentialDrive diffDrive = new DifferentialDrive(leftMotors, rightMotors);

    /** Creates a custom Drivetrain object with better managed methods :). */
    public Drivetrain() {
        leftMotors.setInverted(true);
        setMotorIdle(IdleMode.kBrake);
        leftEncoder = motorLB.getEncoder();
        rightEncoder = motorRB.getEncoder();
    }

    /** Return drive and reverse flags to default settings. */
    public void defaultSettings() {
        setTank(false);
        setReverse(false);
    }

    public double getLeft() {
        return leftEncoder.getPosition();
    }

    public double getRight() {
        return rightEncoder.getPosition();
    }

    public void zeroEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void moveWheelsTo(double left, double right) {
        moveLeftWheelTo(left);
        moveRightWheelTo(right);
    }

    public void moveLeftWheelTo(double target) {
        double diff = target - getLeft(); 
        double sign = Math.signum(diff);
        double absDiff = Math.abs(diff);
        double minDiff = 0.5;
        double scaleDiff = 5;
        double adjusted = scaleDiff - minDiff;
        double lowest = 0.2;
        double temp = 1;
        if (absDiff < minDiff) {
            leftMotors.stopMotor();
        } else {
            if (absDiff < scaleDiff) {
                temp = map(absDiff, minDiff, adjusted, lowest, 1);
            }
            if (sign == -1) {
                leftMotors.set(0.175*temp);
            } else if (sign == 1) {
                leftMotors.set(-0.175*temp);
            } else {
                leftMotors.stopMotor();
            }
        }
    }

    public void moveRightWheelTo(double target) {
        double diff = target - getRight();
        double sign = Math.signum(diff);
        double absDiff = Math.abs(diff);
        double minDiff = 0.5;
        double scaleDiff = 5;
        double adjusted = scaleDiff - minDiff;
        double lowest = 0.2;
        double temp = 1;
        if (absDiff < minDiff) {
            rightMotors.stopMotor();
        } else {
            if (absDiff < scaleDiff) {
                temp = map(absDiff, minDiff, adjusted, lowest, 1);
            }
            if (sign == -1) {
                rightMotors.set(-0.175*temp);
            } else if (sign == 1) {
                rightMotors.set(0.175*temp);
            } else {
                rightMotors.stopMotor();
            }
        }
    }

    public double map(double x, double a, double b, double c, double d) {
        return (x-a)/(b-a)*(d-c)+c; // (absDiff - minDiff)/(adjusted - minDiff)*(1 - lowest)+ lowest -- limiter
    }

    /**
     * Calls the {@link DifferentialDrive}'s arcadeDrive.
     * 
     * @param forward The robot's speed along the X axis. Forward is positive.
     * @param rotation The robot's rotation rate around the Z axis. Counterclockwise is positive.
     */
    public void arcade(double forward, double rotation) {
        forward *= arcadeForwardScale;
        rotation *= arcadeRotationScale;
        if (robotInReverse) diffDrive.arcadeDrive(-forward, rotation);
        else diffDrive.arcadeDrive(forward, rotation);
    }

    /**
     * Calls the {@link DifferentialDrive}'s tankDrive.
     * 
     * @param left The robot's left side speed along the X axis. Forward is positive.
     * @param right The robot's right side speed along the X axis. Forward is positive.
     */
    public void tank(double left, double right) {
        left *= tankScale;
        right *= tankScale;
        if (robotInReverse) diffDrive.tankDrive(-right, -left);
        else diffDrive.tankDrive(left, right);
    }
    public boolean getTank() {
        return robotInTank;
    }

    /**
     * Calls the {@link DifferentialDrive}'s arcadeDrive or tankDrive
     * method depending on the {@link Drivetrain}'s drivingMode parameter.
     * 
     * @param first Depending on the drivingMode parameter, this will either be the forward speed or left side speed.
     * @param second Depending on the drivingMode parameter, this will eiether be the rotation speed or right side speed.
     */
    public void generic(double first, double second) {
        if (robotInTank) this.tank(first, second);
        else this.arcade(first, second);
    }

    /**
     * Sets all {@link CANSparkMax} motors' idleMode.
     * @param mode The {@link IdleMode} that the motors will be set to, either kBrake or kCoast.
     */
    public void setMotorIdle(IdleMode mode) {
        motorLB.setIdleMode(mode);
        motorLF.setIdleMode(mode);
        motorRF.setIdleMode(mode);
        motorRB.setIdleMode(mode);
    }

    /**
     * @return The {@link IdleMode} that the motors are currently set to.
     */
    public IdleMode getMotorIdle() {
        return motorLB.getIdleMode();
    }

    public void toggleMotorIdle() {
        if (getMotorIdle() == IdleMode.kBrake) setMotorIdle(IdleMode.kCoast);
        else setMotorIdle(IdleMode.kBrake);
    }

    public boolean getHardBraking() {
        return getMotorIdle() == IdleMode.kBrake;
    }
    public void setTank(boolean val) {
        robotInTank = val;
    }

    public void toggleTank() {
        robotInTank = robotInTank?false:true;
    }

    public void setReverse(boolean rev) {
        robotInReverse = rev;
    }

    public void toggleReverse() {
        robotInReverse = robotInReverse?false:true;
    }

    /**
     * Stops all motor movement. Motors can be moved again by calling drive methods.
     */
    public void stopMotors() {
        diffDrive.stopMotor();
    }
}