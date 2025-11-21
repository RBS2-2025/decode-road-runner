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
public final class Auto_Blue_small extends LinearOpMode {

    // blue 기준 좌표
    public static double Robot_X = 16;
    public static double Robot_Y = 17.5;
    private static final Pose2d START_POSE = new Pose2d(-72 + Robot_X/2, 0, Math.PI/2);
    private static final Vector2d BP1 = new Vector2d(-36 + Robot_X/2,  36 - Robot_Y/2);
    private static final Vector2d BP2 = new Vector2d(-12 + Robot_X/2,  36 - Robot_Y/2);
    private static final Vector2d BP3 = new Vector2d(12 - Robot_X/2,  36- Robot_Y/2);
    private static final Vector2d G1 = new Vector2d( -48 + Robot_X/2,  0);
    private static final Vector2d G2 = new Vector2d( 0,  0);

    Servo lifting;
    DcMotor Turret_S, Turret_R, IntakeDc;
    Limelight3A limelight;

    vision visionModule;
    ActionManaging action;

    public static double intakePower = 1.0;
    public static final double FEED_SEC = 0.6;        // 피딩 시간
    public static final double ALIGN_SPEED = 0.3;     // 터렛 정렬 스피드


    void initialize() {
        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        Turret_S = hardwareMap.dcMotor.get("Turret_S");
        Turret_R = hardwareMap.dcMotor.get("Turret_R");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        IntakeDc.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Turret_R.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, START_POSE);
        initialize();


        action = new ActionManaging(Turret_S, Turret_R, IntakeDc);
        visionModule = new vision();
        visionModule.VisionModule(hardwareMap, telemetry);

        waitForStart();
        if (isStopRequested()) return;


        Action routine = new SequentialAction(

                // 보유 공 발사
                moveSpinAlignShoot(
                        drive.actionBuilder(START_POSE)
                                .splineTo(G1, Math.PI/4)
                                .build(),
                        true ,
                        1
                )   /**  ,


                //  1차 수집
                drive.actionBuilder(drive.localizer.getPose())
                        .splineTo(BP1, Math.PI)
                        .splineToConstantHeading(new Vector2d(-36, 48), Math.PI)
                        .build(),


                new InstantAction(() -> action.intake(intakePower)),
                new SleepAction(1.0),
                new InstantAction(action::intake_stop),

                // ---------- 1차 발사 ----------
                moveSpinAlignShoot(
                        drive.actionBuilder(drive.localizer.getPose())
                                .splineTo(G2, Math.PI/4)
                                .build(),
                        true,
                        0.5
                )**/      /**,




                // 2차 수집
                drive.actionBuilder(drive.localizer.getPose())
                        .splineTo(BP2, Math.PI)
                        .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                        .build(),
                new InstantAction(() -> action.intake(intakePower)),
                new SleepAction(1.0),
                new InstantAction(action::intake_stop),

                // ---------- 2차 발사 ----------
                moveSpinAlignShoot(
                        drive.actionBuilder(drive.localizer.getPose())
                                .splineTo(G2, Math.PI/4)
                                .build(),
                        true,
                        0.5
                )**/    /**,




                // ---------- 3차 수집 ----------
                drive.actionBuilder(drive.localizer.getPose())
                        .splineTo(BP3, Math.PI)
                        .splineToConstantHeading(new Vector2d(-12, 48), Math.PI)
                        .build(),
                new InstantAction(() -> action.intake(intakePower)),
                new SleepAction(1.0),
                new InstantAction(action::intake_stop),

                // ---------- 3차 발사 ----------
                moveSpinAlignShoot(
                        drive.actionBuilder(drive.localizer.getPose())
                                .splineTo(G2, Math.PI/4)
                                .build(),
                        true,
                        0.5
                ),

                // ---------- 주차 ----------
                drive.actionBuilder(drive.localizer.getPose())
                        .splineTo(new Vector2d(-48,-32), Math.PI/2)
                 .build()

                 **/
        );

        Actions.runBlocking(routine);

        // 안전 정지
        action.outtake_stop();
        action.intake_stop();
        Turret_S.setPower(0);
        Turret_R.setPower(0);
        IntakeDc.setPower(0);

        while (opModeIsActive()) idle();
    }


    private Action moveSpinAlignShoot(Action moveAction, boolean useAlign, double shotPower) {

        return new SequentialAction(

                new ParallelAction(
                        moveAction,
                        new OuttakeSpinUpAction(action, shotPower)
                ),


                useAlign
                        ? new AlignAction(visionModule, Turret_R, true, ALIGN_SPEED)
                        : new SleepAction(0),

                new FeedForTimeAction(action, intakePower, FEED_SEC),

                new InstantAction(action::outtake_stop)
        );
    }




    public static class OuttakeSpinUpAction implements Action {
        private final ActionManaging action;
        private final double power;

        public OuttakeSpinUpAction(ActionManaging action, double power) {
            this.action = action;
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            action.outtake(power);
            return true;
        }
    }



    public static class AlignAction implements Action {
        private final vision visionModule;
        private final DcMotor turret;
        private final boolean isBlue;
        private final double speed;

        public AlignAction(vision visionModule, DcMotor turret, boolean isBlue, double speed) {
            this.visionModule = visionModule;
            this.turret = turret;
            this.isBlue = isBlue;
            this.speed = speed;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            boolean done = visionModule.alignAuto(turret, isBlue, speed);
            return !done;
        }
    }

    /**
     * N초 동안 intake 피딩 후 자동 종료
     */
    public static class FeedForTimeAction implements Action {
        private final ActionManaging action;
        private final double power;
        private final double seconds;
        private final ElapsedTime timer = new ElapsedTime();
        private boolean started = false;

        public FeedForTimeAction(ActionManaging action, double power, double seconds) {
            this.action = action;
            this.power = power;
            this.seconds = seconds;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            if (!started) {
                timer.reset();
                started = true;
            }

            if (timer.seconds() <= seconds) {
                action.intake(power);
                return true;
            } else {
                action.intake_stop();
                return false;
            }
        }
    }
}