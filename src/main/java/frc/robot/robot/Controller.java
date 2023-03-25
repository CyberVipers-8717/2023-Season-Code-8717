package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants.ControllerConstants;

public class Controller {
    public static void action(int button) {
        switch (button) {
            case ControllerConstants.elevatorExtend: // 1
                RobotVars.elevator.extendArm(true, 1);
                break;
            case ControllerConstants.rotateArmDownIndex: // 2
                RobotVars.elevator.rotateArmDown(true, 1);
                break;
            case ControllerConstants.elevatorRetract: // 3
                RobotVars.elevator.extendArm(false, 1);
                break;
            case ControllerConstants.rotateArmUpIndex: // 4
                RobotVars.elevator.rotateArmDown(false, 1);
                break;
            case ControllerConstants.targetCone: // 5
                RobotVars.targetingCube = false;
                break;
            case ControllerConstants.targetCube: // 6
                RobotVars.targetingCube = true;
                break;
            case ControllerConstants.handOpen: // 7
                RobotVars.handSolenoid.set(Value.kForward);
                break;
            case ControllerConstants.handClose: // 8
                RobotVars.handSolenoid.set(Value.kReverse);
                break;
            case ControllerConstants.brakingModeIndex: // 9
                RobotVars.driveTrain.toggleMotorIdle();
                break;
            case ControllerConstants.toggleDriverControl: // 10
                RobotVars.usingController = RobotVars.usingController ? false : true;
                break;
            case ControllerConstants.zeroEncoders: // 11
                RobotVars.elevator.zeroEncoders();
                RobotVars.driveTrain.zeroEncoders();
                break;
            case 12: // not totally sure what this is for?
                RobotVars.driveTrain.moveWheelsTo(-42, 42);
                break;
            default:
                break;
        }
    }
}
