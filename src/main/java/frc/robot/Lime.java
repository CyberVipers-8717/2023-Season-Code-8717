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
    public static final double controlConstant = 0.1;
    public static final double minCommand = 0.03;  
    public static final double maxCommand = 0.7;

    public boolean getIsTargetFound() {
        boolean targetCheck = LimelightHelpers.getTV("limelight");
        return targetCheck;
    }  

    public double[] autoCenter() {
        double adjust = 0;
        double headingError = LimelightHelpers.getTX("limelight");
        if (Math.abs(headingError) > 1) {
            if (headingError > 0) {
                adjust = controlConstant * headingError + minCommand;
                adjust = Math.min(adjust, maxCommand);
            } else {
                adjust = controlConstant * headingError - minCommand;
                adjust = Math.max(adjust, -maxCommand);
            }
        }

        return new double[] {adjust, -adjust}; 
    }
}
