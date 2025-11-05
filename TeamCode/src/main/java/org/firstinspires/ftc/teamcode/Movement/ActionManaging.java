package org.firstinspires.ftc.teamcode.Movement;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ActionManaging {

    Servo lifting;
    DcMotor Turret_R,Turret_L,Turret_M , IntakeDc;
    ColorSensor c1, c2, c3;

    Telemetry telemetry;
    Gamepad gamepad1,gamepad2;
    IMU imu;


    void initialize() {

        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_L = hardwareMap.dcMotor.get("Turret_L");
        Turret_R = hardwareMap.dcMotor.get("Turret_R");
        Turret_M = hardwareMap.dcMotor.get("Turret_M");

        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_M.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    ElapsedTime timer;

    public void intake(double power) {
       IntakeDc.setPower(power);
    }

    public void intake_stop() {
        IntakeDc.setPower(0);
    }
    public void turret_rotation() {

        //수동, 자동 고민해봐야할듯

    }

    public void outtake(double power){
        Turret_L.setPower(power);
        Turret_R.setPower(-power);

    }

    public void outtake_stop(){
        Turret_L.setPower(0);
        Turret_R.setPower(0);

    }
}