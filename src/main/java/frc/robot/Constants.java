// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;
import edu.wpi.first.math.util.Units;
import frc.robot.generated.TunerConstants;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // Motor controller IDs for drivetrain motors
    public static final int LEFT_LEADER_ID = 1;
    public static final int LEFT_FOLLOWER_ID = 2;
    public static final int RIGHT_LEADER_ID = 3;
    public static final int RIGHT_FOLLOWER_ID = 4;

    // Current limit for drivetrain motors. 60A is a reasonable maximum to reduce
    // likelihood of tripping breakers or damaging CIM motors
    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
  }
  public static final class FuelConstants {
    // Motor controller IDs for Fuel Mechanism motors
    public static final int FEEDER_MOTOR_ID = 6;
    public static final int INTAKE_LAUNCHER_MOTOR_ID = 5;

    // Current limit and nominal voltage for fuel mechanism motors.
    public static final int FEEDER_MOTOR_CURRENT_LIMIT = 60;
    public static final int LAUNCHER_MOTOR_CURRENT_LIMIT = 60;

    // Voltage values for various fuel operations. These values may need to be tuned
    // based on exact robot construction.
    // See the Software Guide for tuning information
    public static final double INTAKING_FEEDER_VOLTAGE = -12;
    public static final double INTAKING_INTAKE_VOLTAGE = 10;
    public static final double LAUNCHING_FEEDER_VOLTAGE = 9;
    public static final double LAUNCHING_LAUNCHER_VOLTAGE = 10.6;
    public static final double SPIN_UP_FEEDER_VOLTAGE = -6;
    public static final double SPIN_UP_SECONDS = 1;
  }

  public static final class OperatorConstants {
    // Port constants for driver and operator controllers. These should match the
    // values in the Joystick tab of the Driver Station software
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    // This value is multiplied by the joystick value when driving the robot to
    // help avoid driving and turning too fast and being difficult to control
    public static final double DRIVE_SCALING = .7;
    public static final double ROTATION_SCALING = .8;
  }

// The following constants were added to help combine ABot serve with currant kitbot.
  public static class FieldLayout {
      public static double FIELD_LENGTH = Units.inchesToMeters(651.22);
      public static double FIELD_WIDTH = Units.inchesToMeters(317.69);
   
      public static final Pose3d CENTER_OF_HUB_BLUE = new Pose3d(Units.inchesToMeters(182.11), Units.inchesToMeters(158.84),
                Units.inchesToMeters(72), Rotation3d.kZero);

      public static Pose3d CENTER_OF_HUB_RED =  new Pose3d(FIELD_LENGTH - CENTER_OF_HUB_BLUE.getX(), Units.inchesToMeters(158.84),
                Units.inchesToMeters(72), Rotation3d.kZero);

      public static final double BLUE_ALLIANCE_ZONE = Units.inchesToMeters(156.61);
      public static final double RED_ALLIANCE_ZONE = FIELD_LENGTH - BLUE_ALLIANCE_ZONE;

      public static final Translation3d BLUE_PASSING_SPOT_LEFT = new Translation3d(
        Meters.of(Units.inchesToMeters(90)), Meters.of(FieldLayout.FIELD_WIDTH).div(2).plus(Meters.of(Units.inchesToMeters(85))), Meters.zero());
      
        public static final Translation3d BLUE_PASSING_SPOT_RIGHT = new Translation3d(
          Meters.of(Units.inchesToMeters(90)), Meters.of(FieldLayout.FIELD_WIDTH).div(2).minus(Meters.of(Units.inchesToMeters(85))), Meters.zero());

      public static final Translation3d RED_PASSING_SPOT_LEFT = new Translation3d(
        Meters.of(FIELD_LENGTH - BLUE_PASSING_SPOT_LEFT.getX()), Meters.of(FieldLayout.FIELD_WIDTH - BLUE_PASSING_SPOT_LEFT.getY()), Meters.zero());

       public static final Translation3d RED_PASSING_SPOT_RIGHT = new Translation3d(
        Meters.of(FIELD_LENGTH - BLUE_PASSING_SPOT_RIGHT.getX()), Meters.of(FieldLayout.FIELD_WIDTH - BLUE_PASSING_SPOT_RIGHT.getY()) , Meters.zero());
    }

    public static class LimelightConstants {
      public static final String SHOOTER_LIMELIGHT = "limelight-shooter";
      public static final String LEFT_LIMELIGHT = "limelight-left";
    }
    
     public static class DriveTrainConstants {
    public static final double ROBOT_MAX_SPEED = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    public static final double MAX_ANGULAR_RATE = RotationsPerSecond.of(1).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    public static final double DT_SECONDS = 0.02; // 20ms per tick
    public static final double DISTANCE_PER_TICK = ROBOT_MAX_SPEED * DT_SECONDS; // 20ms per tick

    public static final double ROBOT_DIMENSIONS = Units.inchesToMeters(31.25);
  }
