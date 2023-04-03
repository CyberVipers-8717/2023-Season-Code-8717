package frc.robot;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Debugging {
    public static final ShuffleboardTab DebugTab = Shuffleboard.getTab("Debugging");
    public static final GenericEntry elevatorEncoderEntry = DebugTab.add("Elevator", 0).withPosition(1, 0).getEntry();
    public static final GenericEntry pulleyEncoderEntry = DebugTab.add("Pulley", 0).withPosition(2, 0).getEntry();
    public static final GenericEntry leftWheelsEncoderEntry = DebugTab.add("Left wheels", 0).withPosition(1, 1).getEntry();
    public static final GenericEntry rightWheelsEncoderEntry = DebugTab.add("Right wheels", 0).withPosition(2, 1).getEntry();
    public static final GenericEntry startingAngleEntry = DebugTab.add("Starting angle", 0).withPosition(1, 2).getEntry();
    public static final GenericEntry currentAngleEntry = DebugTab.add("Current angle", 0).withPosition(2, 2).getEntry();

    public static void robotInit() {
        startingAngleEntry.setDouble(Drivetrain.startingAngle);
    }

    public static void periodic() {
        elevatorEncoderEntry.setDouble(Elevator.getElevatorPosition());
        pulleyEncoderEntry.setDouble(Elevator.getPulleyPosition());
        leftWheelsEncoderEntry.setDouble(Drivetrain.getLeftPosition());
        rightWheelsEncoderEntry.setDouble(Drivetrain.getRightPosition());
        currentAngleEntry.setDouble(Drivetrain.getAngle());
    }
}