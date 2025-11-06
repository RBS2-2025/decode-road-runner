package org.firstinspires.ftc.teamcode.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Autonomous(name = "Vision For DFG", group = "Linear")
public class FtcDashboard_Camera extends LinearOpMode {
    public static class CameraStreamProcessor implements VisionProcessor, CameraStreamSource { // 대시보드 띄우기?
        private final AtomicReference<Bitmap> lastFrame =
                new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

        @Override
        public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
            continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
        }

        @Override
        public void init(int width, int height, CameraCalibration cameraCalibration) {
            lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
        }

        @Override
        public Object processFrame(Mat frame, long captureTimeNanos) {
            Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(frame, b);
            lastFrame.set(b);
            return null;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int i, int i1, float v, float v1, Object o) {}
    }

    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawTagOutline(true)
                .build();

        final CameraStreamProcessor processor = new CameraStreamProcessor();

        VisionPortal visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // HW map get
                .addProcessor(tagProcessor) // april tag 인식
                .addProcessor(processor) // 대시보드 띄우기?
                .build();

        FtcDashboard dashboard = FtcDashboard.getInstance(); // dashboard Telemetry
        telemetry = dashboard.getTelemetry(); // dashboard Telemetry

        dashboard.startCameraStream(processor, 0);

        telemetry.addLine("AprilTag detection initialized.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            List<AprilTagDetection> detections = tagProcessor.getDetections();

            if (detections.isEmpty()) {
                telemetry.addLine("No tag detected");
            } else {
                for (AprilTagDetection tag : detections) {
                    telemetry.addData("Tag ID", tag.id);
                    telemetry.addData("Pose X (m)", tag.ftcPose.x);
                    telemetry.addData("Pose Y (m)", tag.ftcPose.y); // 거리가 늘어날 떄 얘가 제일 많이 커짐
                    telemetry.addData("Pose Z (m)", tag.ftcPose.z);
                    telemetry.addData("Yaw (deg)", tag.ftcPose.yaw);
                    telemetry.addData("Pitch (deg)", tag.ftcPose.pitch);
                    telemetry.addData("Roll (deg)", tag.ftcPose.roll);
                    telemetry.addLine("----------------------");
                }
            }

            telemetry.update();
            sleep(50);
        }

        visionPortal.close();
    }

}
