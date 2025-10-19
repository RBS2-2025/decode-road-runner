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
    private static final Vector2d P1 = new Vector2d(-36,  36);
    private static final Vector2d P2 = new Vector2d(-12,  36);
    private static final Vector2d P3 = new Vector2d(  12,  36);
    private static final Vector2d G1 = new Vector2d( -48,  0);
    private static final Vector2d G2 = new Vector2d( 0,  0);


    // 하드웨어 (예시)
    private Servo servo;
    private DcMotor Dc;

    // -------- 액추에이터 액션 정의(InstantAction 추천) --------
    private Action intakeOn() {
        return new InstantAction(() -> {
        });
    }

    private Action sorting() {
        return new InstantAction(() -> {

        });
    }

    private Action out_take() {
        return new InstantAction(() -> {

        });
    }
    private Action align() {
        return new InstantAction(() -> {

        });
    }



    // sorting system을 비롯한 다양한 동작 함수 작성


    @Override
    public void runOpMode() throws InterruptedException {

        // 드라이브 & 하드웨어 초기화
        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);
        servo = hardwareMap.get(Servo.class, "in_takeServo");
        Dc = hardwareMap.get(DcMotor.class,"out_takeDc");


        waitForStart();
        if (isStopRequested()) return;

        // -------- 주행 + 시간 마커(afterTime) 예시 --------
        Action routine = drive.actionBuilder(START_POSE)
                //보관 중인 Artifact 발사
                .splineTo(G1, Math.PI/4)
                .stopAndAdd(align())
                .stopAndAdd(out_take())

                // 1차 먹기(패턴에 따라서 p1, p2, p3 변경)
                .splineTo(P1, Math.PI)
                .splineToConstantHeading(new Vector2d(-36, 48), Math.PI)
                .afterTime(0.3, intakeOn())

                // 1차 발사
                .splineTo(G2, Math.PI/4)
                .stopAndAdd(align()) //aftertime 쓰면서 align 할 수 있는 확인 필요
                .stopAndAdd(out_take())

                //2차 수집
                .splineTo(P2, Math.PI)
                .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                .afterTime(0.3, intakeOn()) // aftertime 은 배터리의 상태에 영향을 받거나 오차가 생길 수도 있어서 afterDisp(), 거리 관련 함수로 사용할 방법도 찾아야함

                // 2차 발사
                .splineTo(G2, Math.PI/4)
                .stopAndAdd(align())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())

               //3차 수집
                .splineTo(P3, Math.PI)
                .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                .afterTime(0.3, intakeOn())

                // 3차 발사
                .splineTo(G2, Math.PI/4)
                .stopAndAdd(align())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())
                .afterTime(0.5,sorting())
                .stopAndAdd(out_take())

                // 시간 체크 후 human player zone까지 들어가서 체크(들어가는 공 갯수랑 정확도도 보면서 이후 로직 정해야할듯)

                //주차
                .splineTo(P1, Math.PI/2)

                .build();

        // 시퀀스 한 번 실행
        Actions.runBlocking(routine);

        // 종료 대기
        while (opModeIsActive()) idle();
    }
}
