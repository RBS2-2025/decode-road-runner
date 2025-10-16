package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.action.Action;
import com.acmerobotics.roadrunner.action.InstantAction;   // v1 RR에서 제공
import com.acmerobotics.roadrunner.action.SleepAction;    // (선택) 잠깐 대기 액션
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
    private Servo claw;
    private DcMotor intakeMotor;

    // -------- 액추에이터 액션 정의(InstantAction 추천) --------
    private Action intakeOn() {
        return new InstantAction(() -> {
            intakeMotor.setPower(1.0);
        });
    }

    private Action intakeOff() {
        return new InstantAction(() -> {
            intakeMotor.setPower(0.0);
        });
    }

    private Action clawOpen() {
        return new InstantAction(() -> claw.setPosition(0.8));
    }

    private Action clawClose() {
        return new InstantAction(() -> claw.setPosition(0.2));
    }

    @Override
    public void runOpMode() throws InterruptedException {

        // 드라이브 & 하드웨어 초기화
        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);
        claw = hardwareMap.get(Servo.class, "claw");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        claw.setPosition(0.2);
        intakeMotor.setPower(0.0);

        waitForStart();
        if (isStopRequested()) return;

        // -------- 주행 + 시간 마커(afterTime) 예시 --------
        Action routine = drive.actionBuilder(START_POSE)
                // 첫 번째 수거 지점으로 스플라인
                .splineTo(P1, Math.PI/2)

                // 주행 시작 후 0.3초 뒤 인테이크 ON
                .afterTime(0.3, intakeOn())

                // 두 번째 지점
                .splineTo(P2, Math.PI/2)

                // 주행 시작 후 1.2초가 지난 시점에 집게 열기
                .afterTime(1.2, clawOpen())

                // 세 번째 지점
                .splineTo(P3, Math.PI/2)

                // 필요하면 현재 시점에서 잠깐 대기하는 액션을 삽입 (정밀 픽업 등)
                .stopAndAdd(new SleepAction(0.2))

                // 네 번째 지점
                .splineTo(P4, Math.PI/2)

                // 주행 시작 후 2.7초에 인테이크 OFF + 집게 닫기 (동시에 예약 가능)
                .afterTime(2.7, intakeOff())
                .afterTime(2.7, clawClose())

                // 파킹으로 이동
                .splineTo(new Vector2d(36, 12), 0.0)

                .build();

        // 시퀀스 한 번 실행
        Actions.runBlocking(routine);

        // 종료 대기
        while (opModeIsActive()) idle();
    }
}
