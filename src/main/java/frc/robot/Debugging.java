package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Debugging {
  public static final ShuffleboardTab DebugTab = Shuffleboard.getTab("Debugging");

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
    DebugTab.add("Elevator Info", new Elevator()).withPosition(3, 0).withSize(3, 2);
    DebugTab.add("Drivetrain Info", new Drivetrain()).withPosition(0, 0).withSize(3, 2);
    DebugTab.add("Gyro Info", new Drivetrain.Gyro()).withPosition(6, 0).withSize(3, 2);
    DebugTab.add("Hand Info", new Hand()).withPosition(0, 2).withSize(3, 1);
  }
}