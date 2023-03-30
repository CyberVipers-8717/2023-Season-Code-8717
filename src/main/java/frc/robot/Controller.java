package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants.ControllerConstants;

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
    return controller.getRawAxis(ControllerConstants.leftX);
  }

  public double getLeftY() {
    return controller.getRawAxis(ControllerConstants.leftY);
  }

  public double getRightX() {
    return controller.getRawAxis(ControllerConstants.rightX);
  }

  public double getRightY() {
    return controller.getRawAxis(ControllerConstants.rightY);
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
    return getRawButton(ControllerConstants.X);
  }

  public boolean getXPressed() {
    return getRawButtonPressed(ControllerConstants.X);
  }

  public boolean getXReleased() {
    return getRawButtonReleased(ControllerConstants.X);
  }

  public boolean getA() {
    return getRawButton(ControllerConstants.A);
  }

  public boolean getAPressed() {
    return getRawButtonPressed(ControllerConstants.A);
  }

  public boolean getAReleased() {
    return getRawButtonReleased(ControllerConstants.A);
  }

  public boolean getB() {
    return getRawButton(ControllerConstants.B);
  }

  public boolean getBPressed() {
    return getRawButtonPressed(ControllerConstants.B);
  }

  public boolean getBReleased() {
    return getRawButtonReleased(ControllerConstants.B);
  }

  public boolean getY() {
    return getRawButton(ControllerConstants.Y);
  }

  public boolean getYPressed() {
    return getRawButtonPressed(ControllerConstants.Y);
  }

  public boolean getYReleased() {
    return getRawButtonReleased(ControllerConstants.Y);
  }

  public boolean getLeftBumper() {
    return getRawButton(ControllerConstants.leftBumper);
  }

  public boolean getLeftBumperPressed() {
    return getRawButtonPressed(ControllerConstants.leftBumper);
  }

  public boolean getLeftBumperReleased() {
    return getRawButtonReleased(ControllerConstants.leftBumper);
  }

  public boolean getRightBumper() {
    return getRawButton(ControllerConstants.rightBumper);
  }

  public boolean getRightBumperPressed() {
    return getRawButtonPressed(ControllerConstants.rightBumper);
  }

  public boolean getRightBumperReleased() {
    return getRawButtonReleased(ControllerConstants.rightBumper);
  }

  public boolean getLeftTrigger() {
    return getRawButton(ControllerConstants.leftTrigger);
  }

  public boolean getLeftTriggerPressed() {
    return getRawButtonPressed(ControllerConstants.leftTrigger);
  }

  public boolean getLeftTriggerReleased() {
    return getRawButtonReleased(ControllerConstants.leftTrigger);
  }

  public boolean getRightTrigger() {
    return getRawButton(ControllerConstants.rightTrigger);
  }

  public boolean getRightTriggerPressed() {
    return getRawButtonPressed(ControllerConstants.rightTrigger);
  }

  public boolean getRightTriggerReleased() {
    return getRawButtonReleased(ControllerConstants.rightTrigger);
  }

  public boolean getBack() {
    return getRawButton(ControllerConstants.back);
  }

  public boolean getBackPressed() {
    return getRawButtonPressed(ControllerConstants.back);
  }

  public boolean getBackReleased() {
    return getRawButtonReleased(ControllerConstants.back);
  }

  public boolean getStart() {
    return getRawButton(ControllerConstants.start);
  }

  public boolean getStartPressed() {
    return getRawButtonPressed(ControllerConstants.start);
  }

  public boolean getStartReleased() {
    return getRawButtonReleased(ControllerConstants.start);
  }

  public boolean getLeftThumb() {
    return getRawButton(ControllerConstants.leftThumb);
  }

  public boolean getLeftThumbPressed() {
    return getRawButtonPressed(ControllerConstants.leftThumb);
  }

  public boolean getLeftThumbReleased() {
    return getRawButtonReleased(ControllerConstants.leftThumb);
  }

  public boolean getRightThumb() {
    return getRawButton(ControllerConstants.rightThumb);
  }

  public boolean getRightThumbPressed() {
    return getRawButtonPressed(ControllerConstants.rightThumb);
  }

  public boolean getRightThumbReleased() {
    return getRawButtonReleased(ControllerConstants.rightThumb);
  }
}