package frc.robot;

public class Constants {
    public static final class ElevatorConstants {
        // E indicates elevator (arm extending and retracting)
        // P indicates pulley (arm rotation)
        // S indicates cube (Sqaure)
        // T indicates cone (Triangle)
        public static final double groundP = 210;
        public static final double groundE = 4;
        public static final double quickSP = 13;
        public static final double quickSE = 1.5;
        public static final double restP = 2;
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
        public static final int USBindex = 1;
        /* buttons
         * 1    X
         * 2    A
         * 3    B
         * 4    Y
         * 5    left bumper
         * 6    right bumper
         * 7    left trigger
         * 8    right trigger
         * 9    back
         * 10   start
         * 11   left joystick press
         * 12   right joystick press
         * axes (plural axis)
         * 0    left stick left and right [-1, 1]
         * 1    left stick up and down [-1, 1]
         * 2    right stick left and right [-1, 1]
         * 3    right stick up and down [-1, 1]
         */
        // arcade driving axes
        public static final int arcadeForward = 1;
        public static final int arcadeRotation = 2;
        // elevator motions
        public static final int elevatorExtend = 1;
        public static final int elevatorRetract = 3;
        public static final int rotateArmDownIndex = 4;
        public static final int rotateArmUpIndex = 2;
        // motor idle mode
        public static final int brakingModeIndex = 9;
        // toggle driving control between joysticks and controller
        public static final int toggleDriverControl = 10;
        // open and close hand
        public static final int handOpen = 7;
        public static final int handClose = 8;
        // toggle which object to target, cone or cube
        public static final int toggleTarget = 6;
        // Limelight auto centering 
        public static final int limelightToggle = 12;
        // zero encoder positions
        public static final int zeroEncoders = 11;
    }
    public static final class JoystickConstants {
        public static final int arcadeForwardAxis = 1;
        public static final int arcadeRotationAxis = 0;
        public static final int tankLeftAxis = 1;
        public static final int tankRightAxis = 1;
        public static final int tankToggleButton = 2;
        public static final int reverseToggleButton = 11;
        public static final int limelightMode = 1;
        public static final int limelightToggle = 3;
        public static final int leftUSBindex = 0;
        public static final int rightUSBindex = 2;
        public static final int useJoystickIndex = 7;
    }
}