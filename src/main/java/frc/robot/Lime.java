package frc.robot;

public class Lime {
    //change these to match desired set up  
    public static final double reflectiveHeight = 40.0; 
    public static final double aprilHeight = 24;
    public static final double cameraHeight = 27; 
    public static final double cameraAngle = 0; // should be fine 
    public static final double desiredDistance = 25.0; //adjust this 
    public static final double controlConstant = .03;  

    public boolean getIsTargetFound() {
        boolean targetCheck = LimelightHelpers.getTV("limelight");
        return targetCheck;
    }  

    public double getEstimatedDistance() {
        double tempHeight = reflectiveHeight;
        double targetAngle = LimelightHelpers.getTY("limelight");
        double combinedRad = (targetAngle + cameraAngle) * (Math.PI/180);
        if (LimelightHelpers.getCurrentPipelineIndex("limelight") == 1) tempHeight = aprilHeight;
        double heightDiff = (tempHeight - cameraHeight);
        return heightDiff / Math.tan(combinedRad);
    }

    public double getAdjustedDistance() {
        double estimatedDistance = getEstimatedDistance();
        System.out.println("estimate:"+estimatedDistance);
        double distanceError = desiredDistance - estimatedDistance;
        double distanceAdjust = controlConstant * distanceError;
        System.out.println("adjust:"+distanceAdjust);
        return distanceAdjust;
    }

}
