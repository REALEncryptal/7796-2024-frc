package frc.robot.constants;

import com.revrobotics.CANSparkMax;

public class Climber {
    // ports
    public static final int CLIMBER_MOTOR_PORT = 6;

    // motor type
    public static final CANSparkMax.MotorType CLIMBER_MOTOR_TYPE = CANSparkMax.MotorType.kBrushed;

    // speeds
    public static final double CLIMBER_SPEED = 0.5;

    //current
    public static final int MAX_CLIMBER_CURRENT = 0;
    public static final boolean CLIMBER_CURRENT_LIMIT = false;

    // controls
    public static final int CLIMBER_UP_BUTTON = 1;
    public static final int CLIMBER_DOWN_BUTTON = 2;
}
