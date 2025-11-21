package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import org.firstinspires.ftc.teamcode.Vision.vision;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto_Actions_AfterTime")
public final class Auto_Blue_small extends LinearOpMode {

    // 예시 좌표 (인치, 헤딩 라디안)
    //blue 기준 코드
    private static final Pose2d START_POSE = new Pose2d(-72, 0, Math.PI/2);
    private static final Vector2d BP1 = new Vector2d(-36,  36);
    private static final Vector2d BP2 = new Vector2d(-12,  36);
    private static final Vector2d BP3 = new Vector2d(  12,  36);
    private static final Vector2d G1 = new Vector2d( -48,  0);
    private static final Vector2d G2 = new Vector2d( 0,  0);


    // 하드웨어
    Servo lifting;
    DcMotor Turret_S, Turret_M, IntakeDc, fl, fr, rl, rr;
    ColorSensor c1, c2, c3;
    Limelight3A limelight;
    Telemetry telemetry;
    Gamepad gamepad1, gamepad2;
    IMU imu;
    // IMU_Driving imu_driving;
    vision vision;
    ActionManaging action;
    ElapsedTime timer;
    private boolean In_wasPressed = false;
    private boolean Out_wasPressed = false;
    public static double intakePower = 1.0;
    public static double outtakePower_L = 1;
    public static double outtakePower_N = 0.1;



    void initialize() {

        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_S = hardwareMap.dcMotor.get("Turret_S");
        Turret_M = hardwareMap.dcMotor.get("Turret_M");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");


        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_M.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }



    @Override
    public void runOpMode() throws InterruptedException {

        // 드라이브 & 하드웨어 초기화
        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);
        initialize();

        waitForStart();
        if (isStopRequested()) return;

        Action routine = drive.actionBuilder(START_POSE)
                //보관 중인 Artifact 발사
                .splineTo(G1, Math.PI/4)
             //   .afterTime(0.1, new InstantAction(()-> {vision.align();}))
                .afterTime(1.5, new InstantAction(() -> {action.outtake(1);}))
                .afterTime(1.2, new InstantAction(()-> {action.intake(intakePower);}))
                .afterTime(3, new InstantAction(()-> {action.outtake_stop();}))
                .afterTime(0.1,new InstantAction(()-> {action.intake_stop();}))

                // 1차 먹기(패턴에 따라서 p1, p2, p3 변경)
                .splineTo(BP1, Math.PI)
                .afterTime(0.5, new InstantAction(() -> {action.intake(intakePower);}))
                .splineToConstantHeading(new Vector2d(-36, 48), Math.PI)
                .afterTime(2, new InstantAction(()-> {action.intake_stop();}))


                // 1차 발사
                .splineTo(G2, Math.PI/4)
                .afterTime(2, new InstantAction(() -> {action.outtake(1)
                ;}))
                .afterTime(1.2, new InstantAction(()-> {action.intake(intakePower);}))
                .afterTime(3, new InstantAction(() -> {action.outtake_stop();}))
                .afterTime(0.1,new InstantAction(()-> {action.intake_stop();}))

                //2차 수집
                .splineTo(BP2, Math.PI)
                .afterTime(0.3, new InstantAction(() -> {action.intake(intakePower);})) // aftertime 은 배터리의 상태에 영향을 받거나 오차가 생길 수도 있어서 afterDisp(), 거리 관련 함수로 사용할 방법도 찾아야함
                .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                .afterTime(3, new InstantAction(()-> {action.intake_stop();}))

                // 2차 발사
                .splineTo(G2, Math.PI/4)
             //   .afterTime(0.2, new InstantAction(() -> {vision.align(Turret_M, boolean blue);}))
                .afterTime(2.5,new InstantAction(() -> {action.outtake(1);}))
                .afterTime(1.2, new InstantAction(()-> {action.intake(intakePower);}))
                .afterTime(3,new InstantAction(() -> {action.outtake_stop();}))
                .afterTime(0.1, new InstantAction(() -> {action.intake_stop();}))

                //3차 수집
                .splineTo(BP3, Math.PI)
                .afterTime(0.5, new InstantAction(() -> {action.intake(intakePower);}))
                .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                .afterTime(2.5, new InstantAction(() -> {action.intake_stop();}))

                // 3차 발사
                .splineTo(G2, Math.PI/4)
                //.afterTime(0.2, new InstantAction(()-> {vision.align();}))
                .afterTime(2,new InstantAction(()-> {action.outtake(1);}))
                .afterTime(1.2, new InstantAction(()-> {action.intake(intakePower);}))
                .afterTime(3, new InstantAction((()-> {action.outtake_stop();})))
                .afterTime(0.1, new InstantAction(()-> {action.intake_stop();}))


                // 시간 체크 후 human player zone까지 들어가서 체크(들어가는 공 갯수랑 정확도도 보면서 이후 로직 정해야할듯)

                //주차

                .splineTo(new Vector2d(-48,-32),Math.PI/2)

                .build();

        // 시퀀스 한 번 실행
        Actions.runBlocking(routine);

        // 종료 대기
        while (opModeIsActive()) idle();
    }
}