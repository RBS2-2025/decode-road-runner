package org.firstinspires.ftc.teamcode.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import org.firstinspires.ftc.teamcode.Vision.vision;

// === Dashboard import ===
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.config.Config;

import java.util.concurrent.TimeUnit;

@TeleOp
@Config   // Dashboard 설정 클래스
public class TurretTest extends LinearOpMode {

    // === Dashboard에서 수정할 값 ===
    public static double intakePower = 1.0;
    public static double outtakePower = 1.0;

    ActionManaging action;
    vision visionModule = new vision();

    private boolean In_wasPressed = false;
    private boolean Out_wasPressed = false;
    private boolean Align_wasPressed = false;

    ElapsedTime timer = new ElapsedTime();



    @Override
    public void runOpMode() {

        timer.reset();
//        action = new ActionManaging(hardwareMap);
//        action.initialize();
        visionModule.VisionModule(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()) {

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
                        action.outtake();
                        if(!gamepad2.b){
                            break;
                        }
                    }
                    action.intake(intakePower);
                }
                else{
                    action.outtake();
                    action.intake(intakePower);
                }
            }
            if (!gamepad2.b && Out_wasPressed) {
                action.outtake_stop();
                Out_wasPressed = false;
            }

            visionModule.scan();

//            if (gamepad2.right_bumper){
//                visionModule.align(action.Turret_R,true);
//                Align_wasPressed = true;
//            }
//            if (!gamepad2.right_bumper && Align_wasPressed){
//                visionModule.align(action.Turret_R,true);
//                Align_wasPressed = false;
//            }
            telemetry.update();

        }
    }
}
