package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import frc.robot.portMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
//import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

//Controls the grabber mechanism
public class grabber {
    //Intake motors and their encoders
    private final CANSparkMax left = new CANSparkMax(portMap.CAN_leftIntake, MotorType.kBrushless);
    private final RelativeEncoder ELeft= left.getEncoder();
    private final CANSparkMax right = new CANSparkMax(portMap.CAN_rightIntake, MotorType.kBrushless);
    private final RelativeEncoder ERight = right.getEncoder();

    //Intake pneumatic
    private final Compressor compress = new Compressor(PneumaticsModuleType.CTREPCM);
    private final DoubleSolenoid soul = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

    //Checks intake position
    public boolean extended = false;

    //Rotates intake motors
    public void intake(double speed) {
        left.set(-speed);
        right.set(speed);
    }

    public void toggle() {
        if (extended) retract();
        else extend();
    }

    public void extend() {
        soul.set(Value.kForward);
        extended = true;
    }

    public void retract() {
        soul.set(Value.kReverse);
        extended = false;
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

    public double getPressure() {
        return compress.getPressure();
    }

}
