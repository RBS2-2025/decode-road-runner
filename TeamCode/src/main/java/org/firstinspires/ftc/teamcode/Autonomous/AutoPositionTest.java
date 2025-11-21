package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Movement.ActionManaging;
import org.firstinspires.ftc.teamcode.Vision.vision;


@Autonomous
public final class AutoPositionTest extends LinearOpMode {

    // blue 기준 좌표
    public static double Robot_X = 17.5;
    public static double Robot_Y = 16;
    private static final Pose2d START_POSE = new Pose2d(0, -72 + Robot_Y/2, Math.PI/2);

    private static final Vector2d BP1 = new Vector2d(-36 + Robot_X/2, -36);

    private static final Vector2d BP2 = new Vector2d(-36 + Robot_X/2, -12);

    private static final Vector2d BP3 = new Vector2d(- 36 + Robot_X/2, 12);

    private static final Vector2d G1 = new Vector2d(0, -60 + Robot_Y/2);

    private static final Vector2d G2 = new Vector2d(0, 0);
    private static final Vector2d G3 = new Vector2d(-12 -Robot_X/2,12+Robot_Y/2);



    ActionManaging action;

    public static final double FEED_SEC = 3;        // 피딩 시간
    public static final double ALIGN_SPEED = 0.3;     // 터렛 정렬 스피드




    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);


        waitForStart();
        if (isStopRequested()) return;


        double dx = 0 - START_POSE.position.x;
        double dy = 0 - START_POSE.position.y;
        double tangentToZero = Math.atan2(dy, dx);

        Action path = drive.actionBuilder(new Pose2d(0, -72, Math.PI/2))
                .splineTo(new Vector2d(0,0),Math.PI/2)
                .splineTo(BP1,Math.PI)
                .splineTo(BP2,Math.PI)
                .splineTo(BP3,Math.PI)
                .build();

        Actions.runBlocking(new SequentialAction(path));



        while (opModeIsActive()) idle();
    }



}