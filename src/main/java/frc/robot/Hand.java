package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Hand {
  public static final Value openChannel = Value.kForward;
  public static final Value closeChannel = Value.kReverse;

  public Compressor pcmCompressor = new Compressor(PneumaticsModuleType.CTREPCM);
  public DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 1, 0);

  public Hand() {
    pcmCompressor.enableDigital();
  }

  /** Sets the solenoid to the off channel. */
  public void off() {
    solenoid.set(Value.kOff);
  }

  /** Sets the solenoid to the open hand channel. */
  public void open() {
    solenoid.set(openChannel);
  }

  /** Sets the solenoid to the close hand channel. */
  public void close() {
    solenoid.set(closeChannel);
  }

  /**
   * @return Boolean indicating if the hand is closed or not, true being the hand
   * is closed and most likely holding an object.
   */
  public boolean getIsClosed() {
    return solenoid.get() == closeChannel;
  }
}