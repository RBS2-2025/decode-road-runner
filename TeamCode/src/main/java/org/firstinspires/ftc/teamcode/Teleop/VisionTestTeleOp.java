package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Vision.vision;

@TeleOp(name = "Vision Test TeleOp", group = "Test")
public class VisionTestTeleOp extends LinearOpMode {

    private vision visionModule = new vision();
    DcMotor DC;
    @Override
    public void runOpMode() throws InterruptedException {

        // --- Vision 모듈 초기화 ---
        telemetry.addLine("Initializing Limelight...");
        telemetry.update();

        visionModule.VisionModule(hardwareMap, telemetry);

        telemetry.addLine("Limelight Initialized!");
        telemetry.update();
        initialize();
        waitForStart();

        while (opModeIsActive()) {
            visionModule.align(DC, true);

            telemetry.addData("tx", visionModule.tx);

            telemetry.update();
        }

        // --- 정지 시 Limelight 종료 ---
        visionModule.stop();
    }

    void initialize() {

        DC = hardwareMap.dcMotor.get("DC");
        DC.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DC.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
