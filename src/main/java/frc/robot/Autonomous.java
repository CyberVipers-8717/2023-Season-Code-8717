package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.AutoConstants;

/*
 * Todo
 *    Refactor the drivetrain code to just simply be better
 *    make it built different
 */

public class Autonomous {
  private static final Timer delayTimer = new Timer();
  private static final Timer waitingTimer = new Timer();

  public static final ShuffleboardTab AutoTab = Shuffleboard.getTab("Autonomous");

  private static final SendableChooser<String> target = new SendableChooser<>();
  private static final SendableChooser<String> height = new SendableChooser<>();
  private static final SendableChooser<String> movement = new SendableChooser<>();
  private static final SendableChooser<String> rotation = new SendableChooser<>();
  private static final GenericEntry delayStart = AutoTab.add("Delay Start", 0).withWidget(BuiltInWidgets.kNumberSlider)
  .withProperties(Map.of("min", 0, "max", 10)).withPosition(2, 0).getEntry();
  private static String m_movement;
  private static String m_rotation;
  private static double m_delayStart;

  private static int currentStep = 0; // indicates current step in a set action like what part of scoring a high cube you are on
  private static int superStep = 0; // indicates the current sequence like you are getting mobility after just doing the high cube sequence

  private static final double baseDelay = 0.3;
  private static final double maximumTimePassed = 4;

  /**
   * Contains code to be run during robotInit.
   * Initialize all auto choices and put them on the Autonomous tab on the {@link SmartDashboard}.
   * Each chooser is a {@link SendableChooser} with the start delay being a number slider.
   */
  public static void robotInit() {
    target.setDefaultOption("Cube", AutoConstants.kTargetOne);
    target.addOption("Cone", AutoConstants.kTargetTwo);
    target.addOption("None", AutoConstants.kNoTarget);

    height.setDefaultOption("High", AutoConstants.kHeightOne);
    height.addOption("Mid", AutoConstants.kHeightTwo);

    movement.setDefaultOption("Mobility clear side", AutoConstants.kDefaultMovement);
    movement.addOption("Mobility bump side", AutoConstants.kAltDefaultMovement);
    movement.addOption("Balance", AutoConstants.kAltMovement);
    movement.addOption("None", AutoConstants.kNoMovement);

    rotation.setDefaultOption("None", AutoConstants.kDefaultRotation);
    rotation.addOption("180", AutoConstants.kAltRotation);

    AutoTab.add("Target", target).withSize(2, 1).withPosition(2, 1);
    AutoTab.add("Height", height).withSize(2, 1).withPosition(2, 2);
    AutoTab.add("Movement", movement).withSize(2, 1).withPosition(5, 0);
    AutoTab.add("Rotation", rotation).withSize(2, 1).withPosition(5, 1);
  }

  /** Contains code that is run in autonomousInit. */
  public static void init() {
    // target object
    switch (target.getSelected()) {
      case AutoConstants.kTargetOne:
        Elevator.targetCube();
        break;
      case AutoConstants.kTargetTwo:
        Elevator.targetCone();
        break;
      case AutoConstants.kNoTarget:
        Elevator.targetItem = Elevator.Item.None;
        break;
      default:
        Elevator.targetCube();
        break;
    }
    // target height
    switch (height.getSelected()) {
      case AutoConstants.kHeightOne:
        Elevator.targetHigh();
        break;
      case AutoConstants.kHeightTwo:
        Elevator.targetMid();
        break;
      default:
        Elevator.targetHigh();
        break;
    }
    // drivetrain movement
    // switch (movement.getSelected()) {
    //   case AutoConstants.kDefaultMovement:
    //     break;
    //   case AutoConstants.kAltDefaultMovement:
    //     break;
    //   case AutoConstants.kAltMovement:
    //     break;
    //   case AutoConstants.kNoMovement:
    //     break;
    //   default:
    //     // no movement
    //     break;
    // }
    m_movement = movement.getSelected();
    m_rotation = rotation.getSelected();
    m_delayStart = delayStart.getDouble(0);

    Drivetrain.zeroDriveEncoders();
    Drivetrain.Gyro.saveStartingAngle();
    Elevator.zeroEncoders();
    Hand.close();

    delayTimer.start();
    restartWaitingTimer();
  }

