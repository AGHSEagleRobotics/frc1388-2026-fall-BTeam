// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Shoontake;
import frc.robot.commands.ShoontakeCommand;
import frc.robot.commands.ShoontakeCommand.Mode;

import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.FuelConstants.*;
// import frc.robot.commands.Autos;
// import frc.robot.subsystems.CANFuelSubsystem;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import static frc.robot.Constants.FuelConstants.SPIN_UP_SECONDS;

public class RobotContainer {


  // The robot's subsystems
  // private final CANFuelSubsystem ballSubsystem = new CANFuelSubsystem();
   private final Shoontake shoontake = new Shoontake();
      private Mode lastState = null;
  
  // The autonomous chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed*.1).withRotationalDeadband(MaxAngularRate*.1)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    
    public RobotContainer(){
        configureBindings();
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() -> drive.withVelocityX(joystick.getLeftX()).withVelocityY(-joystick.getLeftY()).withRotationalRate(joystick.getRightX()))
        );
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
  private void configureBindings() {
        joystick.rightBumper()
            .onTrue(Commands.runOnce(() -> {
                if (lastState == null || lastState == Mode.LONG) {
                    lastState = Mode.SHORT;
                    new ShoontakeCommand(shoontake, Mode.SHORT).schedule();
                } else if (lastState == Mode.SHORT) {
                    lastState = Mode.MEDIUM;
                    new ShoontakeCommand(shoontake, Mode.MEDIUM).schedule();
                } else if (lastState == Mode.MEDIUM) {
                    lastState = Mode.LONG;
                    new ShoontakeCommand(shoontake, Mode.LONG).schedule();
                }
            }));

        // --- Right trigger: fire current state without changing it ---

        joystick.rightTrigger()
            .onTrue(Commands.runOnce(() -> {
                if (lastState != null) {
                    new ShoontakeCommand(shoontake, lastState).schedule();
                }
            }));

        // --- Intake: left trigger ---

        joystick.leftTrigger()
            .onTrue(new ShoontakeCommand(shoontake, Mode.INTAKE));

        // --- Vomit: left bumper ---

        joystick.leftBumper()
            .onTrue(new ShoontakeCommand(shoontake, Mode.VOMIT));

        // --- Stop shooting: X button ---

        joystick.x()
            .onTrue(Commands.runOnce(() -> {
                lastState = null;
                new ShoontakeCommand(shoontake, Mode.STOP).schedule();
            }));

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        // joystick.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        // joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        // joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        // joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        // joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // Reset the field-centric heading on left bumper press.
        // joystick.y().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0),
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
        );
    }
}
