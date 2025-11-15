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
    DcMotor Turret_S,Turret_M , IntakeDc;
    ColorSensor c1, c2, c3;

    Telemetry telemetry;
    Gamepad gamepad1,gamepad2;
    IMU imu;

    public ActionManaging(HardwareMap hw) {
        this.hardwareMap = hw;
    }
    public void initialize() {

//        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_S = hardwareMap.dcMotor.get("Turret_S");
//        Turret_M = hardwareMap.dcMotor.get("Turret_M");

//        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Turret_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        Turret_M.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    ElapsedTime timer;

    public void intake(double power_in) {
       IntakeDc.setPower(power_in);
    }

    public void intake_stop() {
        IntakeDc.setPower(0);
    }
    public void turret_rotation() {

        //수동, 자동 고민해봐야할듯

    }

    public void outtake(double power_out){
        Turret_S.setPower(power_out);
    }


}