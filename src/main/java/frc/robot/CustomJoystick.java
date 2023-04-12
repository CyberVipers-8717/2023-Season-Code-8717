package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class CustomJoystick {
    private Joystick stick;

    public CustomJoystick(int port) {
        stick = new Joystick(port);
    }

    /**
     * Get the value of the axis.
     * @param axis The axis to read, starting at 0.
     * @return The value of the axis.
     */
    public double getRawAxis(int axis) {
        return stick.getRawAxis(axis);
    }

    /**
     * Get the value of the X axis.
     * @return The value of the X axis.
     */
    public double getX() {
        return getRawAxis(1);
    }

    /**
     * Get the value of the Y axis.
     * @return The value of the Y axis.
     */
    public double getY() {
        return getRawAxis(0);
    }

    /**
     * Get the value of the Z axis.
     * @return The value of the Z axis.
     */
    public double getZ() {
        return getRawAxis(2);
    }
    
    /**
     * Get the value of the slider.
     * @return The value of the slider.
     */
    public double getSlider() {
        return getRawAxis(3);
    }

    /**
     * Get the button value.
     * @param button The button number to be read, starting at 1.
     * @return The state of the button.
     */
    public boolean getRawButton(int button) {
        return stick.getRawButton(button);
    }

    /**
     * Whether the button was pressed since the last check.
     * @param button The button index, starting at 1.
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRawButtonPressed(int button) {
        return stick.getRawButtonPressed(button);
    }

    /**
     * Whether the button was released since the last check.
     * @param button The button index, starting at 1.
     * @return Whether the button was released since the last check.
     */
    public boolean getRawButtonReleased(int button) {
        return stick.getRawButtonReleased(button);
    }

    /**
     * Read the state of the trigger on the joystick.
     * @return The state of the trigger.
     */
    public boolean getTrigger() {
        return stick.getTrigger();
    }

    /**
     * Whether the trigger was pressed since the last check.
     * @return Whether the trigger was pressed since the last check.
     */
    public boolean getTriggerPressed() {
        return stick.getTriggerPressed();
    }

    /**
     * Whether the trigger was released since the last check.
     * @return Whether the trigger was released since the last check.
     */
    public boolean getTriggerReleased() {
        return stick.getTriggerReleased();
    }

    /**
     * Read the state of the side thumb button on the joystick.
     * @return The state of the side thumb button.
     */
    public boolean getThumb() {
        return getRawButton(2);
    }

    /**
     * Whether the side thumb button was pressed since the last check.
     * @return Whether the side thumb button was pressed since the last check.
     */
    public boolean getThumbPressed() {
        return getRawButtonPressed(2);
    }

    /**
     * Whether the side thumb button was released since the last check.
     * @return Whether the side thumb button was released since the last check.
     */
    public boolean getThumbReleased() {
        return getRawButtonReleased(2);
    }
}