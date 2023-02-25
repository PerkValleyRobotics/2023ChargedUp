// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Auton.autonBase;
import frc.robot.subsystems.DriveTrain;
//import frc.robot.subsystems.balance; see balance class
import frc.robot.subsystems.grabber;
import frc.robot.subsystems.arm;
import frc.robot.subsystems.vision;

/*
The VM is configured to automatically run this class, and to call the functions corresponding to
each mode, as described in the TimedRobot documentation. If you change the name of this class or
the package after creating this project, you must also update the build.gradle file in the
project.
*/
public class Robot extends TimedRobot {

  //Objects
  public static DriveTrain drive = new DriveTrain();
  private OIHandler oi = new OIHandler();
  private vision limelightboyo = new vision();
  //private balance ballet = new balance(); see balance class
  private grabber grab = new grabber();
  private arm armyBoy = new arm();

  private double tiltsetpoint;

  //Auton choices
  private static final String kDefaultAuto = "communityExit";
  private static final String kCustomAuto1 = "oneCone";
  private static final String kCustomAuto2 = "oneCube";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  autonBase autoBoyo;
  
  //Puts all of the data we want onto the smartdashboard
  private void smartdashboards() {
    //Adds Joysticks values
    SmartDashboard.putNumber("Joystick X: ", oi.getJoystickX());
    SmartDashboard.putNumber("Joystick Y: ", oi.getJoystickY());

    //Adds current extension ammount and angle of the arm
    SmartDashboard.putNumber("Current Angle: ", armyBoy.angle);
    SmartDashboard.putNumber("Extension Ammount: ", armyBoy.extensionAmmount);

    //Encoder values
    SmartDashboard.putNumber("Encoder Left: ", drive.getLeftEncoder());
    SmartDashboard.putNumber("Encoder Right: ", drive.getRightEncoder());

    //Robot's current angle
    //SmartDashboard.putNumber("Gyro: ", gyroRap((int)(Math.round(gBoy.getAngle()))));
    //SmartDashboard.putNumber("Gyro: ", (int)(ballet.navX.getRoll())); was causing an error 
  }

  /*
  //Returns current angle in the smartdashboard
  private int gyroRap(int angle){
    //If angle is less than zero it will loop back to a positive angle between 0-360
    if (angle < 0) return 360-(int)(Math.abs(ballet.navX.getRoll())%360);
    //Else display current gyro angle between 0-360
    else return angle%360;
  }
  */
  /*
  This function is run when the robot is first started up and should be used for any
  initialization code.
  */
  @Override
  public void robotInit() {

    //smartdashboard
    //Adds auton options
    m_chooser.setDefaultOption("Exit Community", kDefaultAuto);
    m_chooser.addOption("One Cone", kCustomAuto1);
    m_chooser.addOption("One Cube", kCustomAuto2);
    SmartDashboard.putData("Auto choices", m_chooser);

    smartdashboards();
  }

  /*
  This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
  that you want ran during disabled, autonomous, teleoperated and test.
  
   <p>This runs after the mode specific periodic functions, but before LiveWindow and
  SmartDashboard integrated updating.
  */
  @Override
  public void robotPeriodic() {
    smartdashboards();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    switch (m_autoSelected) {
      case kCustomAuto2:
        // Put custom auto code here
        break;
      case kCustomAuto1:
        break;
      case kDefaultAuto:
      default:
        autoBoyo = new autonBase();
        break;
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (!autoBoyo.isFinished()) autoBoyo.execute();
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    armyBoy.resetSus();
    grab.resetEncoders();
    
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //Drives using flighstick values
    drive.drive(-oi.getJoystickY(), oi.getJoystickX());

    //Balances Robot
    //if (oi.getXboxButtonPress(8)) ballet.balanceRobot(armyBoy); balance is broken

    //turn limelight light on / off
    if (oi.getTrigger(2) > 0.05) limelightboyo.lightOn();
    else limelightboyo.lightOff();

    //Tilts the arm up & down
    if (oi.getPOV() == 0) armyBoy.tiltManual(.1);
    else if (oi.getPOV() == 180) armyBoy.tiltManual(-.1);
    else armyBoy.tiltManual(0);

    //Arm pid tilt locations
    if (oi.getXboxButtonPress(1)) tiltsetpoint = 0; // low
    else if(oi.getXboxButtonPress(2)) tiltsetpoint = 0; // human player station
    else if(oi.getXboxButtonPress(3)) tiltsetpoint = 0; // mid
    else if(oi.getXboxButtonPress(4)) tiltsetpoint = 0; // high

    armyBoy.tilt(tiltsetpoint);

    //Extends arm in & out
    if (oi.getPOV() == 90) armyBoy.extend(1);
    else if (oi.getPOV() == 270) armyBoy.extend(-1);
    else armyBoy.extend(0);

    //Arm extend pid locations
    


    //Rotates lazy susan
    //not in use
    // if (oi.getPOV() == 90) armyBoy.rotate(-0.3);
    // else if (oi.getPOV() == 270) armyBoy.rotate(0.3);
    // else armyBoy.rotate(0);

    //Controls intake
    if (oi.getJoystickButtonPress(1)) grab.intake(0.1);
    else if (oi.getJoystickButtonPress(2)) grab.intake(-0.15);
    else grab.intake(0);
    

    //If button 3 on xbox is pressed the encoders will be reset
    if (oi.getXboxButtonPress(8)) {
      drive.resetEncoders();
      armyBoy.resetSus();
    }

    //Testing
    //-----------------------------------------------------
    //Spins motor 90 degrees
    // if (oi.getXboxButtonPress(2) && lol == 0.0) {
    //   lol = 1.0;
    //   setPoint = armyBoy.getSusanEncoder() + 0.25;
    // }
    // else if (!oi.getXboxButtonPress(2)) {
    //   lol = 0;
    // }
    // //armyBoy.rotatePID(setPoint);
    // //armyBoy.rotateFeedForward();
    // error = setPoint - (armyBoy.getSusanEncoder() * 42);
  }
  //-------------------------------------------------------------

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
