package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
  private Joystick controller;
  public Controller(int port) {
    controller = new Joystick(port);
  }

  public double[] getArcadeAxes() {
    double forward = getLeftX();
    double rotation = getRightY();
    return new double[] {forward, rotation};
  }

  public double[] getTankAxes() {
    double left = getLeftX();
    double right = getRightX();
    return new double[] {left, right};
  }

  public double[] getDriveAxes() {
    if (!Drivetrain.kTankFlag) return getArcadeAxes();
    return getTankAxes();
  }

  public int getPOV() {
    return controller.getPOV();
  }

  public boolean usingPOV() {
    return getPOV() != -1;
  }

  public double getRawAxis(int axis) {
    return controller.getRawAxis(axis);
  }

  public double getLeftX() {
    return controller.getRawAxis(1);
  }

  public double getLeftY() {
    return controller.getRawAxis(0);
  }

  public double getRightX() {
    return controller.getRawAxis(3);
  }

  public double getRightY() {
    return controller.getRawAxis(2);
  }

  public boolean getRawButton(int button) {
    return controller.getRawButton(button);
  }

  public boolean getRawButtonPressed(int button) {
    return controller.getRawButtonPressed(button);
  }

  public boolean getRawButtonReleased(int button) {
    return controller.getRawButtonReleased(button);
  }

  public boolean getX() {
    return getRawButton(1);
  }

  public boolean getXPressed() {
    return getRawButtonPressed(1);
  }

  public boolean getXReleased() {
    return getRawButtonReleased(1);
  }

  public boolean getA() {
    return getRawButton(2);
  }

  public boolean getAPressed() {
    return getRawButtonPressed(2);
  }

  public boolean getAReleased() {
    return getRawButtonReleased(2);
  }

  public boolean getB() {
    return getRawButton(3);
  }

  public boolean getBPressed() {
    return getRawButtonPressed(3);
  }

  public boolean getBReleased() {
    return getRawButtonReleased(3);
  }

  public boolean getY() {
    return getRawButton(4);
  }

  public boolean getYPressed() {
    return getRawButtonPressed(4);
  }

  public boolean getYReleased() {
    return getRawButtonReleased(4);
  }

  public boolean getLeftBumper() {
    return getRawButton(5);
  }

  public boolean getLeftBumperPressed() {
    return getRawButtonPressed(5);
  }

  public boolean getLeftBumperReleased() {
    return getRawButtonReleased(5);
  }

  public boolean getRightBumper() {
    return getRawButton(6);
  }

  public boolean getRightBumperPressed() {
    return getRawButtonPressed(6);
  }

  public boolean getRightBumperReleased() {
    return getRawButtonReleased(6);
  }

  public boolean getLeftTrigger() {
    return getRawButton(7);
  }

  public boolean getLeftTriggerPressed() {
    return getRawButtonPressed(7);
  }

  public boolean getLeftTriggerReleased() {
    return getRawButtonReleased(7);
  }

  public boolean getRightTrigger() {
    return getRawButton(8);
  }

  public boolean getRightTriggerPressed() {
    return getRawButtonPressed(8);
  }

  public boolean getRightTriggerReleased() {
    return getRawButtonReleased(8);
  }

  public boolean getBack() {
    return getRawButton(9);
  }

  public boolean getBackPressed() {
    return getRawButtonPressed(9);
  }

  public boolean getBackReleased() {
    return getRawButtonReleased(9);
  }

  public boolean getStart() {
    return getRawButton(10);
  }

  public boolean getStartPressed() {
    return getRawButtonPressed(10);
  }

  public boolean getStartReleased() {
    return getRawButtonReleased(10);
  }

  public boolean getLeftThumb() {
    return getRawButton(11);
  }

  public boolean getLeftThumbPressed() {
    return getRawButtonPressed(11);
  }

  public boolean getLeftThumbReleased() {
    return getRawButtonReleased(11);
  }

  public boolean getRightThumb() {
    return getRawButton(12);
  }

  public boolean getRightThumbPressed() {
    return getRawButtonPressed(12);
  }

  public boolean getRightThumbReleased() {
    return getRawButtonReleased(12);
  }
}