package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Movement.IMU_Driving;

@TeleOp(name = "IMU_Test")
public class IMU_Test extends LinearOpMode {
    DcMotor lf, rf, lr, rr;
    IMU imu;
    IMU_Driving imu_d ;
    @Override
    public void runOpMode() {
        lf = hardwareMap.dcMotor.get("lf");
        rf = hardwareMap.dcMotor.get("rf");
        lr = hardwareMap.dcMotor.get("lr");
        rr = hardwareMap.dcMotor.get("rr");
        imu = hardwareMap.get(IMU.class,"imu");

        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lr.setDirection(DcMotorSimple.Direction.REVERSE);

        imu_d = new IMU_Driving(lf,rf,lr,rr,imu,telemetry,gamepad1);
        imu_d.init();
        waitForStart();
        if (opModeIsActive()) {
            // Pre-run
            while (opModeIsActive()) {
                // OpMode loop
                imu_d.rota();
                telemetry.update();
            }
        }
    }
}
