package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;


@TeleOp(name = "Tele")public class Tele extends LinearOpMode {

    Servo IN_R, IN_L, ROT_R, ROT_L, ALI_R, ALI_L;
    DcMotor OUT_R, OUT_L;
    ColorSensor C1, C2, C3;

    void initialize() {

        OUT_R = hardwareMap.dcMotor.get("OUT_R");
        OUT_L = hardwareMap.dcMotor.get("OUT_L");


        OUT_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        OUT_L.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

        OUT_L.setDirection(DcMotor.Direction.REVERSE);
        IN_L.setDirection(Servo.Direction.REVERSE);
        ROT_L.setDirection(Servo.Direction.REVERSE);
        ALI_L.setDirection(Servo.Direction.REVERSE);

        OUT_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        OUT_L.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        OUT_R.setPower(0);
        OUT_L.setPower(0);
    }
    int cheC1, cheC2, cheC3 = 0;
    ElapsedTime timer;
    public void intake() { // a버튼 누르고 있을때
        if(cheC1==0){
            ROT_R.setPosition(1);//각도 측정 필요
            ROT_L.setPosition(1);

            timer.reset();
            while (timer.time(TimeUnit.SECONDS) < 0.5) {

            }

            IN_R.setPosition(1);//속도 측정 필요
            IN_L.setPosition(1);



            if(C1.red()>100 && C1.green()>100 && C1.blue()>100){ // 측정 필요
                cheC1 = 1;
            }

        }else if(cheC2==0 && cheC1 != 0) {
            ROT_R.setPosition(1);//각도 측정 필요
            ROT_L.setPosition(1);

            timer.reset();
            while (timer.time(TimeUnit.SECONDS) < 0.5) {

            }

            IN_R.setPosition(1);//속도 측정 필요
            IN_L.setPosition(1);

            if(C2.red()>100 && C2.green()>100 && C2.blue()>100){ // 측정 필요
                cheC2 = 1;
            }
        } else if(cheC3 == 0 && cheC2 != 0) {
            ROT_R.setPosition(1);//각도 측정 필요
            ROT_L.setPosition(1);

            timer.reset();
            while (timer.time(TimeUnit.SECONDS) < 0.5) {

            }

            IN_R.setPosition(1);//속도 측정 필요
            IN_L.setPosition(1);

            if(C3.red()>100 && C3.green()>100 && C3.blue()>100){ // 측정 필요
                cheC3 = 1;
            }
        }
    }

    public void align() { // 각도 조정

    }

    public void outtake(){ // b 버튼 눌렀을때

        //sort();
        OUT_R.setPower(100); // 속도 측정
        OUT_L.setPower(100);
    }




    @Override public void runOpMode() {
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
