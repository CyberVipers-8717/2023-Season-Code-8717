package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
  private static final Timer delayTimer = new Timer();

  private static final SendableChooser<String> m_chooser = new SendableChooser<>();
  public static String m_autoSelected;

  private static final String kDefaultAuto = "score high cube";
  private static final String kAutoOne = "score high cube and balance";
  private static final String kAutoTwo = "score high cube and taxi";
  private static final String kAutoThree = "do nothing";

  private static int currentStep = 0; // indicates current step in a set action like what part of scoring a high cube you are on
  private static int superStep = 0; // indicates the current sequence like you are getting mobility after just doing the high cube sequence

  /** Creates a {@link SendableChooser} and puts it on the {@link SmartDashboard}. */
  public static void robotInit() {
    m_chooser.setDefaultOption("Score high cube", kDefaultAuto);
    m_chooser.addOption("Score high cube and balance", kAutoOne);
    m_chooser.addOption("Score high cube and taxi out of community", kAutoTwo);
    m_chooser.addOption("Do nothing :(", kAutoThree);
    SmartDashboard.putData("Auto Chooser", m_chooser);
  }

  /** Contains code that is run in autonomousInit. */
  public static void init() {
    m_autoSelected = m_chooser.getSelected();

    Drivetrain.zeroDriveEncoders();
    Elevator.zeroEncoders();
    Hand.close();

    restartDelayTimer();
  }

  /** Contains code that is run in autonomousPeriodic. */
  public static void periodic() {
    Drivetrain.feed();
    switch (m_autoSelected) {
      case kDefaultAuto:
        Elevator.targetingCube = true;
        scoreHigh(0);
        break;
      case kAutoOne:
        Elevator.targetingCube = true;
        scoreHigh(0);
        balanceRobot(1);
        break;
      case kAutoTwo:
        Elevator.targetingCube = true;
        scoreHigh(0);
        taxiRobot(1);
        break;
      case kAutoThree:
        Drivetrain.stopMotor();
        Elevator.stopMotor();
        break;
      default:
        Drivetrain.stopMotor();
        Elevator.stopMotor();
        break;
    }
  }

  /** Scores a cube at the high grid position. */
  public static void scoreHigh(int thisStep) {
    if (superStep == thisStep) {
      // move arm up
      if (currentStep == 0 && timeElapsed(0.5)) {
        if (!Elevator.armAtHigh()) Elevator.armToHigh();
        else {
          restartDelayTimer();
          currentStep++;
        }
      }
      // open hand
      else if (currentStep == 1 && timeElapsed(0.5)) {
        Hand.open();
        restartDelayTimer();
        currentStep++;
      }
      // move arm down
      else if (currentStep == 2 && timeElapsed(0.5)) {
        if (!Elevator.armAtRest()) Elevator.armToRest();
        else {
          restartDelayTimer();
          currentStep++;
        }
      }
      // close hand
      else if (currentStep == 3 && timeElapsed(0.5)) {
        Hand.close();
        restartDelayTimer();
        currentStep = 0;
        superStep++;
      }
      // done
    }
  }

  /** Move the robot to balance on the charging station in a crude way. */
  public static void balanceRobot(int thisStep) {
    if (superStep == thisStep) {
      // move robot to balance
      if (currentStep == 0 && timeElapsed(0.5)) {
        if (!Drivetrain.tracksAtPosition(41, 41)) Drivetrain.moveTracksTo(41, 41);
        else {
          restartDelayTimer();
          currentStep++;
        }
      }
      // save robot position
      else if (currentStep == 1 && timeElapsed(0.5)) {
        Drivetrain.saveCurrentRobotPosition();
        currentStep++;
      }
      // keep robot at saved position
      if (currentStep == 2) {
        Drivetrain.maintainRobotPosition();
      }
      // done
    }
  }

  /** Taxi the robot to outside the community zone. */
  public static void taxiRobot(int thisStep) {
    if (superStep == thisStep) {
      // move robot outside of community
      if (currentStep == 0 && timeElapsed(0.5)) {
        if (!Drivetrain.tracksAtPosition(50, 50)) Drivetrain.moveTracksTo(50, 50);
        else {
          restartDelayTimer();
          currentStep++;
        }
      }
      // save robot position
      else if (currentStep == 1 && timeElapsed(0.5)) {
        Drivetrain.saveCurrentRobotPosition();
        currentStep++;
      }
      // keep robot at saved position
      if (currentStep == 2) {
        Drivetrain.maintainRobotPosition();
      }
      // done
    }
  }

  /**
   * @param delayedTime The time the delayTimer will be checked against.
   * @return A boolean indicating if the delayTimer has reached, or exceeded, the indicated delayedTime.
   */
  private static boolean timeElapsed(double delayedTime) {
    return delayTimer.get() >= delayedTime;
  }

  /** Restarts the delayTimer. */
  private static void restartDelayTimer() {
    delayTimer.restart();
  }
}