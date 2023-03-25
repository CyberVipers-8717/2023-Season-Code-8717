package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.JoystickConstants;

public class RobotMethods {
    public static void manageSmartDashboard() {
        SmartDashboard.putBoolean("Tank Drive", RobotVars.driveTrain.getTank());
        SmartDashboard.putBoolean("Using Controller", RobotVars.usingController);
        SmartDashboard.putBoolean("Using Joystick", !RobotVars.usingController);
        SmartDashboard.putNumber("Elevator Average", RobotVars.elevator.getAveragePosition());
        SmartDashboard.putNumber("Pulley", RobotVars.elevator.getPulleyPosition()); 
        SmartDashboard.putNumber("Current", RobotVars.pcmCompressor.getCurrent());
        SmartDashboard.putBoolean("Hard Braking", RobotVars.driveTrain.getHardBraking());
        SmartDashboard.putBoolean("Targeting Cube", RobotVars.targetingCube);
        SmartDashboard.putBoolean("Targeting Cone", !RobotVars.targetingCube);
        SmartDashboard.putNumber("Current Stream", RobotVars.limelight.getEntry("getpipe").getDouble(0));
        SmartDashboard.putNumber("Left Wheel", RobotVars.driveTrain.getLeft());
        SmartDashboard.putNumber("Right Wheel", RobotVars.driveTrain.getRight());
    }
    public static void usingController() {
        // tank drive and reverse
        RobotVars.driveTrain.defaultSettings();
        // braking
        if (RobotVars.controller.getRawButtonPressed(ControllerConstants.brakingModeIndex)) Controller.action(ControllerConstants.brakingModeIndex);
        // drive code
        double forward = RobotVars.controller.getRawAxis(ControllerConstants.arcadeForward);
        double rotation = RobotVars.controller.getRawAxis(ControllerConstants.arcadeRotation);
        RobotVars.driveTrain.arcade(forward, rotation);
    }
    public static void usingJoystick() {
        // tank drive and reverse buttons
        if (RobotVars.stickL.getRawButtonPressed(JoystickConstants.tankToggleButton)) RobotVars.driveTrain.toggleTank();
        // tank drive
        if (RobotVars.driveTrain.getTank()) {
            double left = RobotVars.stickL.getRawAxis(JoystickConstants.tankLeftAxis);
            double right = RobotVars.stickR.getRawAxis(JoystickConstants.tankRightAxis);
            RobotVars.driveTrain.tank(left, right);
        }
        // arcade drive
        else {
            double forward = RobotVars.stickL.getRawAxis(JoystickConstants.arcadeForwardAxis);
            double rotation = RobotVars.stickR.getRawAxis(JoystickConstants.arcadeRotationAxis);
            RobotVars.driveTrain.arcade(forward, rotation);
        }
        // braking
        if (RobotVars.stickR.getRawButtonPressed(2)) RobotVars.driveTrain.toggleMotorIdle(); 
    }
    public static void manualPulley() {
        // manual pulley
        if (RobotVars.controller.getRawButton(ControllerConstants.rotateArmDownIndex)) {
            RobotVars.elevator.rotateArmDown(true, 1);
        } else if (RobotVars.controller.getRawButton(ControllerConstants.rotateArmUpIndex)) {
            RobotVars.elevator.rotateArmDown(false, 1);
        } else {
            RobotVars.elevator.stopPulley();
        }
    }
    public static void manualElevator() {
        if (RobotVars.controller.getRawButton(ControllerConstants.elevatorExtend)) {
            Controller.action(ControllerConstants.elevatorExtend);
        } else if (RobotVars.controller.getRawButton(ControllerConstants.elevatorRetract)) {
            Controller.action(ControllerConstants.elevatorRetract);
        } else {
            RobotVars.elevator.stopElevator();
        }
    }
    public static void getHandControllerInput() {
        if (RobotVars.controller.getRawButtonPressed(ControllerConstants.handOpen)) {
            Controller.action(ControllerConstants.handOpen);
        } else if (RobotVars.controller.getRawButtonPressed(ControllerConstants.handClose)) {
            Controller.action(ControllerConstants.handClose);
        }
    }
}
