// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;

//constants
import frc.robot.constants.Climber;
import frc.robot.constants.Drivetrain;
import frc.robot.constants.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // motors
  private final PWMSparkMax m_topLeft = new PWMSparkMax(Drivetrain.TOP_LEFT_PORT);
  private final PWMSparkMax m_topRight = new PWMSparkMax(Drivetrain.TOP_RIGHT_PORT);
  private final PWMSparkMax m_bottomLeft = new PWMSparkMax(Drivetrain.BOTTOM_LEFT_PORT);
  private final PWMSparkMax m_bottomRight = new PWMSparkMax(Drivetrain.BOTTOM_RIGHT_PORT);

  private final PWMSparkMax m_shooterLower = new PWMSparkMax(Shooter.SHOOTER_LOWER_PORT);
  private final PWMSparkMax m_shooterUpper = new PWMSparkMax(Shooter.SHOOTER_UPPER_PORT);
  private final CANSparkMax m_climber = new CANSparkMax(Climber.CLIMBER_MOTOR_PORT, Climber.CLIMBER_MOTOR_TYPE);

  //controllers
  private final Joystick joystick = new Joystick(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("Climber Current", 0);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

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
      case kCustomAuto:
        // Put custom auto code here
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
    // stop motors when they get to a certain current
    if (Climber.CLIMBER_CURRENT_LIMIT) {
      m_climber.setSmartCurrentLimit(Climber.MAX_CLIMBER_CURRENT);
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //get current
    double current = m_climber.getOutputCurrent();
    SmartDashboard.putNumber("Climber Current", current);

    //get controls
    double forward = joystick.getRawAxis(0);
    double rotate = joystick.getRawAxis(1);

    boolean climb = joystick.getRawButton(Climber.CLIMBER_UP_BUTTON);
    boolean descend = joystick.getRawButton(Climber.CLIMBER_DOWN_BUTTON);

    boolean shoot = joystick.getRawButton(Shooter.SHOOTER_BUTTON);
    boolean intake = joystick.getRawButton(Shooter.INTAKE_BUTTON);

    //drive
    m_topLeft.set(forward + rotate);
    m_topRight.set(forward - rotate);
    m_bottomLeft.set(forward + rotate);
    m_bottomRight.set(forward - rotate);

    //climb
    if (climb) {
      m_climber.set(Climber.CLIMBER_SPEED);
    } else if (descend) {
      m_climber.set(-Climber.CLIMBER_SPEED);
    } else {
      m_climber.set(0);
    }

    //shoot
    if (shoot) {
      m_shooterLower.set(Shooter.INTAKE_SPEED);
      m_shooterUpper.set(Shooter.SHOOTER_SPEED);
    } else if (intake) {
      m_shooterLower.set(-Shooter.INTAKE_SPEED);
      m_shooterUpper.set(-Shooter.SHOOTER_SPEED);
    } else {
      m_shooterLower.set(0);
      m_shooterUpper.set(0); 
    }
  }

  /** This function is called once when the robot is disabled. */
  

  @Override
  public void disabledInit() {
    // Create an array of motors
    PWMSparkMax[] motors = new PWMSparkMax[] {
      //drive
      m_topLeft, 
      m_topRight, 
      m_bottomLeft, 
      m_bottomRight,
      //shooter
      m_shooterLower,
      m_shooterUpper,
    };

    // Turn off all the motors
    for (PWMSparkMax motor : motors) {
      motor.set(0);
    }

    m_climber.set(0);
  }

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
