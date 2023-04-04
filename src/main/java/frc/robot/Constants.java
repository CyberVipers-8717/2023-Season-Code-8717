package frc.robot;

public class Constants {
    public static final class AutoConstants {
        public static final String kDefaultTarget = "cube";
        public static final String kAltTarget = "cone";
        public static final String kNoTarget = "none";

        public static final String kDefaultHeight = "high";
        public static final String kAltHeight = "mid";

        public static final String kDefaultMovement = "taxi clear";
        public static final String kAltDefaultMovement = "taxi bump";
        public static final String kAltMovement = "balance";
        public static final String kNoMovement = "none";

        public static final String kDefaultRotation = "none";
        public static final String kAltRotation = "180";
    }
    public static final class ElevatorConstants {
        // E indicates elevator (arm extending and retracting)
        // P indicates pulley (arm rotation)
        // S indicates cube (Sqaure)
        // T indicates cone (Triangle)
        public static final double groundSP = 210;
        public static final double groundSE = 4;
        public static final double groundTP = 210;
        public static final double groundTE = 4;
        public static final double quickSP = 13;
        public static final double quickSE = 1.5;
        public static final double restP = 5;
        public static final double restE = 1.5;
        // all cube heights
        public static final double highSP = 91.5;
        public static final double highSE = 77.5;
        public static final double midSP = 73.5;
        public static final double midSE = 18.5;
        public static final double doubleSP = 39;
        public static final double doubleSE = 1;
        // all cone heights
        public static final double highTP = 90;
        public static final double highTE = 91;
        public static final double midTP = 74.5;
        public static final double midTE = 41;
        public static final double doubleTP = 39.7;
        public static final double doubleTE = 1;
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