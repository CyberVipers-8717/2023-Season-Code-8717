package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.*;

public class Robot extends TimedRobot {
  public static boolean usingController = false;

  public final Joystick stickL = new Joystick(JoystickConstants.leftUSBindex);
  public final Joystick stickR = new Joystick(JoystickConstants.rightUSBindex);
  public final Joystick controller = new Joystick(ControllerConstants.USBindex);

  public static final Drivetrain driveTrain = new Drivetrain();
  public static final Elevator elevator = new Elevator();
  public static final Lime limelight = new Lime();
  public static final Hand hand = new Hand();

  @Override
  public void robotInit() {
    hand.off();
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("Tank Drive", Drivetrain.kTankFlag);
    SmartDashboard.putBoolean("Using Joystick", !usingController);
    SmartDashboard.putNumber("Elevator", elevator.getElevatorPosition());
    SmartDashboard.putNumber("Pulley", elevator.getPulleyPosition());
    SmartDashboard.putBoolean("Hard Braking", driveTrain.getDriveIdle()==IdleMode.kBrake);
    SmartDashboard.putBoolean("Targeting Cube", Elevator.targetingCube);
    SmartDashboard.putBoolean("Targeting Cone", !Elevator.targetingCube);
    SmartDashboard.putString("Current Pipeline", Lime.getCurrentPipeline());
    SmartDashboard.putNumber("Left Wheel", driveTrain.getLeftPosition());
    SmartDashboard.putNumber("Right Wheel", driveTrain.getRightPosition());
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
    if (controller.getRawButtonPressed(ControllerConstants.toggleDriverControl)) usingController = usingController?false:true;

    // limelight pipeline
    if (controller.getRawButtonPressed(12)) Lime.incrementPipeline();

    // targetting object
    if (controller.getRawButtonPressed(ControllerConstants.toggleTarget)) Elevator.targetingCube = Elevator.targetingCube?false:true;

    // zero encoders
    if (controller.getRawButtonPressed(ControllerConstants.zeroEncoders)) {
      elevator.zeroEncoders();
      driveTrain.zeroDriveEncoders();
    }

    // braking
    if (stickR.getRawButtonPressed(2)) driveTrain.toggleDriveIdle();

    // tank flag
    if (stickL.getRawButtonPressed(JoystickConstants.tankToggleButton)) driveTrain.toggleTankFlag();

    // drive code
    if (stickR.getRawButton(JoystickConstants.limelightMode)) {
      double[] modifiedCommands = limelight.autoCenter();
      driveTrain.tank(modifiedCommands[0],modifiedCommands[1]);
    } else {
      if (Drivetrain.kTankFlag) {
        double left = stickL.getRawAxis(JoystickConstants.tankLeftAxis);
        double right = stickR.getRawAxis(JoystickConstants.tankRightAxis);
        driveTrain.tank(left, right);
      } else {
        double forward = stickL.getRawAxis(JoystickConstants.arcadeForwardAxis);
        double rotation = stickR.getRawAxis(JoystickConstants.arcadeRotationAxis);
        driveTrain.arcade(forward, rotation);
      }
    }

    // auto pulley and elevator
    if (controller.getPOV()==-1) {
      // manual pulley
      if (controller.getRawButton(ControllerConstants.rotateArmDownIndex)) {
        elevator.rotateDown();
      } else if (controller.getRawButton(ControllerConstants.rotateArmUpIndex)) {
        elevator.rotateUp();
      } else {
        elevator.stopPulley();
      }

      // manual elevator
      if (controller.getRawButton(ControllerConstants.elevatorExtend)) {
        elevator.extend();
      } else if (controller.getRawButton(ControllerConstants.elevatorRetract)) {
        elevator.retract();
      } else {
        elevator.stopElevator();
      }
    } else elevator.handlePOV(controller.getPOV());

    // hand
    if (controller.getRawButtonPressed(ControllerConstants.handOpen)) hand.open();
    else if (controller.getRawButtonPressed(ControllerConstants.handClose)) hand.close();
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
   * @param minimumDifference The minium difference in encoder positions that the motor will start running at.
   * @param maximumSpeed The maximum speed to run the motor at.
   * @param whenToScale The difference at which the motor will begin to scale its speed.
   */
  public static void moveMotorTo(double target, double currPos, MotorController motor, double minimumDifference, double maximumSpeed, double whenToScale) {
    double diff = target - currPos;
    motor.set(scaleTempCommand(diff, minimumDifference, whenToScale, 0.2, maximumSpeed));
  }

  /**
   * @param target The target encoder position of the motor.
   * @param currPos The current encodor position of the motor.
   * @param minimumDifference The minium difference in encoder positions that the motor will start running at.
   * @return A boolean indicating if the motor encoder position is within a minumum difference of the target encoder position.
   */
  public static boolean motorAtTarget(double target, double currPos, double minimumDifference) {
    return Math.abs(target - currPos) < minimumDifference;
  }
}