package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "Auto_Actions_AfterTime")
public final class Auto extends LinearOpMode {

    // 예시 좌표 (인치, 헤딩 라디안)
    private static final Pose2d START_POSE = new Pose2d(-72, 0, Math.PI/2);
    private static final Vector2d P1 = new Vector2d(-48,  12);
    private static final Vector2d P2 = new Vector2d(-24,  24);
    private static final Vector2d P3 = new Vector2d(  0,  24);
    private static final Vector2d P4 = new Vector2d( 24,  12);

    // 하드웨어 (예시)
    private Servo in_takeServo;
    private DcMotor out_takeDc;

    // -------- 액추에이터 액션 정의(InstantAction 추천) --------
    private Action intakeOn() {
        return new InstantAction(() -> {
            in_takeServo.setPosition(1.0);
        });
    }

    private Action intakeOff() {
        return new InstantAction(() -> {
            in_takeServo.setPosition(0.0);
        });
    }

    // sorting system을 비롯한 다양한 동작 함수 작성


    @Override
    public void runOpMode() throws InterruptedException {

        // 드라이브 & 하드웨어 초기화
        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);
        in_takeServo = hardwareMap.get(Servo.class, "in_takeServo");
        out_takeDc = hardwareMap.get(DcMotor.class,"out_takeDc");


        waitForStart();
        if (isStopRequested()) return;

        // -------- 주행 + 시간 마커(afterTime) 예시 --------
        Action routine = drive.actionBuilder(START_POSE)
                .splineTo(P1, Math.PI/2)
                .afterTime(0.3, intakeOn())
                .splineTo(P2, Math.PI/2)
                .afterTime(1.2, intakeOn())
                .splineTo(P3, Math.PI/2)
                .stopAndAdd(new SleepAction(0.2))
                .splineTo(P4, Math.PI/2)
                .afterTime(2.7, intakeOff())
                .afterTime(2.7, intakeOn())
                .splineTo(new Vector2d(36, 12), 0.0)

                .build();

        // 시퀀스 한 번 실행
        Actions.runBlocking(routine);

        // 종료 대기
        while (opModeIsActive()) idle();
    }
}
