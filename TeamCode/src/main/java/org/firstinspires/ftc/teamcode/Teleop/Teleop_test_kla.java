package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import org.firstinspires.ftc.teamcode.movement.IMU_Driving;
import org.firstinspires.ftc.teamcode.Vision.vision;

import java.util.concurrent.TimeUnit;

@TeleOp
public class Teleop_test_kla extends LinearOpMode {
    Servo lifting;
    DcMotor Turret_S, Turret_R, IntakeDc, fl, fr, rl, rr;
    ColorSensor c1, c2, c3;
    Limelight3A limelight;

    IMU imu;
    IMU_Driving imu_driving;
    ActionManaging action;
    vision visionModule = new vision();

    public static double intakePower = 1.0;
    public static double outtakePower = 0.15;
    private boolean In_wasPressed = false;
    private boolean InR_wasPressed = false;
    private boolean Out_wasPressed = false;
    private boolean Align_wasPressed = false;
    ElapsedTime timer = new ElapsedTime();

    void TurretRotateTest(){
        double dir = (gamepad2.dpad_right? -1:0) - (gamepad2.dpad_left? -1:0);
        Turret_R.setPower(dir*0.035);
    }

    @Override
    public void runOpMode() {
        initialize();
        //action.initialize();
        timer.reset();

        action = new ActionManaging(Turret_S, Turret_R, IntakeDc);
        imu_driving = new IMU_Driving(fl,fr,rl, rr, imu, telemetry,gamepad1);
        visionModule.VisionModule(hardwareMap, telemetry);

        imu_driving.init();
        imu_driving.getYaw();

        waitForStart();


        if (opModeIsActive()) {

            while (opModeIsActive()) {
                imu_driving.controlWithPad(IMU_Driving.GamepadPurpose.WHOLE);

                //TEST - 나중에 지우기
                TurretRotateTest();

                // intake reverse (gamepad2.x)
                if (gamepad2.x) {
                    action.intake_r();
                    if (!InR_wasPressed) InR_wasPressed = true;

                }
                if (!gamepad2.x && InR_wasPressed) {
                    action.intake_stop();
                    InR_wasPressed = false;
                }

                // intake (gamepad2.a)
                if (gamepad2.a) {
                    action.intake(intakePower);
                    if (!In_wasPressed) In_wasPressed = true;
                }
                if (!gamepad2.a && In_wasPressed) {
                    action.intake_stop();
                    In_wasPressed = false;
                }

                // outtake (gamepad2.b)
                if (gamepad2.b) {
                    if (!Out_wasPressed){
                        timer.reset();
                        Out_wasPressed = true;
                        while(timer.time(TimeUnit.SECONDS) <= 1.5){
                            action.outtake(outtakePower);
                            if(!gamepad2.b){
                                break;
                            }
                        }
                        action.intake(intakePower);
                    }
                    else{
                        action.outtake(outtakePower);
                        action.intake(intakePower);
                    }
                }

                if (!gamepad2.b && Out_wasPressed) {
                    action.outtake_stop();
                    Out_wasPressed = false;
                }

                if (gamepad2.right_bumper){
                    visionModule.align(Turret_R,true);
                    Align_wasPressed = true;
                }
                if (!gamepad2.right_bumper && Align_wasPressed){
                    visionModule.align(Turret_R,true);
                    Align_wasPressed = false;
                }
                    telemetry.update();
                }
            }
        }

    void initialize() {

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


}



