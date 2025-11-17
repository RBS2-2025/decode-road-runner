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

    private HardwareMap hardwareMap;
    Servo lifting;
    public DcMotor Turret_S,Turret_R , IntakeDc;
    ColorSensor c1, c2, c3;

    Telemetry telemetry;
    Gamepad gamepad1,gamepad2;
    IMU imu;

    public ActionManaging(HardwareMap hw) {
        this.hardwareMap = hw;
    }
    public void initialize() {

        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_S = hardwareMap.dcMotor.get("Turret_S");
        Turret_R = hardwareMap.dcMotor.get("Turret_R");

        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    ElapsedTime timer;

    public void intake(double power_in) {
        IntakeDc.setPower(power_in);
    }

    public void intake_stop() {
        IntakeDc.setPower(0);
    }
    public void intake_r() {
        IntakeDc.setPower(0.3);
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