package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.JoystickConstants;

public class RobotVars {
    public static Joystick stickL = new Joystick(JoystickConstants.leftUSBindex);
    public static Joystick stickR = new Joystick(JoystickConstants.rightUSBindex);
    public static Joystick controller = new Joystick(ControllerConstants.USBindex);
    public static boolean usingController = false;
    public static boolean targetingCube = true;
    public static Drivetrain driveTrain = new Drivetrain();
    public static Elevator elevator = new Elevator();
    public static Compressor pcmCompressor = new Compressor(PneumaticsModuleType.CTREPCM);
    public static DoubleSolenoid handSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);
    public static int maxPipelines = 3;
    public static NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
}
