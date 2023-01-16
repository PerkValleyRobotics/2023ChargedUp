package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

public class OIHandler {
    static Joystick joystick = new Joystick(portMap.joystick);
    static XboxController xbox = new XboxController(portMap.xbox);

    public static double getJoystickX(){
        return joystick.getX();
    }

    public static double getJoystickY(){
        return joystick.getY();
    }

    public static boolean getJoystickButtonPress(int button){
        return joystick.getRawButton(button);
    }

    public static boolean getXboxButtonPress(int button){
        return xbox.getRawButton(button);
    }

    public int getJoystickPOV(){
        return joystick.getPOV();
    }

    public double getTrigger(int axis){
        return xbox.getRawAxis(axis);
    }
}
