package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import org.firstinspires.ftc.teamcode.Movement.IMU_Driving;
import org.firstinspires.ftc.teamcode.Vision.vision;

@TeleOp(name = "Tele")public class Teleop_test_kla extends LinearOpMode {
    Servo lifting;
    DcMotor Turret_S, Turret_M, IntakeDc, fl, fr, rl, rr;
    ColorSensor c1, c2, c3;
    Limelight3A limelight;
    Telemetry telemetry;
    Gamepad gamepad1, gamepad2;
    IMU imu;
    IMU_Driving imu_driving;
    vision vision;
    ActionManaging action;
    ElapsedTime timer;
    private boolean In_wasPressed = false;
    private boolean Out_wasPressed = false;


    @Override
    public void runOpMode() {

        action = new ActionManaging();
        vision = new vision();
        imu_driving = new IMU_Driving(fl,fr,rl, rr, imu, telemetry,gamepad1);
        waitForStart();

        initialize();
        imu_driving.init();
        imu_driving.getYaw();

        if (opModeIsActive()) {

            while (opModeIsActive()) {
                //imu_driving.move();


                if (gamepad2.a) {
                    action.intake(1);
                    In_wasPressed = true;
                }
                if(!gamepad2.a && In_wasPressed){
                    action.intake_stop();
                    In_wasPressed = false;
                }
                if(gamepad2.b){
                    action.outtake(1);
                    Out_wasPressed = true;
                }
                if(!gamepad2.b && Out_wasPressed){
                    action.outtake_stop();
                    Out_wasPressed = false;
                }
                if (gamepad2.y) {
                    //vision.align();
                }
                if(gamepad2.x){
                    vision.decode();
                }


                    telemetry.update();


                }
            }
        }

    void initialize() {

        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_S = hardwareMap.dcMotor.get("Turret_S");
        Turret_M = hardwareMap.dcMotor.get("Turret_M");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");


        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_M.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}



