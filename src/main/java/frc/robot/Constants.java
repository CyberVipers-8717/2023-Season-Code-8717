package frc.robot;

public class Constants {
  public static final class AutoConstants {
    public static final String kTargetOne = "cube";
    public static final String kTargetTwo = "cone";
    public static final String kNoTarget = "none";

    public static final String kHeightOne = "high";
    public static final String kHeightTwo = "mid";
    public static final String kHeightThree = "double";
    public static final String kHeightFour = "ground";
    public static final String kHeightFive = "rest";

    public static final String kDefaultMovement = "taxi clear";
    public static final String kAltDefaultMovement = "taxi bump";
    public static final String kAltMovement = "balance";
    public static final String kNoMovement = "none";

    public static final String kDefaultRotation = "none";
    public static final String kAltRotation = "180";
  }
  public static final class ControllerConstants {
    public static final int USB = 0;
    public static final int X = 1;
    public static final int A = 2;
    public static final int B = 3;
    public static final int Y = 4;
    public static final int leftBumper = 5;
    public static final int rightBumper = 6;
    public static final int leftTrigger = 7;
    public static final int rightTrigger = 8;
    public static final int back = 9;
    public static final int start = 10;
    public static final int leftThumb = 11;
    public static final int rightThumb = 12;
    public static final int leftY = 0; // left thumb left and right [-1, 1]
    public static final int leftX = 1; // left thumb up and down [-1, 1]
    public static final int rightY = 2; // right thumb left and right [-1, 1]
    public static final int rightX = 3; // right thumb up and down [-1, 1]
  }
  public static final class JoystickConstants {
    public static final int USBleft = 1;
    public static final int USBright = 2;
    public static final int sideThumb = 2;
  }
}