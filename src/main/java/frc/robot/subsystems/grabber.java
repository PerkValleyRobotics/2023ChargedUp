package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import frc.robot.portMap;

//Controls the grabber mechanism
public class grabber {
    private CANSparkMax left = new CANSparkMax(portMap.CAN_leftIntake, MotorType.kBrushless);
    private RelativeEncoder ELeft= left.getEncoder();
    private CANSparkMax right = new CANSparkMax(portMap.CAN_rightIntake, MotorType.kBrushless);
    private RelativeEncoder ERight = right.getEncoder();
    private PIDController PIDL = new PIDController(0.00000000001, 0.004, 0);
    private PIDController PIDR = new PIDController(0.00000000001, 0.004, 0);

    
    //Rotates intake motors
    public void intake(double speed) {
        left.set(-speed);
        right.set(speed);
    }

    public double getLeftEncoder(){
        return ELeft.getPosition();
    }

    public double getRightEncoder(){
        return ERight.getPosition();
    }

    public void resetEncoders(){
        ELeft.setPosition(0);
        ERight.setPosition(0);
    }

    public void PID(double rSet, double lSet){
        right.set(PIDL.calculate(getLeftEncoder(), rSet));
        left.set(PIDR.calculate(getRightEncoder(), lSet));
        
    }




}
