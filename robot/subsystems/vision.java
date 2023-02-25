package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class vision {

  NetworkTable lime;
  
  NetworkTableEntry tx;
  NetworkTableEntry ty;
  NetworkTableEntry tv;

  public vision() {
    NetworkTableInstance inst = NetworkTableInstance.getDefault(); 
    lime = inst.getTable("limelight");

    tx = lime.getEntry("tx");
    ty = lime.getEntry("ty");
    tv = lime.getEntry("tv");
    
    lightOff();
  }

public void lightOn(){
    lime.getEntry("ledMode").setNumber(3);
  }

  public void lightOff(){
    lime.getEntry("ledMode").setNumber(1);
  }
}