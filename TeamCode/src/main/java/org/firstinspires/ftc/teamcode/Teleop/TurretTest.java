package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;

@TeleOp
public class TurretTest extends LinearOpMode {
    DcMotor Turret_S;

    ActionManaging action;

    private boolean Out_wasPressed = false;


    @Override
    public void runOpMode() {

        action = new ActionManaging(hardwareMap);
        action.initialize();
        waitForStart();


        if (opModeIsActive()) {

            while (opModeIsActive()) {
                if(gamepad2.b){
                    action.outtake(1.0);

                    Out_wasPressed = true;
                }
                if(!gamepad2.b && Out_wasPressed){
                    action.outtake(0);

                    Out_wasPressed = false;
                }
                    telemetry.update();


                }
            }
        }


}



