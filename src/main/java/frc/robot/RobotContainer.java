// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import static frc.robot.Constants.OperatorConstants.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import static frc.robot.Constants.FuelConstants.*;
import frc.robot.commands.Autos;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CANDriveSubsystem;
import frc.robot.subsystems.CANFuelSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.superstructure.Superstructure;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
  // private final CANFuelSubsystem ballSubsystem = new CANFuelSubsystem();
  // Need to replace the code for new ball system commands and more.
  public final Superstructure superstructure;
  
  // The driver's controller
  private final CommandXboxController driverController = new CommandXboxController(
      DRIVER_CONTROLLER_PORT);


  // The autonomous chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()

            // NOTE deadband changed to 0.01 from 0.1
            .withDeadband(DriveTrainConstants.ROBOT_MAX_SPEED * 0.01).withRotationalDeadband(DriveTrainConstants.MAX_ANGULAR_RATE * 0.01) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final Telemetry logger = new Telemetry(DriveTrainConstants.ROBOT_MAX_SPEED);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    
    superstructure = new Superstructure(drivetrain);
    
    configureBindings();

    // Set the options to show up in the Dashboard for selecting auto modes. If you
    // add additional auto modes you can add additional lines here with
    // autoChooser.addOption
    // autoChooser.setDefaultOption("Autonomous", Autos.exampleAuto(driveSubsystem, ballSubsystem));


  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the {@link Trigger#Trigger(java.util.function.BooleanSupplier)}
   * constructor with an arbitrary predicate, or via the named factories in
   * {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses
   * for {@link CommandXboxController Xbox}/
   * {@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */

    // Set the default command for the drive subsystem to the command provided by
    // factory with the values provided by the joystick axes on the driver
    // controller. The Y axis of the controller is inverted so that pushing the
    // stick away from you (a negative value) drives the robot forwards (a positive
    // value). The X-axis is also inverted so a positive value (stick to the right)
    // results in clockwise rotation (front of the robot turning right). Both axes
    // are also scaled down so the rotation is more easily controllable.

    // driveArcade is important and is the way we cross compat this code with newly generated code
    // If new code is completed type y here: n


  private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
        
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(calculateVelocity(driverController.getLeftY())) // Drive forward with negative Y (forward)
                    .withVelocityY(calculateVelocity(driverController.getLeftX())) // Drive left with negative X (left)
                    .withRotationalRate(calculateRotationalVelocity()) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();


        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.

        driverController.start().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);

        // DRIVER CONTROLLER

        // sets robot shoot on/off while right trigger is held

        // (joystick.rightTrigger().and(()-> ((superstructure.readyToShoot()) || (joystick.rightBumper().getAsBoolean()))))
        //         .whileTrue(shootingCommand());

        (driverController.y()).whileTrue(shootingCommand());
         driverController.y().onFalse(superstructure.stopShooting());


        (driverController.rightTrigger().and(superstructure::readyToShoot))
                .whileTrue(shootingCommand());
        driverController.rightTrigger().onFalse(superstructure.stopShooting());

        //SOTM
        driverController.x().whileTrue(superstructure.startShootingSOTM());
        driverController.x().onFalse(superstructure.stopShooting());

        // manual shooting
        driverController.rightBumper().whileTrue(superstructure.shootManually());
        driverController.rightBumper().onFalse(superstructure.stopShooting());

        // sets intake on/off on toggle default = on
        driverController.leftBumper().onTrue(superstructure.deployIntakingCommand());
        // retracts intake
        driverController.leftTrigger().onTrue(superstructure.retractIntake());

        // driverController.rightBumper().whileTrue(drivetrain.applyRequest(() -> brake));

        driverController.a().whileTrue(superstructure.outTake());
        driverController.a().onFalse(superstructure.deployIntakingCommand());

        // joystick.back().onTrue(new InstantCommand(() -> drivetrain.resetPose(new Pose2d(0, 0, new Rotation2d()))));

        // TESTING JOYSTICK

        // intake deploy and retract
    
        // testJoystick.leftBumper().whileTrue(superstructure.testIntakeDeployDown());
        // testJoystick.leftBumper().onFalse(superstructure.retractIntake());

        // testJoystick.leftTrigger().whileTrue(superstructure.testIntakeDeployUp());
        // testJoystick.leftTrigger().onFalse(superstructure.retractIntake());

        // // intake rollers test
        // testJoystick.a().whileTrue(superstructure.testIntakeRollers());
        // testJoystick.a().onFalse(superstructure.stopIntakeRollers());

        // // roller floor test
        // testJoystick.rightBumper().whileTrue(superstructure.testRollers());
        // testJoystick.rightBumper().onFalse(superstructure.stopRollers());

        // // shooter test
        // testJoystick.rightTrigger().whileTrue(superstructure.testShooter());
        // testJoystick.rightTrigger().onFalse(superstructure.stopShooting());

        // // SYS ID TUNING
        // testJoystick.x().whileTrue(shooter.sysIdQuasistatic(Direction.kForward));
        // testJoystick.y().whileTrue(shooter.sysIdQuasistatic(Direction.kReverse));
        // testJoystick.a().whileTrue(shooter.sysIdDynamic(Direction.kForward));
        // testJoystick.b().whileTrue(shooter.sysIdDynamic(Direction.kReverse));

        // testJoystick.x().whileTrue(intake.sysIdQuasistaticCommand(Direction.kForward));
        // testJoystick.y().whileTrue(intake.sysIdQuasistaticCommand(Direction.kReverse));
        // testJoystick.a().whileTrue(intake.sysIdDynamicCommand(Direction.kForward));
        // testJoystick.b().whileTrue(intake.sysIdDynamicCommand(Direction.kReverse));
    }

    public Command getAutonomousCommand() {
        

        return autoChooser.getSelected();

    }

    public double calculateVelocity(double joystick) {
        double leftJoystick = MathUtil.applyDeadband(joystick, 0.1);
        double velocity = -DriveTrainConstants.ROBOT_MAX_SPEED * scale(leftJoystick, 2.5);
        return velocity;
    }

    public double calculateRotationalVelocity() {
        double omega = 0;
        if (driverController.rightTrigger().getAsBoolean()) {
            if (!superstructure.pointedAtTarget()) {
                omega = superstructure.turnToTargetSpeed();
            } 
         } else if (driverController.x().getAsBoolean()) {
                omega = superstructure.turnToTargetSpeedSOTM();
        } else {
            double rightX = MathUtil.applyDeadband(driverController.getRightX(), 0.05);
            omega = -DriveTrainConstants.MAX_ANGULAR_RATE * scale(rightX, 2.5);
        }
        return omega;
    }

    private double scale(double in, double scale) {
        return Math.tan(in * Math.atan(scale)) / scale;
    }

    private boolean isJoystickNeutral() {
        return Math.hypot(driverController.getLeftX(), driverController.getLeftY()) < 0.1;
    }
    private Command shootingCommand() {
        return Commands.parallel(
                superstructure.startShooting(),
                Commands.run(() -> {
                    if (isJoystickNeutral()) {
                        drivetrain.setControl(brake);
                    } else {
                        drivetrain.setControl(
                                drive.withVelocityX(calculateVelocity(driverController.getLeftY()))
                                        .withVelocityY(calculateVelocity(driverController.getLeftX()))
                                        .withRotationalRate(calculateRotationalVelocity()));
                    }
                }, drivetrain));
    }
}
