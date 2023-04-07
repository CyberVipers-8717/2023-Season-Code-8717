package frc.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.*;

public class Robot extends TimedRobot {
  public static final Timer robotLifeTimer = new Timer();

  public static double getRobotLifeTime() {
    return robotLifeTimer.get();
  }

  public static final CustomJoystick stickL = new CustomJoystick(JoystickConstants.USBleft);
  public static final CustomJoystick stickR = new CustomJoystick(JoystickConstants.USBright);
  public static final Controller controller = new Controller(ControllerConstants.USB);

  @Override
  public void robotInit() {
    robotLifeTimer.start();
    Drivetrain.robotInit();
    Arm.robotInit();
    Hand.robotInit();
    Autonomous.robotInit();
    Debugging.robotInit();
    Lime.robotInit();
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("Tank Drive", Drivetrain.kTankFlag);
    SmartDashboard.putBoolean("Using Joystick", !Drivetrain.usingController);
    SmartDashboard.putBoolean("Hard Braking", Drivetrain.getDriveIdle()==IdleMode.kBrake);
    SmartDashboard.putBoolean("Targeting Cube", Arm.targetItem == Arm.Item.Cube);
    SmartDashboard.putBoolean("Targeting Cone", Arm.targetItem == Arm.Item.Cone);
    SmartDashboard.putString("Current Pipeline", Lime.getCurrentPipeline());

    // lime auto april tag
    if (getRobotLifeTime() > 25) {
      LimelightHelpers.setPipelineIndex("limelight", 1);
      robotLifeTimer.stop();
      robotLifeTimer.reset();
    }

    // limelight pipeline
    if (controller.getRightThumbPressed()) Lime.incrementPipeline();
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
    Drivetrain.usingController = false;
    Arm.targetCube();
    ArmPresets.loadPreferences();
  }

  @Override
  public void teleopPeriodic() {
    Drivetrain.feed();

    // reset preferences
    if (controller.getLeftBumperPressed()) ArmPresets.resetPreferences();

    // manage driver control
    if (controller.getStartPressed()) Drivetrain.toggleDriverControl();

    // targetting object
    if (controller.getRightBumperPressed()) Arm.toggleTarget();

    // zero encoders
    if (controller.getLeftThumbPressed()) {
      Arm.zeroEncoders();
      Drivetrain.zeroDriveEncoders();
    }

    // braking
    if (stickR.getThumbPressed() || controller.getBackPressed()) Drivetrain.toggleDriveIdle();

    // tank flag
    if (stickL.getThumbPressed()) Drivetrain.toggleTankFlag();

    // drivetrain stuff
    // maintain position
    if (stickL.getTrigger()) Drivetrain.maintainRobotPosition(stickL.getTriggerPressed());
    // align to limelight
    else if (stickR.getTrigger()) Drivetrain.limeAlign();
    else {
      if (!Drivetrain.usingController) {
        // joystick drive
        if (Drivetrain.kTankFlag) Drivetrain.tank(stickL, stickR);
        else Drivetrain.arcade(stickL, stickR);
      } else {
        // controller drive
        Drivetrain.arcade(controller.getArcadeAxes());
      }
    }

    // elevator and pulley
    if (!controller.usingPOV()) {
      // manual
      if (controller.getY()) Arm.rotateDown();
      else if (controller.getA()) Arm.rotateUp();
      else Arm.stopPulley();

      if (controller.getX()) Arm.extend();
      else if (controller.getB()) Arm.retract();
      else Arm.stopElevator();
    } else {
      // cool automatic
      Arm.handlePOV(controller.getPOV());
    }

    // hand
    if (controller.getLeftTriggerPressed()) Hand.open();
    else if (controller.getRightTriggerPressed()) Hand.close();
  }

  @Override
  public void disabledInit() {
    Drivetrain.defaultFlags();
    Drivetrain.stopMotor();
    Arm.stopMotor();
  }

  @Override
  public void disabledPeriodic() {
    Drivetrain.stopMotor();
    Arm.stopMotor();
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {
    Autonomous.init();
  }

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
    if (abs > scaleDiff) return Math.signum(diff)*maxCommand;
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
    motor.set(scaleTempCommand(diff, minimumDifference, whenToScale, 0.05, maximumSpeed));
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