package frc.robot.Subsystems;

import frc.robot.portMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


public class DriveTrain {
    static CANSparkMax left1 = new CANSparkMax(portMap.CAN_left1, MotorType.kBrushless);
    static CANSparkMax left2 = new CANSparkMax(portMap.CAN_left2, MotorType.kBrushless);
    static CANSparkMax right1 = new CANSparkMax(portMap.CAN_right1, MotorType.kBrushless);
    static CANSparkMax right2 = new CANSparkMax(portMap.CAN_right2, MotorType.kBrushless);

    //encoders
    static RelativeEncoder leftEncoder;
	static RelativeEncoder rightEncoder;

    static MotorControllerGroup left = new MotorControllerGroup(left1, left2);
    static MotorControllerGroup right = new MotorControllerGroup(right1, right2);

    static DifferentialDrive diffdrive = new DifferentialDrive(left, right);

    

    public DriveTrain(){
        
    }

    public static void setUp(){
        right.setInverted(true);

        leftEncoder = left1.getEncoder();
        rightEncoder = right1.getEncoder();

        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public static void drive(double x, double y){
        diffdrive.arcadeDrive(y, x);
    }

    public void backwardsDrive(double x, double y){
        diffdrive.arcadeDrive(-y, x);
    }

    public void straightDrive(){
        diffdrive.arcadeDrive(.5, 0);
    }

    public double getLeftEncoder(){
        return leftEncoder.getPosition();
    }

    public double getRightEncoder(){
        return rightEncoder.getPosition();
    }

    public void resetEncoders(){
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }
}
