package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

//Handles inputs
public class OIHandler {
    //Controller objects
    Joystick joystick = new Joystick(portMap.joystick);
    XboxController xbox = new XboxController(portMap.xbox);

    //Gets the flighsticks's current x val
    public double getJoystickX(){
        return joystick.getX();
    }

    //Gets the flighsticks's current y val
    public double getJoystickY(){
        return joystick.getY() * -1;
    }

    //Checks if a specified button on the flightstick is being pressed
    public boolean getJoystickButtonPress(int button){
        return joystick.getRawButton(button);
    }

    //Checks if a button on the xbox controller is being pressed
    public boolean getXboxButtonPress(int button){
        return xbox.getRawButton(button);
    }

    //Checks for thumbstick on flight controller
    public int getJoystickPOV(){
        return joystick.getPOV();
    }

    //gets the position of a specified trigger on xbox controller
    public double getTrigger(int axis){
        return xbox.getRawAxis(axis);
    }

    //Checks dpad on xbox controller
    public double getPOV() {
        return xbox.getPOV();
    }
}
