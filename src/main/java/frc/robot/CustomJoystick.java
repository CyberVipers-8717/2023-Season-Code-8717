package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class CustomJoystick {
    private Joystick stick;

    public CustomJoystick(int port) {
        stick = new Joystick(port);
    }

    public double getRawAxis(int axis) {
        return stick.getRawAxis(axis);
    }

    public double getX() {
        return getRawAxis(1);
    }

    public double getY() {
        return getRawAxis(0);
    }

    public double getZ() {
        return getRawAxis(2);
    }
    
    public double getSlider() {
        return getRawAxis(3);
    }

    public boolean getRawButton(int button) {
        return stick.getRawButton(button);
    }

    public boolean getRawButtonPressed(int button) {
        return stick.getRawButtonPressed(button);
    }

    public boolean getRawButtonReleased(int button) {
        return stick.getRawButtonReleased(button);
    }

    public boolean getTrigger() {
        return stick.getTrigger();
    }

    public boolean getTriggerPressed() {
        return stick.getTriggerPressed();
    }

    public boolean getTriggerReleased() {
        return stick.getTriggerReleased();
    }

    public boolean getThumb() {
        return getRawButton(2);
    }

    public boolean getThumbPressed() {
        return getRawButtonPressed(2);
    }

    public boolean getThumbReleased() {
        return getRawButtonReleased(2);
    }
}