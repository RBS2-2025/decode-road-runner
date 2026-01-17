package org.firstinspires.ftc.teamcode.Teleop;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Smart Sorting Logic Final", group = "TeleOp")
public class New_Sorting extends LinearOpMode {

    private NormalizedColorSensor[] sensors = new NormalizedColorSensor[3];
    private ActionManaging actionManager;

    private static final double LIFT_TIME = 0.5;
    private final float[] hsvCache = new float[3];

    private boolean isInfiniteSorting = false;
    private int targetColorToggle = 0; // 0: Green, 1: Purple

    private static final String COLOR_GREEN = "GREEN";
    private static final String COLOR_PURPLE = "PURPLE";
    private static final String COLOR_NONE = "NONE";

    @Override
    public void runOpMode() {
        initHardware();

        telemetry.addLine("Ready. Logic: Go Get -> Return c1 -> Lift(Launch).");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            handleInput();

            if (isInfiniteSorting) {
                String currentTarget = (targetColorToggle == 0) ? COLOR_GREEN : COLOR_PURPLE;

                performSmartSorting(currentTarget);

                targetColorToggle = 1 - targetColorToggle;
                actionManager.sleepFor(0.2);
            } else {
                manualControl();
            }
            debugTelemetry();
            telemetry.update();
        }
    }

    // ========================================================
    // [수정된 핵심 로직] Go -> Bring Back -> Launch
    // ========================================================
    private void performSmartSorting(String targetColor) {
        // 1. 타겟 색상 탐색
        List<Integer> validIndices = findMatchingIndices(targetColor);

        if (validIndices.isEmpty()) {
            telemetry.addData("Status", "No " + targetColor + " found.");
            return;
        }

        // 2. 현재 위치에서 가장 가까운 타겟 선정
        int currentWheelIdx = actionManager.getCurrentIndex();
        int bestTargetIdx = findClosestIndex(currentWheelIdx, validIndices);

        telemetry.addData("Sequence", "Get(" + bestTargetIdx + ") -> c1 -> Fire!");
        telemetry.update();

        // ----------------------------------------------------
        // Step 1: 물체가 있는 곳으로 가서 '확보(Pick up/Align)'
        // ----------------------------------------------------
        actionManager.moveToIndex(bestTargetIdx);

        // (필요 시 여기서 Intake를 잠깐 돌리거나 대기할 수 있음)
        // actionManager.sleepFor(0.2);

        // ----------------------------------------------------
        // Step 2: 발사 위치(c1)로 운반 (이미 c1이면 이동 안함)
        // ----------------------------------------------------
        actionManager.moveToIndex(ActionManaging.LAUNCH_POSITION_INDEX);

        // ----------------------------------------------------
        // Step 3: 발사 (Lift는 오직 여기서만)
        // ----------------------------------------------------
        actionManager.operateLift(LIFT_TIME);
    }

    // 센서 색상 판별 로직
    private List<Integer> findMatchingIndices(String targetColor) {
        List<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (detectColorType(sensors[i]).equals(targetColor)) {
                matchingIndices.add(i);
            }
        }
        return matchingIndices;
    }

    private String detectColorType(NormalizedColorSensor sensor) {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvCache);
        float hue = hsvCache[0];
        float sat = hsvCache[1];
        float val = hsvCache[2];

        if (val < 0.1 || sat < 0.25) return COLOR_NONE;
        if (hue >= 70 && hue <= 170) return COLOR_GREEN;
        else if (hue >= 250 && hue <= 340) return COLOR_PURPLE;
        return COLOR_NONE;
    }

    private int findClosestIndex(int current, List<Integer> candidates) {
        int bestIdx = candidates.get(0);
        int minDistance = Math.abs(current - bestIdx);
        for (int idx : candidates) {
            int dist = Math.abs(current - idx);
            if (dist < minDistance) {
                minDistance = dist;
                bestIdx = idx;
            }
        }
        return bestIdx;
    }

    private void initHardware() {
        Servo liftServo = hardwareMap.get(Servo.class, "LiftServo");
        Servo rotateWheel = hardwareMap.get(Servo.class, "RotateWheel");
        sensors[0] = hardwareMap.get(NormalizedColorSensor.class, "c1");
        sensors[1] = hardwareMap.get(NormalizedColorSensor.class, "c2");
        sensors[2] = hardwareMap.get(NormalizedColorSensor.class, "c3");
        for(NormalizedColorSensor s : sensors) s.setGain(10);
        actionManager = new ActionManaging(liftServo, this, rotateWheel);
    }

    private void handleInput() {
        if (gamepad1.y && !isInfiniteSorting) { isInfiniteSorting = true; targetColorToggle = 0; }
        if (gamepad1.x && isInfiniteSorting) { isInfiniteSorting = false; actionManager.stopAll(); }
    }

    private void manualControl() {
        if (gamepad1.a) performSmartSorting(COLOR_GREEN);
        if (gamepad1.b) performSmartSorting(COLOR_PURPLE);
        // c1으로 복귀 테스트
        if (gamepad1.dpad_left) actionManager.moveToIndex(ActionManaging.LAUNCH_POSITION_INDEX);
        if (gamepad1.dpad_up)   actionManager.moveToIndex(1);
        if (gamepad1.dpad_right) actionManager.moveToIndex(2);
    }

    private void debugTelemetry() {
        telemetry.addData("Wheel", actionManager.getCurrentIndex());
        telemetry.addData("ReadyToLaunch", actionManager.getCurrentIndex() == ActionManaging.LAUNCH_POSITION_INDEX);
    }
}