// Shooter Constants imported from A-Bot
 public static class ShooterConstants {
      public static final int SHOOT_MOTOR1_CANID = 36;
      public static final int SHOOT_MOTOR2_CANID = 37;
      public static final int KICKER_MOTOR_CANID = 42;

      public static final double STATOR_CURRENT_LIMIT_KICKER = 100.0;
      public static final double SUPPLY_CURRENT_LIMIT_KICKER = 40.0;
      
      public static final double STATOR_CURRENT_LIMIT_SHOOTER = 100.0;
      public static final double SUPPLY_CURRENT_LIMIT_SHOOTER = 40.0;

      public static final double TESTING_STATE_VOLTS = 4.0;

      public static final double KICKER_SHOOTING_VELOCITY = 1200.0 / 60.0;

      public static final double SLOW_SHOOTER_VOLTS = 2;
      public static final double SLOW_KICKER_VOLTS = 2.5;

      public static final double MANUAL_SHOOT_CLOSE = 2100.0/60.0;
      public static final double MANUAL_SHOOT_CLOSE_KICKER = 2000.0/60.0;

      public static final double MANUAL_SHOOT_FAR = 3000.0/60.0;
      public static final double MANUAL_SHOOT_FAR_KICKER = 2750.0/60.0;

      public static final double KICKER_TO_SHOOTER_RATIO = 5.0/5.0;

      public static final Transform3d BALL_TRANSFORM_CENTER = new Transform3d(0, 0, Units.inchesToMeters(18.93), Rotation3d.kZero);
      public static final InterpolatingDoubleTreeMap DISTANCE_TO_SHOT_RPM = new InterpolatingDoubleTreeMap();
      static {
        DISTANCE_TO_SHOT_RPM.put(2.512, 2150.0/60.0);
        DISTANCE_TO_SHOT_RPM.put(2.98, 2450.0/60.0);
        DISTANCE_TO_SHOT_RPM.put(3.63, 2750.0/60.0);
        DISTANCE_TO_SHOT_RPM.put(3.97, 3000.0/60.0);
        DISTANCE_TO_SHOT_RPM.put(4.572, 3750.0/60.0);
      }
      public static final InterpolatingDoubleTreeMap DISTANCE_TO_PASS_RPM = new InterpolatingDoubleTreeMap();
      static {
        DISTANCE_TO_PASS_RPM.put(2.375, 2000.0/60.0);
        DISTANCE_TO_PASS_RPM.put(2.79, 2100.0/60.0);
        DISTANCE_TO_PASS_RPM.put(3.23, 2350.0/60.0);
        DISTANCE_TO_PASS_RPM.put(3.7, 2600.0/60.0);
        DISTANCE_TO_PASS_RPM.put(4.05, 2950.0/60.0);
      }
      
      public static final InterpolatingDoubleTreeMap DISTANCE_TO_SHOT_SPEED = new InterpolatingDoubleTreeMap();
      static {
        DISTANCE_TO_SHOT_SPEED.put(2.375, 6.108);
        DISTANCE_TO_SHOT_SPEED.put(4.05, 6.452);
      }
    }

}
