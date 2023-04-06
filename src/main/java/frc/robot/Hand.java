package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Hand implements Sendable {
  public static enum State {Open, Closed, Off}
  public static State currentState = State.Off;

  public static final Value openChannel = Value.kForward;
  public static final Value closeChannel = Value.kReverse;

  public static Compressor pcmCompressor = new Compressor(PneumaticsModuleType.CTREPCM);
  public static DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);

  @Override
  public void initSendable(SendableBuilder builder) {
    //builder.setSmartDashboardType("Hand");
    builder.addStringProperty("Current state", Hand::getNameOfState, null);
  }

  /** Contains code that will be called when the robot is turned on. */
  public static void robotInit() {
    pcmCompressor.enableDigital();
    off();
  }

  /** Sets the solenoid to the off channel. */
  public static void off() {
    solenoid.set(Value.kOff);
    currentState = State.Off;
  }

  /** Sets the solenoid to the open hand channel. */
  public static void open() {
    solenoid.set(openChannel);
    currentState = State.Open;
  }

  /** Sets the solenoid to the close hand channel. */
  public static void close() {
    solenoid.set(closeChannel);
    currentState = State.Closed;
  }

  /**
   * @return A string indicating the name of the current state of the hand solenoid.
   * Possible values are "Open", "Closed", and "Off".
   */
  public static String getNameOfState() {
    switch (currentState) {
      case Open:
        return "Open";
      case Closed:
        return "Closed";
      case Off:
        return "Off";
      default:
        return "Off";
    }
  }

  /**
   * @return Boolean indicating if the hand is closed or not, true being the hand
   * is closed and most likely holding an object.
   */
  public static boolean getIsClosed() {
    return solenoid.get() == closeChannel;
  }
}