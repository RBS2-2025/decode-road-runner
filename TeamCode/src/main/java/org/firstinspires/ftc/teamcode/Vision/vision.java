package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.movement.IMU_Driving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class vision {

    public Limelight3A limelight;
    public List<Integer> colorArray = new ArrayList<>(Arrays.asList(0, 0, 0)); // 초록 1 보라 0
    public Telemetry telemetry;

    public IMU_Driving drive;

    public double tx ;

    public void VisionModule(HardwareMap hardwareMap, Telemetry telemetry) {
        this.limelight = hardwareMap.get(Limelight3A.class, "limelight");
        this.telemetry = telemetry;

        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0); // 0번 파이프라인 (예: AprilTag)
        limelight.start();
    }


    public void stop() {

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

    public void align(DcMotor dc, boolean Blue, double speed){ // 터렛 dc 받기 , Blue goal -> true
        LLResult result = limelight.getLatestResult();

        if (!result.isValid()) {
            dc.setPower(0);
            return;
        }

        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        if (fiducialResults.isEmpty()) {
            dc.setPower(0);
            return;
        }

        int targetId = Blue ? 20 : 24;

        for (LLResultTypes.FiducialResult fr : fiducialResults) {
            if (fr.getFiducialId() != targetId)
                continue;

            telemetry.addData("goal", Blue ? "blue" : "red");

            tx = fr.getTargetXDegrees();

            if (Math.abs(tx) < 3) {
                dc.setPower(0);
            } else if (tx > 0) {
                dc.setPower(-speed);
            } else {
                dc.setPower(speed);
            }

            return;
        }

        dc.setPower(0);
    }

    public void scan(){
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            tx = result.getTx();
            double ty = result.getTy();
            double ta = result.getTa();
            telemetry.addData("tx",tx);
            telemetry.addData("ty",ty);
            telemetry.addData("ta",ta);
            telemetry.addData("Dist",getDistanceFromTage(ta));
        }else{
            tx = 0;
        }
    }

    public double getDistanceFromTage(double ta){
        double scale = 30665.95;
        double distance = (scale / ta);
        return distance;
    }
    // ^ 실패

    // 각도 찾기 https://docs.limelightvision.io/docs/docs-limelight/tutorials/tutorial-estimating-distance

}