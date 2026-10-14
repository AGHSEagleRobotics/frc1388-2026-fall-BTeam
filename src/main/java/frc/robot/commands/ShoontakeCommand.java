// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.Shoontake;
import edu.wpi.first.wpilibj2.command.Command;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShoontakeCommand extends Command {
 public enum Mode {
        LONG,
        MEDIUM,
        SHORT,
        INTAKE,
        VOMIT,
        STOP
    }

     private final Shoontake shoontake;
    private final Mode mode;

     public ShoontakeCommand(Shoontake shoontake, Mode mode) {
        this.shoontake = shoontake;
        this.mode = mode;
        addRequirements();
    }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shoontake.clearState();

        switch (mode) {
            case LONG:
                shoontake.setShoontake(1.00);
                shoontake.setIndexer(-1.00);
                break;

            case MEDIUM:
                shoontake.setShoontake(0.95);
                shoontake.setIndexer(-0.95);
                break;

            case SHORT:
                shoontake.setShoontake(0.90);
                shoontake.setIndexer(-0.90);
                break;

            case INTAKE:
                shoontake.setShoontake(-0.75);
                shoontake.setIndexer(0.85);
                break;

            case VOMIT:
                shoontake.setShoontake(0.85);
                shoontake.setIndexer(0.75);
                break;

            case STOP:
                shoontake.setShoontake(-0.30);
                shoontake.setIndexer(0.40);
                break;
        }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
