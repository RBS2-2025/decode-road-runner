package org.firstinspires.ftc.teamcode.Movement;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ActionManaging {
//    Servo lifting;
    public DcMotor Turret_S,Turret_R , IntakeDc;
//    ColorSensor c1, c2, c3;
//
//    Telemetry telemetry;
//    Gamepad gamepad1,gamepad2;
//    IMU imu;
//
//
//    ElapsedTime timer;

    public ActionManaging(DcMotor Turret_S, DcMotor Turret_R, DcMotor IntakeDc) {
        this.Turret_S = Turret_S;
        this.Turret_R = Turret_R;
        this.IntakeDc = IntakeDc;
    }

    public void intake(double power_in) {
        IntakeDc.setPower(power_in);
    }

    public void intake_stop() {
        IntakeDc.setPower(0);
    }
    public void intake_r() {
        IntakeDc.setPower(-1);
    }

    public void turret_rotation() {
    }

    public void outtake(double power){
        Turret_S.setPower(power);
    }

    public void outtake_stop(){
        Turret_S.setPower(0);
        IntakeDc.setPower(0);
    }


}