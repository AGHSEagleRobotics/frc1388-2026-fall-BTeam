// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

public class Shoontake extends SubsystemBase {
  /** Creates a new Shoontake. */

  private final TalonFX m_shoontakeMotor1;
  private final TalonFX m_indexerMotor1;

  public Shoontake(TalonFX shoontakeMotor1, TalonFX indexerMotor1) {
    // Shoontake.configFactoryDefault();

   
    m_shoontakeMotor1 = shoontakeMotor1;
    m_indexerMotor1 = indexerMotor1;
    // TalonFXConfigurator configurator = new TalonFXConfigurator(1);
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    shoontakeMotor1.getConfigurator().apply(config);
    indexerMotor1.getConfigurator().apply(config);
    //Good place to add code for getting information or setting from an existing config instead of creating a new one
  }

    public void setPower(double power){
      m_shoontakeMotor1.set(power);
      m_indexerMotor1.set(power);
      power = 0.7; 
    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
