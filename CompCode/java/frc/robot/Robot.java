// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Auton.autonBase;
import frc.robot.subsystems.*;

import edu.wpi.first.cameraserver.CameraServer;
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

  //Important variables
  private double tiltsetpoint = 0;
  private double extendsetpoint = 0;
  private boolean done = true;
  private double targetSetpoint = 0;
  private boolean buttonIsPress = false;
  //private boolean failSafeEnable = false;
  private String inSteak;
  private double intakeSpeed = 0;

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
    SmartDashboard.putNumber("Arm Angle: ", armyBoy.getTiltEncoder());
    SmartDashboard.putNumber("Extension Ammount: ", armyBoy.getExtendEncoder());

    //Encoder values
    SmartDashboard.putNumber("Encoder Left: ", drive.getLeftEncoder());
    SmartDashboard.putNumber("Encoder Right: ", drive.getRightEncoder());

    //Arm angle to be moved to and if it is done moving
    SmartDashboard.putNumber("Target Position: ", targetSetpoint);
    SmartDashboard.putBoolean("Done Moving?: ", done);

    //Setpoints
    SmartDashboard.putNumber("Arm Set Point: ", tiltsetpoint);
    SmartDashboard.putNumber("Extend Set Point: ", extendsetpoint);

    //Pneumatics pressure
    //SmartDashboard.putNumber("Robot Pressure: ", grab.getPressure());
    //Intake state
    if (grab.extended) inSteak = "Cube";
    else inSteak = "Cone";

    SmartDashboard.putString("Intake", inSteak);

    //Intake direcrion
    if (intakeSpeed == .6) SmartDashboard.putString("Intake Direction: ", "Fast Out");
    else if (intakeSpeed == .2) SmartDashboard.putString("Intake Direction: ", "Out");
    else if (intakeSpeed == -.15) SmartDashboard.putString("Intake Direction: ", "In");
    else SmartDashboard.putString("Intake Direction: ", "Stopped");

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

    CameraServer.startAutomaticCapture();
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
    //armyBoy.extend(extendsetpoint);  
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
    autoBoyo.execute();
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    armyBoy.resetSus();
    grab.resetEncoders();
    
    limelightboyo.setPipeLine();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //Drives using flighstick values
    drive.drive(oi.getJoystickX(), -oi.getJoystickY());

    //Balances Robot
    //if (oi.getXboxButtonPress(8)) ballet.balanceRobot(armyBoy); balance is broken

    //turn limelight light on / off
    //if (oi.getTrigger(2) > 0.05) limelightboyo.lightOn();
    //else limelightboyo.lightOff();

    //Tilts the arm up & down manually
    if (oi.getPOV() == 0) tiltsetpoint += .3;
    else if (oi.getPOV() == 180) tiltsetpoint -= .2;

    //Extends arm in & out manually
    if (oi.getPOV() == 90) extendsetpoint += 0.6;
    else if (oi.getPOV() == 270) extendsetpoint -= 0.6;

    //Arm pid locations
    if (oi.getXboxButtonPress(1)) { // low
      done = false;
      targetSetpoint = 13.4;
      extendsetpoint = -64.02;
    }
    else if(oi.getXboxButtonPress(2)) { //ground position
      // done = false;
      // targetSetpoint = 8.3;
      // extendsetpoint = -280.65;
    }
    else if(oi.getXboxButtonPress(3)) { // mid
      done = false;
      targetSetpoint = 23.57;
      extendsetpoint = -10;
    }
      //extendsetpoint = -243.6;
    else if(oi.getXboxButtonPress(4)) { // human player 
      done = false;
      //targetSetpoint = 28.7;
      //26
      targetSetpoint = 28.7;
      extendsetpoint = 0;
    }

    //Fail safe for pid
    //if (!failSafeEnable && armyBoy.getTiltEncoder() > 4) failSafeEnable = true;
    //if (armyBoy.getTiltEncoder() < 3.1 && failSafeEnable) cutPID = true;

    //Steps setpoint for tilt to the target location
    if (!done) {
      if(tiltsetpoint < targetSetpoint) {
        tiltsetpoint += 0.2;
      }
      else if(tiltsetpoint > targetSetpoint) {
        tiltsetpoint -= 0.08;
      }
    }
    if (targetSetpoint - .4 < armyBoy.getTiltEncoder() && armyBoy.getTiltEncoder() < targetSetpoint + .4) {
      done = true;
    }

    //Updates pid for arm tilt and extend
    if ((targetSetpoint != 0 || armyBoy.getExtendEncoder() > -0.2)) armyBoy.tilt(tiltsetpoint);
    if ((done || targetSetpoint == 0)) armyBoy.extend(extendsetpoint);
    

    //Rotates lazy susan
    //not in use
    // if (oi.getPOV() == 90) armyBoy.rotate(-0.3);
    // else if (oi.getPOV() == 270) armyBoy.rotate(0.3);
    // else armyBoy.rotate(0);

    //Controls intake
    if (oi.getXboxButtonPress(6) && oi.getXboxButtonPress(7)) intakeSpeed = .2;
    else if (oi.getXboxButtonPress(6)) intakeSpeed = .6;
    else if (oi.getXboxButtonPress(5)) intakeSpeed = -.15;
    else intakeSpeed = 0;;

    grab.intake(intakeSpeed);

    //Intake out/in
    if (oi.getJoystickButtonPress(5) && !buttonIsPress) {
      grab.toggle();
      buttonIsPress = true;
    }
    else if (!oi.getJoystickButtonPress(5)) buttonIsPress = false;

    //Cuts the PID
    //if (oi.getXboxButtonPress(7)) cutPID = true;

    //Resets everything
    if (oi.getXboxButtonPress(8)) {
      done = true;
      drive.resetEncoders();
      armyBoy.resetSus();
      armyBoy.resetTilt();
      armyBoy.resetExtention();
      targetSetpoint = 0;
      tiltsetpoint = 0;
      extendsetpoint = 0;
    }
  }
  
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
