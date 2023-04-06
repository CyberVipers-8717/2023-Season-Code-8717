package frc.robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ElevatorConstants;

/*
 * Todo
 *    Create a custom widget for the presets
 *    instead of two dropdowns and input fields
 * Cleanup
 *    Everything
 */

public class ElevatorPresets /*implements Sendable*/ {
  // E is for elevator encoder position (extension and retraction)
  // P is for pulley encoder position (rotation up and down)
  // S is for cube (square)
  // T is for cone (triangle)
  public static double restE = 1.5;
  public static double restP = 5;
  // all cube heights
  public static double highSE = 77.5;
  public static double highSP = 91.5;
  public static double midSE = 18.5;
  public static double midSP = 73.5;
  public static double doubleSE = 1;
  public static double doubleSP = 39;
  public static double groundSE = 4;
  public static double groundSP = 210;
  // all cone heights
  public static double highTE = 91;
  public static double highTP = 90;
  public static double midTE = 41;
  public static double midTP = 74.5;
  public static double doubleTE = 1;
  public static double doubleTP = 39.7;
  public static double groundTE = 4;
  public static double groundTP = 210;

  private static final String kDefaultTarget = "cube";
  private static final String kAltTarget = "cone";

  private static final String kDefaultHeight = "high";
  private static final String kHeightOne = "mid";
  private static final String kHeightTwo = "double";
  private static final String kHeightThree = "ground";
  private static final String kHeightFour = "rest";

  private static final SendableChooser<String> height_chooser = new SendableChooser<>();
  private static final SendableChooser<String> item_chooser = new SendableChooser<>();
  // private static final SuppliedValueWidget<Double> elevatorPresetEntry = Debugging.DebugTab.addDouble("Elevator", () -> currentElevatorPreset);

  private static String m_height;
  private static String m_item;

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
    Preferences.initDouble("restE", restE);
    Preferences.initDouble("restP", restP);
    Preferences.initDouble("highSE", highSE);
    Preferences.initDouble("highSP", highSP);
    Preferences.initDouble("midSE", midSE);
    Preferences.initDouble("midSP", midSP);
    Preferences.initDouble("doubleSE", doubleSE);
    Preferences.initDouble("doubleSP", doubleSP);
    Preferences.initDouble("groundSE", groundSE);
    Preferences.initDouble("groundSP", groundSP);
    Preferences.initDouble("highTE", highTE);
    Preferences.initDouble("highTP", highTP);
    Preferences.initDouble("midTE", midTE);
    Preferences.initDouble("midTP", midTP);
    Preferences.initDouble("doubleTE", doubleTE);
    Preferences.initDouble("doubleTP", doubleTP);
    Preferences.initDouble("groundTE", groundTE);
    Preferences.initDouble("groundTP", groundTP);

    restE = Preferences.getDouble("restE", restE);
    restP = Preferences.getDouble("restP", restP);
    highSE = Preferences.getDouble("highSE", highSE);
    highSP = Preferences.getDouble("highSP", highSP);
    midSE = Preferences.getDouble("midSE", midSE);
    midSP = Preferences.getDouble("midSP", midSP);
    doubleSE = Preferences.getDouble("doubleSE", doubleSE);
    doubleSP = Preferences.getDouble("doubleSP", doubleSP);
    groundSE = Preferences.getDouble("groundSE", groundSE);
    groundSP = Preferences.getDouble("groundSP", groundSP);
    highTE = Preferences.getDouble("highTE", highTE);
    highTP = Preferences.getDouble("highTP", highTP);
    midTE = Preferences.getDouble("midTE", midTE);
    midTP = Preferences.getDouble("midTP", midTP);
    doubleTE = Preferences.getDouble("doubleTE", doubleTE);
    doubleTP = Preferences.getDouble("doubleTP", doubleTP);
    groundTE = Preferences.getDouble("groundTE", groundTE);
    groundTP = Preferences.getDouble("groundTP", groundTP);

    height_chooser.setDefaultOption("High", kDefaultHeight);
    height_chooser.addOption("Mid", kHeightOne);
    height_chooser.addOption("Double", kHeightTwo);
    height_chooser.addOption("Ground", kHeightThree);
    height_chooser.addOption("Rest", kHeightFour);

    item_chooser.setDefaultOption("Cube", kDefaultTarget);
    item_chooser.addOption("Cone", kAltTarget);

    Debugging.DebugTab.add("Height", height_chooser).withPosition(6, 2).withSize(2, 1);
    Debugging.DebugTab.add("Item", item_chooser).withPosition(6, 3).withSize(2, 1);

    SmartDashboard.putNumber("Elevator preset", 5);
    SmartDashboard.putNumber("Pulley preset", 5);
  }

  public void periodic() {
    m_height = height_chooser.getSelected();
    m_item = item_chooser.getSelected();

    String partOne = m_height;
    String partTwo;
    if (m_height == kHeightFour) {
      partTwo = "";
    } else {
      partTwo = m_item == kDefaultHeight ? "S" : "T";
    }

    Preferences.setDouble(partOne + partTwo + "E", SmartDashboard.getNumber("Elevator preset", 5));
    Preferences.setDouble(partOne + partTwo + "P", SmartDashboard.getNumber("Pulley preset", 5));
  }

  /**
   * @param item An {@link Elevator.Item} to get the preset of.
   * @param height A {@link Elevator.Height} to get the preset of.
   * @return A double array containing the presets for the elevator motors and pulley motor
   * with the given item and height. The array is in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getTargetPresets(Elevator.Item item, Elevator.Height height) {
    if (item == Elevator.Item.Cube) {
      switch (height) {
        case High:
          return new double[] {ElevatorConstants.highSE, ElevatorConstants.highSP};
        case Mid:
          return new double[] {ElevatorConstants.midSE, ElevatorConstants.midSP};
        case Ground:
          return new double[] {ElevatorConstants.groundSE, ElevatorConstants.groundSP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleSE, ElevatorConstants.doubleSP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    } else if (item == Elevator.Item.Cube) {
      switch (height) {
        case High:
          return new double[] {ElevatorConstants.highTE, ElevatorConstants.highTP};
        case Mid:
          return new double[] {ElevatorConstants.midTE, ElevatorConstants.midTP};
        case Ground:
          return new double[] {ElevatorConstants.groundTE, ElevatorConstants.groundTP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleTE, ElevatorConstants.doubleTP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    } else {
      return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
    }
  }

  /**
   * @return A double array containing the presets for the elevator motors and pulley motor.
   * They are in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getTargetPresets() {
    return getTargetPresets(Elevator.targetItem, Elevator.targetHeight);
  }

  /**
   * @return A string representation of the current target item.
   */
  public static String getNameOfItem() {
    switch (Elevator.targetItem) {
      case Cube:
        return "Cube";
      case Cone:
        return "Cone";
      default:
        return "Cube";
    }
  }

  /**
   * @return A string representation of the current target height.
   */
  public static String getNameOfHeight() {
    switch (Elevator.targetHeight) {
      case High:
        return "High";
      case Mid:
        return "Mid";
      case Ground:
        return "Ground";
      case Rest:
        return "Rest";
      case Double:
        return "Double";
      default:
        return "Rest";
    }
  }
}