package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Autonomous(name = "DFGAuto")
public class DFGAuto extends LinearOpMode {
    public static int distance = 1000;

    DcMotor fl,fr,rl,rr;
    @Override
    public void runOpMode() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        rl = hardwareMap.dcMotor.get("rl");
        rr = hardwareMap.dcMotor.get("rr");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        rl.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        waitForStart();
        if (opModeIsActive()) {
            // Pre-run
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            while (opModeIsActive()) {
                // OpMode loop
                if(fl.getCurrentPosition() <= distance){
                    fl.setPower(0.7);
                    fr.setPower(0.7);
                    rl.setPower(0.7);
                    rr.setPower(0.7);
                }
                else{
                    fl.setPower(0);
                    fr.setPower(0);
                    rl.setPower(0);
                    rr.setPower(0);
                }
            }
        }
    }
}
