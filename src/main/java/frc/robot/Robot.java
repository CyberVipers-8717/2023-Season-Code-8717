package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Constants.*;

public class Robot extends TimedRobot {
  public static boolean usingController = false;

  public final static Joystick stickL = new Joystick(JoystickConstants.leftUSBindex);
  public final static Joystick stickR = new Joystick(JoystickConstants.rightUSBindex);
  public final static Joystick controller = new Joystick(ControllerConstants.USBindex);

  public static final Drivetrain driveTrain = new Drivetrain();
  public static final Elevator elevator = new Elevator();
  public static final Lime limelight = new Lime();
  public static final Hand hand = new Hand();

  @Override
  public void robotInit() {
    Autonomous.initChooser();
    hand.off();
  }

  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void autonomousInit() {
    Autonomous.init();
  }

  @Override
  public void autonomousPeriodic() {
    Autonomous.periodic();
  }

  @Override
  public void teleopInit() {
    usingController = false;
  }

  @Override
  public void teleopPeriodic() {
    driveTrain.diffDrive.feed();

    // manage driver control
    // default is joystick
    if (controller.getRawButtonPressed(ControllerConstants.toggleDriverControl)) Controller.run(10);

    // limelight pipeline
    if (controller.getRawButtonPressed(ControllerConstants.limelightToggle)) Controller.run(12);

    // targetting object
    if (controller.getRawButtonPressed(ControllerConstants.toggleTarget)) Controller.run(6);

    // zero encoders
    if (controller.getRawButtonPressed(11)) 

    // braking
    if (stickR.getRawButtonPressed(2)) driveTrain.toggleDriveIdle();
    if (controller.getRawButtonPressed(ControllerConstants.brakingModeIndex)) Controller.run(9);

    // tank flag
    if (stickL.getRawButtonPressed(JoystickConstants.tankToggleButton)) driveTrain.toggleTankFlag();

    RobotMethods.usingController(usingController);

    // maintain position
    if (stickL.getRawButtonPressed(1)) driveTrain.saveCurrentRobotPosition();
    if (stickL.getRawButton(1)) driveTrain.maintainRobotPosition();

    // auto pulley and elevator
    if (controller.getPOV() == -1) {
      // manual pulley
      RobotMethods.manualPulley();
      // manual elevator
      RobotMethods.manualElevator();
    } else elevator.handlePOV(controller.getPOV());

    // hand
    if (controller.getRawButtonPressed(ControllerConstants.handOpen)) Controller.run(7);
    else if (controller.getRawButtonPressed(ControllerConstants.handClose)) Controller.run(8);
  }

  @Override
  public void disabledInit() {
    driveTrain.defaultFlags();
    driveTrain.stopMotor();
    elevator.stopMotor();
  }

  @Override
  public void disabledPeriodic() {
    driveTrain.stopMotor();
    elevator.stopMotor();
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