package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.movement.IMU_Driving;

@Config
@TeleOp(name = "IMU_Test")
public class IMU_Test extends LinearOpMode {
    DcMotor fl, fr, rl, rr;
    IMU imu;
    IMU_Driving imu_d ;

    public static double RotateSlowThreshold = 15;

    @Override
    public void runOpMode() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        rl = hardwareMap.dcMotor.get("rl");
        rr = hardwareMap.dcMotor.get("rr");
        imu = hardwareMap.get(IMU.class,"imu");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        rl.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu_d = new IMU_Driving(fl,fr,rl,rr,imu,telemetry,gamepad1);
        imu_d.init();
        imu_d.rotateSlowThreshold = RotateSlowThreshold;
        waitForStart();
        if (opModeIsActive()) {
            // Pre-run
            while (opModeIsActive()) {
                // OpMode loop
                imu_d.controlWithPad(IMU_Driving.GamepadPurpose.WHOLE);
                telemetry.update();
            }
        }
    }
}
