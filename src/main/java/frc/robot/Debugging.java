package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Debugging {
    public static final ShuffleboardTab DebugTab = Shuffleboard.getTab("Debugging");

    public static void robotInit() {
        DebugTab.add("Elevator Info", new Elevator());
        DebugTab.add("Drivetrain Info", new Drivetrain());
        DebugTab.add("Gyro Info", new Drivetrain.Gyro());
    }
}