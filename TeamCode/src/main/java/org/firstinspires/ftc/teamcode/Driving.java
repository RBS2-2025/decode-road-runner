package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Driving")
public class Driving extends LinearOpMode {

    DcMotor lf, rf, lr, rr;

    void initialize(){
        lf = hardwareMap.dcMotor.get("FL");
        rf = hardwareMap.dcMotor.get("FR");
        lr = hardwareMap.dcMotor.get("RL");
        rr = hardwareMap.dcMotor.get("RR");

        rf.setDirection(DcMotor.Direction.FORWARD);
        rr.setDirection(DcMotor.Direction.FORWARD);
        lf.setDirection(DcMotor.Direction.REVERSE);
        lr.setDirection(DcMotor.Direction.REVERSE);

        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        lf.setPower(0);
        rf.setPower(0);
        lr.setPower(0);
        rr.setPower(0);
    }

    void move(double x, double y, double r, double spd){
        lf.setPower(spd*(x-y+r)/3);
        rf.setPower(spd*(-x-y-r)/3);
        lr.setPower(spd*(-x-y+r)/3);
        rr.setPower(spd*(x-y-r)/3);
    }

    @Override
    public void runOpMode() {
        waitForStart();

        initialize();
        if (opModeIsActive()) {
            // Pre-run
            while (opModeIsActive()) {
                // OpMode loop
            }
        }
    }
}
