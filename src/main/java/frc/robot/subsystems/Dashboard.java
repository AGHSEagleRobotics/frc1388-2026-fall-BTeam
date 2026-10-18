package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.Shoontake;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.commands.ShoontakeCommand.Mode;

public class Dashboard {

    private final Shoontake shoontake;
    private final CommandSwerveDrivetrain drivetrain;

    // Tabs
    private final ShuffleboardTab driverTab = Shuffleboard.getTab("Driver");
    private final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");

    // Entries
    private final GenericEntry shoontakeStateEntry;
    private final GenericEntry matchPhaseEntry;
    private final GenericEntry robotStateEntry;
    private final GenericEntry shootCueEntry;

    // Auto chooser
    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public Dashboard(Shoontake shoontake, CommandSwerveDrivetrain drivetrain) {
        this.shoontake = shoontake;
        this.drivetrain = drivetrain;

        shoontakeStateEntry = driverTab.add("Shoontake State", "STOP")
            .withPosition(0, 0).withSize(2, 1).getEntry();

        matchPhaseEntry = driverTab.add("Match Phase", "DISABLED")
            .withPosition(0, 1).withSize(2, 1).getEntry();

        robotStateEntry = driverTab.add("Robot State", "IDLE")
            .withPosition(2, 0).withSize(2, 1).getEntry();

        shootCueEntry = driverTab.add("Shoot Cue", "HOLD")
            .withPosition(2, 1).withSize(2, 1).getEntry();

        autoTab.add("Auto Selector", autoChooser)
            .withPosition(0, 0).withSize(3, 2);

        NetworkTableInstance.getDefault()
            .getTable("Robot")
            .getEntry("SelectedAuto")
            .setString("None");
    }

    // Auto integration
    public void addAutoOption(String name, Command autoCommand) {
        autoChooser.addOption(name, autoCommand);
    }

    public void setDefaultAuto(String name, Command autoCommand) {
        autoChooser.setDefaultOption(name, autoCommand);
    }

    public Command getSelectedAuto() {
        Command selected = autoChooser.getSelected();
        if (selected != null) {
            NetworkTableInstance.getDefault()
                .getTable("Robot")
                .getEntry("SelectedAuto")
                .setString(selected.getName());
        }
        return selected;
    }

    // Call this from RobotContainer/Robot.periodic
    public void update() {
        updateMatchPhase();
        updateShoontakeState();
        updateRobotStateAndCue();
    }

    private void updateMatchPhase() {
        if (DriverStation.isDisabled()) {
            matchPhaseEntry.setString("DISABLED");
        } else if (DriverStation.isAutonomous()) {
            matchPhaseEntry.setString("AUTO");
        } else if (DriverStation.isTeleop()) {
            double time = DriverStation.getMatchTime();
            if (time <= 30.0) {
                matchPhaseEntry.setString("ENDGAME");
            } else {
                matchPhaseEntry.setString("TELEOP");
            }
        } else {
            matchPhaseEntry.setString("UNKNOWN");
        }
    }

    private void updateShoontakeState() {
        Mode mode = shoontake.getMode();
        shoontakeStateEntry.setString(mode == null ? "NONE" : mode.name());
    }

    private void updateRobotStateAndCue() {
        var speeds = drivetrain.getChassisSpeeds();
        boolean isMoving =
            Math.abs(speeds.vxMetersPerSecond) > 0.05 ||
            Math.abs(speeds.vyMetersPerSecond) > 0.05 ||
            Math.abs(speeds.omegaRadiansPerSecond) > 0.05;

        Mode mode = shoontake.getMode();

        if (mode == Mode.SHORT || mode == Mode.MEDIUM || mode == Mode.LONG) {
            if (!isMoving) {
                robotStateEntry.setString("AIMED");
                shootCueEntry.setString("SHOOT NOW");
            } else {
                robotStateEntry.setString("MOVING");
                shootCueEntry.setString("ALIGNING");
            }
        } else if (mode == Mode.INTAKE) {
            robotStateEntry.setString("INTAKING");
            shootCueEntry.setString("HOLD");
        } else if (mode == Mode.VOMIT) {
            robotStateEntry.setString("VOMITING");
            shootCueEntry.setString("HOLD");
        } else if (mode == Mode.STOP || mode == null) {
            robotStateEntry.setString("IDLE");
            shootCueEntry.setString("HOLD");
        } else {
            robotStateEntry.setString("UNKNOWN");
            shootCueEntry.setString("HOLD");
        }
    }
}
