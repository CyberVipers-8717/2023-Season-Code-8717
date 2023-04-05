package frc.robot;

import frc.robot.Constants.ElevatorConstants;

public class ElevatorPresets {
  /**
   * @return A double array containing the presets for the elevator motors and pulley motor.
   * They are in the form of double[] {elevatorPreset, pulleyPreset}.
   */
  public static double[] getTargetPresets() {
    if (Elevator.targetItem == Elevator.Item.Cube) {
      switch (Elevator.targetHeight) {
        case High:
          return new double[] {ElevatorConstants.highSE, ElevatorConstants.highSP};
        case Mid:
          return new double[] {ElevatorConstants.midSE, ElevatorConstants.midSP};
        case Ground:
          return new double[] {ElevatorConstants.groundSE, ElevatorConstants.groundSP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleSE, ElevatorConstants.doubleSP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    } else {
      switch (Elevator.targetHeight) {
        case High:
          return new double[] {ElevatorConstants.highTE, ElevatorConstants.highTP};
        case Mid:
          return new double[] {ElevatorConstants.midTE, ElevatorConstants.midTP};
        case Ground:
          return new double[] {ElevatorConstants.groundTE, ElevatorConstants.groundTP};
        case Rest:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
        case Double:
          return new double[] {ElevatorConstants.doubleTE, ElevatorConstants.doubleTP};
        default:
          return new double[] {ElevatorConstants.restE, ElevatorConstants.restP};
      }
    }
  }
}