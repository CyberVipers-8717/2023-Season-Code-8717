package frc.robot;

import edu.wpi.first.wpilibj.Preferences;

public class ArmPresets {
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

  /** Resets the preferences saved on the roboRIO. */
  public static void resetPreferences() {
    Preferences.removeAll();
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
  }

  /** Loads the preferences from the roboRIO. */
  public static void loadPreferences() {
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
  }

  /** Contains code that will be called when teleop is enabled. */
  public static void teleopInit() {
    loadPreferences();
  }

  /**
   * @param item An {@link Arm.Item} to get the preset of.
   * @param height A {@link Arm.Height} to get the preset of.
   * @return A double array containing the presets for the elevator motors and pulley motor
   * with the given item and height. The array is in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getTargetPresets(Arm.Item item, Arm.Height height) {
    if (item == Arm.Item.Cube) {
      switch (height) {
        case High:
          return new double[] {highSE, highSP};
        case Mid:
          return new double[] {midSE, midSP};
        case Ground:
          return new double[] {groundSE, groundSP};
        case Rest:
          return new double[] {restE, restP};
        case Double:
          return new double[] {doubleSE, doubleSP};
        default:
          return new double[] {restE, restP};
      }
    } else if (item == Arm.Item.Cube) {
      switch (height) {
        case High:
          return new double[] {highTE, highTP};
        case Mid:
          return new double[] {midTE, midTP};
        case Ground:
          return new double[] {groundTE, groundTP};
        case Rest:
          return new double[] {restE, restP};
        case Double:
          return new double[] {doubleTE, doubleTP};
        default:
          return new double[] {restE, restP};
      }
    } else {
      return new double[] {restE, restP};
    }
  }

  /**
   * @return A double array containing the presets for the elevator motors and pulley motor.
   * They are in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getTargetPresets() {
    return getTargetPresets(Arm.targetItem, Arm.targetHeight);
  }

  /**
   * @return A string representation of the current target item.
   */
  public static String getNameOfItem() {
    switch (Arm.targetItem) {
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
    switch (Arm.targetHeight) {
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