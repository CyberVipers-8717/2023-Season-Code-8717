package frc.robot;

import java.lang.Math;

import edu.wpi.first.wpilibj.Timer;

public class Lime {
  public static final Timer robotInitTimer = new Timer();
  public static final String[] pipelineNames = {"Reflective tape", "April tags", "Viewing"}; // names to display on dashboard when switching streams
  public static final int maxPipelines = 3; // the number of pipelines that are available to switch through on the limelight

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
    robotInitTimer.start();
  }

  /**
   * @return The name of the currently selected pipeline.
   */
  public static String getCurrentPipeline() {
    return pipelineNames[(int)LimelightHelpers.getCurrentPipelineIndex("limelight")];
  }

  /** Cycles through the available piplines by incrementing the index. */
  public static void incrementPipeline() {
    double currPipe = LimelightHelpers.getCurrentPipelineIndex("limelight");
    if (++currPipe >= maxPipelines) currPipe = 0;
    LimelightHelpers.setPipelineIndex("limelight", (int)currPipe);
  }

  //change these to match desired set up  
  // public static final double reflectiveHeight = 40.0; 
  // public static final double aprilHeight = 24;
  // public static final double cameraHeight = 27; 
  // public static final double cameraAngle = 0; // should be fine 
  // public static final double desiredDistance = 25.0; //adjust this
  public static final double minimumHeadingError = 0.8;
  public static final double controlConstant = 0.1;
  public static final double minCommand = 0.15;
  public static final double maxCommand = 0.7;

  public static boolean getIsTargetFound() {
    boolean targetCheck = LimelightHelpers.getTV("limelight");
    return targetCheck;
  }  

  /**
   * @return A double array of the left and right command for tank drive in order to center the limelight target.
   */
  public static double[] autoCenter() {
    double adjust = 0;
    double headingError = LimelightHelpers.getTX("limelight");
    if (Math.abs(headingError) > minimumHeadingError) {
      double abs = Math.abs(headingError);
      adjust = controlConstant * abs + minCommand;
      adjust = Math.min(adjust, maxCommand);
      adjust = Math.copySign(adjust, headingError);
    }
    return new double[] {-adjust, adjust}; 
  }
}
