package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class vision {

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry ty = table.getEntry("ty");
  NetworkTableEntry ta = table.getEntry("ta");
  NetworkTableEntry light = table.getEntry("ledMode");
  NetworkTableEntry pipeLine = table.getEntry("pipeline");

  public void update(){
    //double x = tx.getDouble(0.0);
    //double y = ty.getDouble(0.0);
    //double area = ta.getDouble(0.0);
  }

  public void lightOn() {
    light.setNumber(3);
  }

  public void lightOff(){
    light.setNumber(1);
  }

  public void setPipeLine(){
    pipeLine.setNumber(0);
  }
}