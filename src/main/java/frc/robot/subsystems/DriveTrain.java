package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import frc.robot.portMap;

//Controls the drive train of the robot
public class DriveTrain {
    //Objects for each of the drivetrain's motorcontrollers
    private CANSparkMax left1 = new CANSparkMax(portMap.CAN_left1, MotorType.kBrushless);
    private CANSparkMax left2 = new CANSparkMax(portMap.CAN_left2, MotorType.kBrushless);
    private CANSparkMax right1 = new CANSparkMax(portMap.CAN_right1, MotorType.kBrushless);
    private CANSparkMax right2 = new CANSparkMax(portMap.CAN_right2, MotorType.kBrushless);

    //encoders
    private RelativeEncoder leftEncoder;
	private RelativeEncoder rightEncoder;

    //Groups the motor controllers based on the side of the drive train they are on
    private MotorControllerGroup left = new MotorControllerGroup(left1, left2);
    private MotorControllerGroup right = new MotorControllerGroup(right1, right2);

    //Groups the motor controller groups into one
    private DifferentialDrive diffdrive = new DifferentialDrive(left, right);

    //private SimpleMotorFeedforward simpleGoToPoint = new SimpleMotorFeedforward(.5, 0.15);

    //Ensures motorcontrollers are zeroed out and inverts left side
    public DriveTrain(){
        left.setInverted(true);

        leftEncoder = left1.getEncoder();
        rightEncoder = right1.getEncoder();

        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    //Takes two values (x - side to side, y - forward and back) to make the robot drive
    public void drive(double x, double y){
        diffdrive.arcadeDrive(y, x);
    }

    //Returns the left encoders value
    public double getLeftEncoder(){
        return leftEncoder.getPosition();
    }

    //Returns the right encoders value
    public double getRightEncoder(){
        return rightEncoder.getPosition();
    }

    //Resets both encoders to zero
    public void resetEncoders(){
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }
}
