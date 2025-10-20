package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.*;

@TeleOp(name = "Vision")
public class Vision extends LinearOpMode {
    Limelight3A limelight;

    @Override
    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0); // april tag 인식 파이프라인
        limelight.start();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                LLResult result = limelight.getLatestResult();
                if (result != null && result.isValid()) {
                    double tx = result.getTx();
                    double ty = result.getTy();

                    telemetry.addData("Target X", tx);
                    telemetry.addData("Target Y", ty);
                } else {
                    telemetry.addData("Limelight", "No Targets");
                }
            }
        }
    }
}
