package frc.robot;

import java.lang.Math;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lime {
    //change these to match desired set up  
    // public static final double reflectiveHeight = 40.0; 
    // public static final double aprilHeight = 24;
    // public static final double cameraHeight = 27; 
    // public static final double cameraAngle = 0; // should be fine 
    // public static final double desiredDistance = 25.0; //adjust this 
    public static final double controlConstant = -.1;
    public static final double minCommand = .05;  

    public boolean getIsTargetFound() {
        boolean targetCheck = LimelightHelpers.getTV("limelight");
        return targetCheck;
    }  

    public double[] autoCenter() {
        double leftCommand = 0;
        double rightCommand = 0;
        double headingError = LimelightHelpers.getTX("limelight");
        SmartDashboard.putNumber("heading error", headingError);
        double steeringAdjust = 0; 
        if (Math.abs(headingError) > 1) {
            if (headingError < 0) {
                steeringAdjust = controlConstant * headingError + minCommand;
            } else {
                steeringAdjust = controlConstant * headingError - minCommand; 
            }
        }
        leftCommand += steeringAdjust; 
        rightCommand -= steeringAdjust;
        SmartDashboard.putNumber("ster adj", steeringAdjust);
        return new double[] {leftCommand, rightCommand}; 
    }
}
