package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
//import edu.wpi.first.math.controller.SimpleMotorFeedforward;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import frc.robot.portMap;

//Controls the arm
public class arm {
    //private SimpleMotorFeedforward man = new SimpleMotorFeedforward(1, 4);
    //Motor controller for pivot point
    private CANSparkMax tiltControl = new CANSparkMax(portMap.CAN_tilt, MotorType.kBrushless);
    private RelativeEncoder tiltEncoder;
    //PID for pivot point
    private PIDController pidExtention = new PIDController(0, 0., 0);

    //Motor controller for arm
    private CANSparkMax armExtend = new CANSparkMax(portMap.CAN_extending, MotorType.kBrushless);
    private RelativeEncoder extendEncoder;
    
    //PID for arm
    private PIDController pidTilt = new PIDController(0, 0, 0);

    //Motor controller for lazy susan
    private CANSparkMax susan = new CANSparkMax(portMap.CAN_susRotate, MotorType.kBrushless);
    private RelativeEncoder susEncoder;
    
    //Important arm variables
    public double angle = 0;
    public double extensionAmmount = 0;

    public arm(){
        susEncoder = susan.getEncoder();
        susEncoder.setPosition(0);

        tiltEncoder = tiltControl.getEncoder();
        tiltEncoder.setPosition(0);
    }

    //Controls the angle of the arm
    //DO NOT RUN!!!! NOT FINISHED
    public void tilt(double setpoint) {
        tiltControl.set(pidTilt.calculate(getTiltEncoder(), setpoint));
    }

    public void tiltManual(double speed){
        tiltControl.set(speed);
    }

    //Extends and contracts the arm
    public void extend(double setpoint) {
        armExtend.set(pidExtention.calculate(getExtendEncoder(), setpoint));
    }

    //Rotates the lazy suzan
    public void rotate(double speed) {
        //susan.getEncoder().getPosition();
        susan.set(speed);
    }

    //Testing
    //----------------------------------------------------------
    // public void rotatePID(double setpoint){
    //     susan.set(pidPivot.calculate(getSusanEncoder(), setpoint));
    // }

    // public void rotateFeedForward(){
    //     susan.setVoltage(man.calculate(-1, 1));
    // }
    //-----------------------------------------------------------

    //Gets the encoder position of the lazy susan
    public double getSusanEncoder() {
        return susEncoder.getPosition();
    }

    public void resetSus() {
        susEncoder.setPosition(0);
    }

    public double getTiltEncoder() {
        return tiltEncoder.getPosition();
    }

    public void resetTilt() {
        tiltEncoder.setPosition(0);
    }

    public double getExtendEncoder() {
        return extendEncoder.getPosition();
    }

    public void resetExtention() {
        extendEncoder.setPosition(0);
    }
}
