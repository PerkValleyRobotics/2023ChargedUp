package frc.robot.Auton;

import frc.robot.Robot;

public class autonBase {

    public autonBase() {
        Robot.drive.resetEncoders();
    }
//frame: 39in, community distance 16 1/4 ft, 17 ft
    public void execute() {
        if (Math.abs(Robot.drive.getLeftEncoder()) < 87) {
            Robot.drive.drive(0, -.4);
        }
        //1.9inches per 1 @ .2
        //1.92inches per 1 @ .4
    }
}
