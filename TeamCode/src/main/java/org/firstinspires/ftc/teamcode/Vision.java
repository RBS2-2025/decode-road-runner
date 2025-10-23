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

    Limelight3A limelight;
    List<Integer> colorArray = new ArrayList<>(Arrays.asList(0, 0, 0)); // 초록 1 보라 0

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
            LLResult result = limelight.getLatestResult();
            if (result.isValid()) {
                telemetry.addData("tx", result.getTx()); // 물체가 x축에서 어디있나. // 태그 2개 이상일때 이게 어떤거 출력되는지 ㅁㄹ겠음
                telemetry.addData("ty", result.getTy()); // 얘는 y축

                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) { // 인식된 태그마다 출력되게 반복
                    telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());

                    // getFiducialId -> april tag id
                    // getFamily -> april tag의 패밀리 id 딱히 필요없을듯
                    // getTargetXDegrees = tx
                    // getTargetYDegrees = ty


                    // --- 모티프 aprilTag --- //
                    if (fr.getFiducialId() == 21){
                        telemetry.addData("arr","GPP");
                        colorArray = Arrays.asList(1,0,0);
                    }
                    if (fr.getFiducialId() == 22){
                        telemetry.addData("arr","PGP");
                        colorArray = Arrays.asList(0,1,0);
                    }
                    if (fr.getFiducialId() == 23){
                        telemetry.addData("arr","PPG");
                        colorArray = Arrays.asList(0,0,1);
                    }

                    // --- 골대 aprilTag --- //
                    if (fr.getFiducialId() == 20){
                        telemetry.addData("goal","blue");
                        // 확장 가능성 : tx가 -면 0이 되도록 오른쪽으로 움직여라
                    }
                    if (fr.getFiducialId() == 24){
                        telemetry.addData("goal","red");
                    }
                }


            } else {
                telemetry.addData("Limelight", "No data available");
            }

            telemetry.update();
        }
        limelight.stop();
    }

}

