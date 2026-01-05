package org.firstinspires.ftc.teamcode.Teleop;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.teamcode.Movement.IMU_Driving;
@Config
@TeleOp(name = "TeleOp")public class Teleop_test_kla_2 extends LinearOpMode {
    DcMotor IntakeDc, fl, fr, rl, rr, OuttakeDc;
    IMU imu;
    IMU_Driving imu_driving;
    public static double outtakePower = 1.0;
    public static double intakePower = 1.0;
    private boolean Intake_Pressed = false;
    private boolean Outtake_Pressed = false;
    @Override
    public void runOpMode() {
        initialize();
        imu_driving = new IMU_Driving(fl,fr,rl, rr, imu, telemetry,gamepad1);
        imu_driving.init();
        imu_driving.getYaw();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                imu_driving.controlWithPad(IMU_Driving.GamepadPurpose.WHOLE);

                if (gamepad2.a) {
                    IntakeDc.setPower(intakePower);
                    if (!Intake_Pressed) Intake_Pressed = true;
                }
                if(!gamepad2.a && Intake_Pressed){
                    IntakeDc.setPower(0);
                    Intake_Pressed = false;
                }
                if(gamepad2.b){
                    OuttakeDc.setPower(outtakePower);
                    if (!Outtake_Pressed) Outtake_Pressed = true;
                }
                if(!gamepad2.b && Outtake_Pressed){
                    Outtake_Pressed = false;
                    OuttakeDc.setPower(0);
                }
                telemetry.update();
            }
        }
    }
    void initialize() {
        IntakeDc = hardwareMap.dcMotor.get("IntakeDc");
        OuttakeDc = hardwareMap.dcMotor.get("OuttakeDc");

        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        rl = hardwareMap.dcMotor.get("rl");
        rr = hardwareMap.dcMotor.get("rr");
        imu = hardwareMap.get(IMU.class,"imu");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        rl.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        IntakeDc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OuttakeDc.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        OuttakeDc.setDirection(DcMotorSimple.Direction.REVERSE);

        IntakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        OuttakeDc.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}



