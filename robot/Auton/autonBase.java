package frc.robot.Auton;

import frc.robot.Robot;

public class autonBase {
    private boolean finished;

    public autonBase() {
        finished = false;
        Robot.drive.resetEncoders();
    }

    public void execute() {
        Robot.drive.drive(0, .4);
        if(Math.abs(Robot.drive.getLeftEncoder()) > 66) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
