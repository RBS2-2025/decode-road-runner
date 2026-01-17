package org.firstinspires.ftc.teamcode.Movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ActionManaging {
    public Servo lifting, wheel;
    public DcMotor OuttakeDc, IntakeDc;
    private ElapsedTime timer;
    private LinearOpMode opMode;

    // ====================================================================
    // [보정 데이터] 5-Turn Servo Calibration
    // ====================================================================
    private static final double SERVO_CENTER = 0.5;
    private static final double OFFSET_120_DEG = 0.0772;

    // [중요] c1(Index 0)이 발사 위치(Launch Position)
    public static final int LAUNCH_POSITION_INDEX = 0;

    // Index 0: c1 (Launch Position) -> 0.4228
    // Index 1: c2 (Center)          -> 0.5000
    // Index 2: c3 (Right)           -> 0.5772
    private static final double[] ARTIFACT_POSITIONS = {
            SERVO_CENTER - OFFSET_120_DEG,
            SERVO_CENTER,
            SERVO_CENTER + OFFSET_120_DEG
    };

    private int currentWheelIndex = 1;

    public ActionManaging(Servo lift, LinearOpMode opMode, Servo wheel) {
        this.lifting = lift;
        this.opMode = opMode;
        this.wheel = wheel;
        this.timer = new ElapsedTime();

        // 초기화: 발사 위치(c1)에서 시작
        wheel.setPosition(ARTIFACT_POSITIONS[LAUNCH_POSITION_INDEX]);
        currentWheelIndex = LAUNCH_POSITION_INDEX;
    }

    // ----- Lift Control (발사) -----
    public void operateLift(double duration) {
        lifting.setPosition(1.0); // UP (Fire)
        sleepFor(duration);
        lifting.setPosition(0.0); // DOWN (Reset)
        sleepFor(0.2);
    }

    // ----- RotateWheel Control -----
    public void moveToIndex(int targetIndex) {
        if (targetIndex < 0 || targetIndex >= ARTIFACT_POSITIONS.length) return;

        if (currentWheelIndex != targetIndex) {
            wheel.setPosition(ARTIFACT_POSITIONS[targetIndex]);
            currentWheelIndex = targetIndex;
            // 이동 후 안정화 시간 (중요)
            sleepFor(0.4);
        }
    }

    // 현재 바퀴 인덱스 반환
    public int getCurrentIndex() {
        return currentWheelIndex;
    }

    // ----- Standard Methods -----
    public void intake(double power) { if(IntakeDc != null) IntakeDc.setPower(power); }
    public void stopIntake() { if(IntakeDc != null) IntakeDc.setPower(0); }
    public void outtake(double power) { if(OuttakeDc != null) OuttakeDc.setPower(power); }
    public void stopOuttake() {
        if(OuttakeDc != null) OuttakeDc.setPower(0);
        if(IntakeDc != null) IntakeDc.setPower(0);
    }

    public void stopAll() {
        stopIntake();
        stopOuttake();
        lifting.setPosition(0);
        moveToIndex(LAUNCH_POSITION_INDEX); // 종료 시 발사 위치로 정렬
    }

    public void sleepFor(double seconds) {
        timer.reset();
        while (opMode.opModeIsActive() && timer.seconds() <= seconds) { }
    }
}