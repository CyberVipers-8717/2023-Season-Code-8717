/*  Robot todo list
 * 
 *    Auto
 *        Add gyro to robot and the code
 *        Use pathweaver
 * 
 *    Limelight
 *        Put limelight on robot and add code
 * 
 */

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Constants.*;

public class Robot extends TimedRobot {
  @Override
  public void robotInit() {
    RobotVars.pcmCompressor.enableDigital();
  }

  @Override
  public void robotPeriodic() {
    RobotMethods.manageSmartDashboard();
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    RobotVars.usingController = false;
  }

  @Override
  public void teleopPeriodic() {
    // manage driver control
    // default is joystick
    if (RobotVars.controller.getRawButtonPressed(ControllerConstants.toggleDriverControl)) Controller.action(ControllerConstants.toggleDriverControl);

    /*if (controller.getRawButtonPressed(12)) {
      double currPipe = limelight.getEntry("getpipe").getDouble(0);
      if (currPipe == maxPipelines-1) limelight.getEntry("pipeline").setNumber(0);
      else limelight.getEntry("pipeline").setNumber(currPipe+1);
    }*/
    if (RobotVars.controller.getRawButton(12)) {
      Controller.action(12);
      return;
    }

    // targetting object
    if (RobotVars.controller.getRawButtonPressed(ControllerConstants.targetCube)) Controller.action(ControllerConstants.targetCube);
    if (RobotVars.controller.getRawButtonPressed(ControllerConstants.targetCone)) Controller.action(ControllerConstants.targetCone);
    // zero encoders
    if (RobotVars.controller.getRawButtonPressed(ControllerConstants.zeroEncoders)) {
      Controller.action(ControllerConstants.zeroEncoders);
    }

    // controller drive code
    if (RobotVars.usingController) {
      RobotMethods.usingController();
    }

    // joystick code
    else {
      RobotMethods.usingJoystick();
    }

    // manual pulley code
    RobotMethods.manualPulley();

    // manual elevator
    RobotMethods.manualElevator();

    // auto pulley and elevator
    RobotVars.elevator.dealWithPOV(RobotVars.controller.getPOV(), RobotVars.targetingCube);

    // hand
    RobotMethods.getHandControllerInput();
  }

  @Override
  public void disabledInit() {
    RobotVars.targetingCube = true;
    RobotVars.driveTrain.defaultSettings();
    RobotVars.driveTrain.stopMotors();
    RobotVars.elevator.stopAllMotors();
  }

  @Override
  public void disabledPeriodic() {
    RobotVars.driveTrain.stopMotors();
    RobotVars.elevator.stopAllMotors();
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}