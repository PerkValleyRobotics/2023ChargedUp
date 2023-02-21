// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.DriveTrain;
//import frc.robot.subsystems.balance; see balance class
import frc.robot.subsystems.grabber;
import frc.robot.subsystems.arm;

/*
The VM is configured to automatically run this class, and to call the functions corresponding to
each mode, as described in the TimedRobot documentation. If you change the name of this class or
the package after creating this project, you must also update the build.gradle file in the
project.
*/
public class Robot extends TimedRobot {

  //Objects
  private ADXRS450_Gyro gBoy = new ADXRS450_Gyro();
  private DriveTrain drive = new DriveTrain();
  private OIHandler oi = new OIHandler();
  //private balance ballet = new balance(); see balance class
  private grabber grab = new grabber();
  private arm armyBoy = new arm();

  private double lol = -1;
  private double error;
  private double setPoint = 0;
  private double GrabberSetL = 0;
  private double GrabberSetR = 0;
  private boolean changable = true;

  //Auton choices
  private static final String kDefaultAuto = "communityExit";
  private static final String kCustomAuto1 = "oneCone";
  private static final String kCustomAuto2 = "oneCube";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
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
    SmartDashboard.putNumber("susEncoder: ", armyBoy.getSusanEncoder() * 42);

    //Robot's current angle
    //SmartDashboard.putNumber("Gyro: ", gyroRap((int)(Math.round(gBoy.getAngle()))));
    //SmartDashboard.putNumber("Gyro: ", (int)(ballet.navX.getRoll())); was causing an error 

    //Error for encoder
    SmartDashboard.putNumber("Error: ", error);
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
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto2:
        // Put custom auto code here
        break;
      case kCustomAuto1:
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
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

    //test for tilt
    if (oi.getJoystickButtonPress(1)){
      armyBoy.tiltManual(0.05);
    }else if(oi.getJoystickButtonPress(2)){
      armyBoy.tiltManual(-0.05);
    }else{
      armyBoy.tiltManual(0);
    }

    //Tilts the arm up & down
    if (oi.getPOV() == 0) armyBoy.tilt(1);
    else if (oi.getPOV() == 180) armyBoy.tilt(-1);
    //else armyBoy.tilt(0);

    if (oi.getXboxButtonPress(6)) armyBoy.extend(1);
    else if (oi.getXboxButtonPress(5)) armyBoy.extend(-1);
    else armyBoy.extend(0);

    //Rotates lazy susan
    if (oi.getPOV() == 90) armyBoy.rotate(-0.3);
    else if (oi.getPOV() == 270) armyBoy.rotate(0.3);
    else armyBoy.rotate(0);

    //Controls intake
    if (oi.getXboxButtonPress(4)){
      grab.intake(0.1);//.25 
      // changable = false;
      // GrabberSetL+=1;
      // GrabberSetR+=1;
      
    }else if (oi.getXboxButtonPress(1)){
      grab.intake(-0.15);//-.25
      // changable = false;
      // GrabberSetL-=1;
      // GrabberSetR-=1;
    }else if(!oi.getXboxButtonPress(1) && !oi.getXboxButtonPress(4)){
      changable = true;
      grab.resetEncoders();
      GrabberSetL = 0;
      GrabberSetR = 0;
      grab.PID(-GrabberSetR, GrabberSetL);
      //grab.intake(-.02);
    }
    

    //If button 3 on xbox is pressed the encoders will be reset
    if (oi.getXboxButtonPress(3)) {
      setPoint = 0;
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
