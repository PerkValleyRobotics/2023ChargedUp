package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import frc.robot.portMap;

//Controls the arm
public class arm {
    private SimpleMotorFeedforward man = new SimpleMotorFeedforward(1, 4);
    //Motor controller for pivot point
    private CANSparkMax tiltControl = new CANSparkMax(portMap.CAN_tilt, MotorType.kBrushless);
    //PID for pivot point
    private PIDController pidPivot = new PIDController(0.15, 0.02, 0);

    //Motor controller for arm
    private CANSparkMax armExtend = new CANSparkMax(portMap.CAN_extending, MotorType.kBrushless);
    //PID for arm
    private PIDController pidArm = new PIDController(0, 0, 0);

    //Motor controller for lazy susan
    private CANSparkMax susan = new CANSparkMax(portMap.CAN_susRotate, MotorType.kBrushless);
    private RelativeEncoder susEncoder;
    
    //Important arm variables
    public double angle = 0;
    public double extensionAmmount = 0;

    public arm(){
        susEncoder = susan.getEncoder();
        susEncoder.setPosition(0);
    }

    //Controls the angle of the arm
    //DO NOT RUN!!!! NOT FINISHED
    public void tilt(double speed) {
        tiltControl.set(speed);
        pidPivot.atSetpoint();
    }

    public void tiltManual(double speed){
        tiltControl.set(speed);
    }

    //Extends and contracts the arm
    public void extend(double speed) {
        armExtend.set(speed);
    }

    //Rotates the lazy suzan
    public void rotate(double speed) {
        //susan.getEncoder().getPosition();
        susan.set(speed);
    }

    //Testing
    //----------------------------------------------------------
    public void rotatePID(double setpoint){
        susan.set(pidPivot.calculate(getSusanEncoder(), setpoint));
    }

    public void rotateFeedForward(){
        susan.setVoltage(man.calculate(-1, 1));
    }
    //-----------------------------------------------------------

    //Gets the encoder position of the lazy susan
    public double getSusanEncoder() {
        return susEncoder.getPosition();
    }

    public void resetSus() {
        susEncoder.setPosition(0);
    }
}
