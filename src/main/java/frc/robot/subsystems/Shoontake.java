// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.controls.DutyCycleOut;
// import com.ctre.phoenix6.controls.Follower;
// import com.ctre.phoenix6.controls.NeutralOut;
// import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
// import edu.wpi.first.units.measure.Angle;
// import edu.wpi.first.units.measure.AngularVelocity;
// import edu.wpi.first.units.measure.Current;
// import edu.wpi.first.units.measure.Temperature;
// import edu.wpi.first.units.measure.Voltage;

public class Shoontake extends SubsystemBase {
  /** Creates a new Shoontake. */

    private static final int SHOONTAKE_MOTOR_ID = 15;
    private static final int INDEXER_MOTOR_ID   = 14;
    // private static final int SHOONTAKE_CANCODER_ID = 12;
    private final TalonFX shoontakeMotor;
    private final TalonFX indexerMotor;
    // private final CANcoder shoontakeEncoder;
    // ShoontakePower was originally -0.30
    // IndexerPower was originally 0.40
    private double shoontakePower = 0.15; // The following are the default power values on the shoontake and indexer motors
    private double indexerPower   = 0.20;  // :P
  
  public Shoontake() {
        shoontakeMotor = new TalonFX(SHOONTAKE_MOTOR_ID);
        indexerMotor   = new TalonFX(INDEXER_MOTOR_ID);
        // shoontakeEncoder = new CANcoder(SHOONTAKE_CANCODER_ID);

        shoontakeMotor.setControl(new DutyCycleOut(shoontakePower));
        indexerMotor.setControl(new DutyCycleOut(indexerPower));
    
}
    // TalonFXConfigurator configurator = new TalonFXConfigurator(1);

    //Good place to add code for getting information or setting from an existing config instead of creating a new one
  

   public void setShoontake(double power) {
        shoontakePower = power;
        shoontakeMotor.setControl(new DutyCycleOut(power)); //Duty cycle really? Velocity control with KV and small KP will do great. Just set target rpm for shooter, convert to motor rpm via gear ratio, and do simple kv tuning aka rotation speed per volt. -Perrin
    }

     public void setIndexer(double power) {
        indexerPower = power;
        indexerMotor.setControl(new DutyCycleOut(power));
    }
     public double getShoontakePower() {
        return shoontakePower;
    }

     public double getIndexerPower() {
        return indexerPower;
    }

     public double getShoontakeRPM() {
        return shoontakeMotor.getVelocity().getValueAsDouble();
    }

    public double getIndexerRPM() {
        return indexerMotor.getVelocity().getValueAsDouble();
    }

    // CANCoder angle (if you want it)
    // public double getShoontakeAngleDegrees() {
    //     return shoontakeEncoder.getPosition().getValueAsDouble();
    // }

    // Clear to default "idle" state
    public void clearState() {
        setShoontake(0.15); // Old value -0.30
        setIndexer(0.20); // Old value 0.40
    }
  

     
   
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

