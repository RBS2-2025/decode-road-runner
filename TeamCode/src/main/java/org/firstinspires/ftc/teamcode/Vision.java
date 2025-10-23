package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TeleOp(name = "Vision")
public class Vision extends LinearOpMode {

    public Limelight3A limelight;
    public List<Integer> colorArray = new ArrayList<>(Arrays.asList(0, 0, 0)); // 초록 1 보라 0

    @Override
    public void runOpMode() throws InterruptedException
    {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0); // 0번이 april tag 인식하는 파이프라인

        limelight.start();

        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (colorArray.equals(Arrays.asList(0, 0, 0))) {
                decode();
            }
            telemetry.update();

        }
        limelight.stop();
    }

    public void decode(){
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) { // 인식된 태그마다 출력되게 반복
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());

                // --- 모티프 aprilTag --- //
                if (fr.getFiducialId() == 21) {
                    telemetry.addData("arr", "GPP");
                    colorArray = Arrays.asList(1, 0, 0);
                }
                if (fr.getFiducialId() == 22) {
                    telemetry.addData("arr", "PGP");
                    colorArray = Arrays.asList(0, 1, 0);
                }
                if (fr.getFiducialId() == 23) {
                    telemetry.addData("arr", "PPG");
                    colorArray = Arrays.asList(0, 0, 1);
                }
            }
        }
    }

    public void align(){
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) { // 인식된 태그마다 출력되게 반복
                telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());

                // --- 골대 aprilTag --- //
                if (fr.getFiducialId() == 20){
                    telemetry.addData("goal","blue");
                }
                if (fr.getFiducialId() == 24){
                    telemetry.addData("goal","red");
                }
            }
        }
    }

    // 각도 찾기 https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance

}

