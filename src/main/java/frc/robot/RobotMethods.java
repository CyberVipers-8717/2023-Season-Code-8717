package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.JoystickConstants;

public class RobotMethods {
    public static void manageSmartDashboard() {
        SmartDashboard.putBoolean("Tank Drive", Drivetrain.kTankFlag);
        SmartDashboard.putBoolean("Using Joystick", !Robot.usingController);
        SmartDashboard.putNumber("Elevator", Robot.elevator.getElevatorPosition());
        SmartDashboard.putNumber("Pulley", Robot.elevator.getPulleyPosition());
        SmartDashboard.putBoolean("Hard Braking", Robot.driveTrain.getDriveIdle()==IdleMode.kBrake);
        SmartDashboard.putBoolean("Targeting Cube", Elevator.targetingCube);
        SmartDashboard.putBoolean("Targeting Cone", !Elevator.targetingCube);
        SmartDashboard.putString("Current Pipeline", Lime.getCurrentPipeline());
        SmartDashboard.putNumber("Left Wheel", Robot.driveTrain.getLeftPosition());
        SmartDashboard.putNumber("Right Wheel", Robot.driveTrain.getRightPosition());
    }
    public static void usingController(boolean isUsingController) {
        if (!isUsingController) {
            // drive code
            if (Robot.stickR.getRawButton(JoystickConstants.limelightMode)) {
              double[] modifiedCommands = Robot.limelight.autoCenter();
              Robot.driveTrain.tank(modifiedCommands[0],modifiedCommands[1]);
            } else {
              if (Drivetrain.kTankFlag) {
                double left = Robot.stickL.getRawAxis(JoystickConstants.tankLeftAxis);
                double right = Robot.stickR.getRawAxis(JoystickConstants.tankRightAxis);
                Robot.driveTrain.tank(left, right);
              } else {
                double forward = Robot.stickL.getRawAxis(JoystickConstants.arcadeForwardAxis);
                double rotation = Robot.stickR.getRawAxis(JoystickConstants.arcadeRotationAxis);
                Robot.driveTrain.arcade(forward, rotation);
              }
            }
          } else {
            double forward = Robot.controller.getRawAxis(ControllerConstants.arcadeForward);
            double steering = Robot.controller.getRawAxis(ControllerConstants.arcadeRotation);
            Robot.driveTrain.arcade(forward, steering);
          }
    }
    public static void manualPulley() {
        // manual pulley
        if (Robot.controller.getRawButton(ControllerConstants.rotateArmDownIndex)) Controller.run(4);
        else if (Robot.controller.getRawButton(ControllerConstants.rotateArmUpIndex)) Controller.run(2);
        else Robot.elevator.stopPulley();
    }
    public static void manualElevator() {
        // manual elevator
        if (Robot.controller.getRawButton(ControllerConstants.elevatorExtend)) Controller.run(1);
        else if (Robot.controller.getRawButton(ControllerConstants.elevatorRetract)) Controller.run(3);
        else Robot.elevator.stopElevator();
    }
    public static void usingLimelight() {
        double[] modifiedCommands = Robot.limelight.autoCenter();
        Robot.driveTrain.tank(modifiedCommands[0],modifiedCommands[1]);
    }
    public static void usingTankDrive() {
        double left = Robot.stickL.getRawAxis(JoystickConstants.tankLeftAxis);
        double right = Robot.stickR.getRawAxis(JoystickConstants.tankRightAxis);
        Robot.driveTrain.tank(left, right);
    }
    public static void usingArcadeDrive() {
        double forward = Robot.stickL.getRawAxis(JoystickConstants.arcadeForwardAxis);
        double rotation = Robot.stickR.getRawAxis(JoystickConstants.arcadeRotationAxis);
        Robot.driveTrain.arcade(forward, rotation);
      }
      /**
        * Calculates a scaled speed for a motor to run at in order to reach a desired encoder position.
        * @param diff The difference in the target encoder position and the current encoder position.
        * @param minDiff The minimum difference in encoder positions that the motor will begin to run at.
        * @param scaleDiff The difference at which the motor will begin to scale its speed, this is used so as not to
        * overshoot the desired encoder position since the motor will slow down the closer it gets to its target.
        * @param minCommand The minimum command that will be returned from this method, a motor will not run when its
        * command is extremely small.
        * @param maxCommand The maximum command that will be returned from this method, a motor is supposed to only
        * accept a value between -1 and 1.
        * @return The scaled speed command for the motor to achieve its desired encoder position.
        */
    public static double scaleTempCommand(double diff, double minDiff, double scaleDiff, double minCommand, double maxCommand) {
        double abs = Math.abs(diff);
        if (abs < minDiff) return 0;
        if (abs > scaleDiff) return Math.signum(diff);
        double scaled = (Math.abs(diff)-minDiff)/(scaleDiff-2*minDiff)*(maxCommand-minCommand)+minCommand;
        return Math.copySign(scaled, Math.signum(diff));
    }

        /**
        * Moves the specified motor to the target encoder position.
        * @param target The target encoder position to move the motor to.
        * @param currPos The current encoder position of the motor.
        * @param motor The motor that will be moved.
        * @param minimumDifference The minimum difference in encoder positions that the motor will start running at.
        * @param maximumSpeed The maximum speed to run the motor at.
        * @param whenToScale The difference at which the motor will begin to scale its speed.
        */
    public static void moveMotorTo(double target, double currPos, MotorController motor, double minimumDifference, double maximumSpeed, double whenToScale) {
        double diff = target - currPos;
        motor.set(scaleTempCommand(diff, minimumDifference, whenToScale, 0.2, maximumSpeed));
    }

        /**
        * @param target The target encoder position of the motor.
        * @param currPos The current encoder position of the motor.
        * @param minimumDifference The minium difference in encoder positions that the motor will start running at.
        * @return A boolean indicating if the motor encoder position is within a minumum difference of the target encoder position.
        */
    public static boolean motorAtTarget(double target, double currPos, double minimumDifference) {
        return Math.abs(target - currPos) < minimumDifference;
    }
}