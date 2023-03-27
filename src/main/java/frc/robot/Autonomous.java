package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
  public static final Timer autoTimer = new Timer();
  private static final Timer delayTimer = new Timer();

  private static final SendableChooser<String> m_chooser = new SendableChooser<>();
  public static String m_autoSelected;

  private static final double startDelay = 0.5;

  private static final String kDefaultAuto = "score high cube";
  private static final String kAutoOne = "score high cube and balance";
  private static final String kAutoTwo = "score high cube and taxi";

  private static boolean openedHand = false;
  //private static boolean closedHand = false;
  private static boolean scoredHighCube = false;
  //private static boolean scoredHighCone = false;
  //private static boolean scoredMidCube = false;
  //private static boolean scoredMidCone = false;
  private static boolean taxied = false;
  private static boolean balanced = false;
  private static boolean savedPosition = false;

  /** Creates a {@link SendableChooser} and puts it on the {@link SmartDashboard}. */
  public static void initChooser() {
    m_chooser.setDefaultOption("Score high cube", kDefaultAuto);
    m_chooser.addOption("Score high cube and try to balance", kAutoOne);
    m_chooser.addOption("Score high cube and taxi out of community", kAutoTwo);
    SmartDashboard.putData(m_chooser);
  }

  /** Contains code that is run in autonomousInit. */
  public static void init() {
    m_autoSelected = m_chooser.getSelected();

    Robot.driveTrain.zeroDriveEncoders();
    Robot.elevator.zeroEncoders();
    Elevator.targetingCube = true;

    Robot.hand.close();

    resetTimer();
    startTimer();
  }

  /** Contains code that is run in autonomousPeriodic. */
  public static void periodic() {
    Robot.driveTrain.feed();
    if (getTime() < startDelay) return;
    switch (m_autoSelected) {
      case kDefaultAuto:
        scoreHighCube();
        break;
      case kAutoOne:
        if (scoredHighCube) {
          if (wasDelayed(0.5)) {
            if (balanced) {
              if (savedPosition) Robot.driveTrain.maintainRobotPosition();
              else {
                Robot.driveTrain.saveCurrentRobotPosition();
                savedPosition = true;
              }
            } else balanceRobot();
          }
        } else scoreHighCube();
        break;
      case kAutoTwo:
        if (scoredHighCube) {
          if (wasDelayed(0.5)) {
            if (!taxied) taxiRobot();
          }
        } else scoreHighCube();
        break;
      default:
        Robot.driveTrain.stopMotor();
        Robot.elevator.stopMotor();
        break;
    }
  }

  /** Scores a cube at the high grid position. */
  public static void scoreHighCube() {
    if (Robot.elevator.armAtHigh()) {
      if (!openedHand) {
        Robot.hand.open();
        openedHand = true;
        scoredHighCube = true;
        restartDelayTimer();
      }
    } else Robot.elevator.handlePOV(0);
  }

  /** Move the robot to balance on the charging station in a crude way. */
  public static void balanceRobot() {
    if (Robot.driveTrain.tracksAtPosition(41, 41)) {
      if (!balanced) {
        balanced = true;
      }
    } else Robot.driveTrain.moveTracksTo(41, 41);
  }

  /** Taxi the robot to outside the community zone. */
  public static void taxiRobot() {
    Robot.driveTrain.moveTracksTo(50, 50);
  }

  /**
   * @return The current time from the autoTimer.
   */
  public static double getTime() {
    return autoTimer.get();
  }
  
  /** Resets the autoTimer. */
  public static void resetTimer() {
    autoTimer.reset();
  }

  /** Starts the autoTimer. */
  public static void startTimer() {
    autoTimer.start();
  }

  /**
   * @param delayedTime The time the delayTimer will be checked against.
   * @return A boolean indicating if the delayTimer has reached, or exceeded, the indicated delayedTime.
   */
  private static boolean wasDelayed(double delayedTime) {
    return delayTimer.get() >= delayedTime;
  }

  /** Restarts the delayTimer. */
  private static void restartDelayTimer() {
    delayTimer.restart();
  }
}