  /**
   * Contains the sequences of code that pertain to scoring a game piece
   * during auto at the selected height.
   * @param thisStep An integer indicating this sequence's index in a list of sequences.
   * If you wanted to have the moveArm sequence be executed and then the moveRobot sequence,
   * the thisStep would be 0 as it is the first sequence to be executed.
   */
  private static void moveArm(int thisStep) {
    // deals with target and height
    if (superStep == thisStep) {
      if (timeElapsed(waitingTimer, maximumTimePassed)) {
        restartWaitingTimer();
        currentStep++;
      }
      if (timeElapsed(waitingTimer, baseDelay)) {
        // wait for the baseDelay amount of time between doing things
        if (currentStep == 0) {
          // if we have no target move on to next sequence
          if (Elevator.targetItem == Elevator.Item.None) {
            currentStep = 0;
            superStep++;
          } else {
            currentStep++;
            restartWaitingTimer();
          }
        } else if (currentStep == 1) {
          // move the arm
          if (!Elevator.armAtTargets()) Elevator.runArm();
          else {
            Elevator.stopMotor();
            restartWaitingTimer();
            currentStep++;
          }
        } else if (currentStep == 2) {
          // open the hand
          Hand.open();
          restartWaitingTimer();
          currentStep++;
        } else if (currentStep == 3) {
          // retract the arm
          Elevator.targetRest();
          if (!Elevator.armAtTargets()) Elevator.runArm();
          else {
            Elevator.stopMotor();
            restartWaitingTimer();
            currentStep++;
          }
        } else if (currentStep > 3) {
          restartWaitingTimer();
          currentStep = 0;
          superStep++;
        }
      }
    }
  }

  /**
   * Contains the sequences of code that pertain to moving the robot during auto
   * to either mobility or balance and then rotation if applicable.
   * @param thisStep An integer indicating this sequence's index in a list of sequences.
   * If you wanted to have the moveArm sequence be executed and then the moveRobot sequence,
   * the thisStep would be 0 as it is the first sequence to be executed.
   */
  private static void moveRobot(int thisStep) {
    // deals with robot movement and rotation
    if (superStep == thisStep) {
      if (timeElapsed(waitingTimer, maximumTimePassed)) {
        restartWaitingTimer();
        currentStep++;
      }
      // wait for the baseDelay amount of time between doing things
      if (currentStep == 0 && timeElapsed(waitingTimer, baseDelay)) {
        // move robot
        switch(m_movement) {
          case AutoConstants.kNoMovement:
            // if we have no desired movement distance move on to next sequence
            currentStep = 0;
            superStep++;
            break;
          case AutoConstants.kDefaultMovement:
            if (!Drivetrain.tracksAtPosition(50, 50)) Drivetrain.moveTracksTo(50, 50);
            else {
              restartWaitingTimer();
              currentStep++;
            }
            break;
          case AutoConstants.kAltDefaultMovement:
            if (!Drivetrain.tracksAtPosition(75, 75)) Drivetrain.moveTracksTo(75, 75);
            else {
              restartWaitingTimer();
              currentStep++;
            }
            break;
          case AutoConstants.kAltMovement:
            if (!Drivetrain.tracksAtPosition(40, 40)) Drivetrain.moveTracksTo(40, 40);
            else {
              restartWaitingTimer();
              currentStep++;
            }
            break;
        }
      } else if (currentStep == 1) {
        // save robot pos
        Drivetrain.saveCurrentRobotPosition();
        currentStep++;
      } else if (currentStep == 2) {
        switch(m_movement) {
          case AutoConstants.kDefaultMovement:
            // taxi maintain robot pos for 1 sec
            if (!timeElapsed(waitingTimer, 1)) Drivetrain.maintainRobotPosition();
            else {
              restartWaitingTimer();
              currentStep++;
            }
            break;
          default:
            Drivetrain.maintainRobotPosition();
            break;
        }
      } else if (currentStep == 3 && timeElapsed(waitingTimer, baseDelay)) {
        // rotate robot
        if (m_rotation == AutoConstants.kAltRotation) {
          if (!Drivetrain.Gyro.facingAngle(Drivetrain.Gyro.startingAngle180Offset)) Drivetrain.rotateToCorrectAngle(Drivetrain.Gyro.startingAngle180Offset);
          else {
            restartWaitingTimer();
            currentStep++;
          }
        } else {
          // if we are not rotating after taxi then move on to next sequence
          currentStep = 0;
          superStep++;
        }
        
      } else if (currentStep == 4 && timeElapsed(waitingTimer, baseDelay)) {
        // save robot pos
        Drivetrain.saveCurrentRobotPosition();
        currentStep++;
      } else if (currentStep == 5 && timeElapsed(waitingTimer, baseDelay)) {
        // maintain robot pos
        Drivetrain.maintainRobotPosition();
      } else if (currentStep > 5) {
        restartWaitingTimer();
        currentStep = 0;
        superStep++;
      }
    }
  }

  /** Contains code that is run in autonomousPeriodic. */
  public static void periodic() {
    Drivetrain.feed();

    if (timeElapsed(delayTimer, m_delayStart)) {
      moveArm(0);
      moveRobot(1);
    }
  }

  /**
   * @param timer The Timer to check the time of.
   * @param delayedTime The time the passed in Timer will be checked against.
   * @return A boolean if the passed in Timer has reached or exceed the indicated delayedTime.
   */
  private static boolean timeElapsed(Timer timer, double delayedTime) {
    return timer.get() >= delayedTime;
  }

  /** Restarts the waitingTimer. */
  private static void restartWaitingTimer() {
    waitingTimer.restart();
  }
}