// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.Shoontake;
import frc.robot.commands.ShoontakeCommand;
import frc.robot.commands.ShoontakeCommand.Mode;

import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.FuelConstants.*;
// import frc.robot.commands.Autos;
// import frc.robot.subsystems.CANFuelSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  // private final CANFuelSubsystem ballSubsystem = new CANFuelSubsystem();
   private final Shoontake shoontake = new Shoontake();
   private final CommandXboxController driverController = new CommandXboxController(0);
      private Mode lastState = null;
  
  // The autonomous chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    
    configureBindings();

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
        driverController.rightBumper()
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

        driverController.rightTrigger()
            .onTrue(Commands.runOnce(() -> {
                if (lastState != null) {
                    new ShoontakeCommand(shoontake, lastState).schedule();
                }
            }));

        // --- Intake: left trigger ---

        driverController.leftTrigger()
            .onTrue(new ShoontakeCommand(shoontake, Mode.INTAKE));

        // --- Vomit: left bumper ---

        driverController.leftBumper()
            .onTrue(new ShoontakeCommand(shoontake, Mode.VOMIT));

        // --- Stop shooting: X button ---

        driverController.x()
            .onTrue(Commands.runOnce(() -> {
                lastState = null;
                new ShoontakeCommand(shoontake, Mode.STOP).schedule();
            }));

  }

  // Change this ^^^^^^^^^^^^^^^^^^^ 
  // We are not using differental drive, we should try to make sure its as compatable with this code 

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }
}
