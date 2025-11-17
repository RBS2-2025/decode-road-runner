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

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Tele")public class Teleop_test_kla extends LinearOpMode {
    Servo lifting;
    DcMotor fl, fr, rl, rr;
    ColorSensor c1, c2, c3;
    Telemetry telemetry;
    Gamepad gamepad1, gamepad2;
    IMU imu;
    IMU_Driving imu_driving;
    ActionManaging action;
    vision visionModule = new vision();

    public static double intakePower = 1.0;
    public static double outtakePower = 1.0;
    ElapsedTime timer;
    private boolean In_wasPressed = false;
    private boolean Out_wasPressed = false;
    private boolean Align_wasPressed = false;


    @Override
    public void runOpMode() {

        action = new ActionManaging(hardwareMap);
        imu_driving = new IMU_Driving(fl, fr, rl, rr, imu, telemetry, gamepad1);
        visionModule.VisionModule(hardwareMap, telemetry);

        waitForStart();

        action.initialize();
        imu_driving.init();
        imu_driving.getYaw();

        if (opModeIsActive()) {

            while (opModeIsActive()) {
                imu_driving.controlWithPad(IMU_Driving.GamepadPurpose.WHOLE);

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
                    if (!Out_wasPressed) {
                        timer.reset();
                        Out_wasPressed = true;

                        while (timer.time(TimeUnit.SECONDS) <= 1.5) {
                            action.outtake(outtakePower);
                            if (!gamepad2.b) {
                                break;
                            }
                        }
                        action.intake(intakePower);
                    } else {
                        action.outtake(outtakePower);
                        action.intake(intakePower);
                    }
                }
                if (!gamepad2.b && Out_wasPressed) {
                    action.outtake_stop();
                    Out_wasPressed = false;
                }

                // align (gamepad2.y)
                if (gamepad2.y){
                    visionModule.align(action.Turret_R,true);
                    Align_wasPressed = true;
                }
                if (!gamepad2.y && Align_wasPressed){
                    visionModule.align(action.Turret_R,true);
                    Align_wasPressed = false;
                }

                if (gamepad2.x){
                    visionModule.decode();
                }

                telemetry.update();


            }
        }
    }
}



