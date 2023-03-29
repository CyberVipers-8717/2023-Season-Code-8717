package frc.robot;

import frc.robot.Constants.ControllerConstants;

public class Controller {
    public static void run(int button) {
        switch (button) {
            case ControllerConstants.elevatorExtend: // 1
                Robot.elevator.extend(true);
                break;
            case ControllerConstants.rotateArmUpIndex: // 2
                Robot.elevator.rotateDown(false);
                break;
            case ControllerConstants.elevatorRetract: // 3
                Robot.elevator.extend(false);
                break;
            case ControllerConstants.rotateArmDownIndex: // 4
                Robot.elevator.rotateDown(true);
                break;
            case ControllerConstants.toggleTarget: // 6
                Elevator.targetingCube = Elevator.targetingCube ? false : true;
            case ControllerConstants.handOpen: // 7
                Robot.hand.open();
                break;
            case ControllerConstants.handClose: // 8
                Robot.hand.close();
                break;
            case ControllerConstants.brakingModeIndex: // 9  
                Robot.driveTrain.toggleDriveIdle();
                break;
            case ControllerConstants.toggleDriverControl: // 10
                Robot.usingController = Robot.usingController ? false : true;
                break;
            case ControllerConstants.zeroEncoders: // 11
                Robot.elevator.zeroEncoders();
                Robot.driveTrain.zeroDriveEncoders();
                break;
            case ControllerConstants.limelightToggle: // 12
                Lime.incrementPipeline();
                break;
        }
    }
}
