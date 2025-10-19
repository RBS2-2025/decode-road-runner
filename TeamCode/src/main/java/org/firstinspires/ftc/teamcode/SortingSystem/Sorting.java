package org.firstinspires.ftc.teamcode.SortingSystem;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Sorting")
public class Sorting extends LinearOpMode {
    private NormalizedColorSensor c1, c2, c3;
    private CRServo rotateWheel;
    private Servo casement1, casement2;
    public static final double GREEN_R = 0.2; // 초록색으로 판단하기 위한 R G B
    public static final double GREEN_G = 0.55;
    public static final double GREEN_B = 0.25;

    public static final double PURPLE_R = 0.4; // 보라색으로 판단하기 위한 최소 R G B
    public static final double PURPLE_G = 0.25;
    public static final double PURPLE_B = 0.45;
    public static final double ColorThreshold = 0.2; // N% 오차, 테스트 필요함

    public static final boolean RotateWheel_Reverse = false; //기본값 RotateWheel 정방향
    public static final boolean Servos_Reverse = false; // 기본값 casements 정방향

    public static final double RotateWheel_Duration = 0.5; // 검증되지 않음, 테스트 필요함ㅁ

    public static final double Servos_Rotation_Angle = 1.0; // 테스트 필요

    public static final double CoolDowns = 0.2; // 각 동작 시행 간격; 필요 없으면 0.0

    public ElapsedTime Rotating_time = new ElapsedTime(); //ElapsedTime 써가지고 비동기로 처리, 이것도 테스트 필요

    public int findColor(NormalizedColorSensor sensor){
        NormalizedRGBA colors = sensor.getNormalizedColors();

        telemetry.addData("alpha", colors.alpha);
        telemetry.addData("red", colors.red);
        telemetry.addData("green", colors.green);
        telemetry.addData("blue", colors.blue);
        // Return 값
        // 0:초록/1:보라/2:X
        if (IsGreen(colors)){
            return 0;
        } else if (IsPurple(colors)){
            return 1;
        } else {
            return 2;
        }



    }
    //Threshold로 오차 범위 1 +- .n% 적용해 Green 색상 판별
    private boolean IsGreen(NormalizedRGBA colors){
        if ((GREEN_R*(1+ColorThreshold) >= colors.red)&&(colors.red >= GREEN_R*(1-ColorThreshold))){
            if ((GREEN_G*(1+ColorThreshold) >= colors.green)&&(colors.green >= GREEN_G*(1-ColorThreshold))){
                if ((GREEN_B*(1+ColorThreshold) >= colors.blue)&&(colors.blue >= GREEN_B*(1-ColorThreshold))){
                    return true;
                }
            }
        }
        return false;
    }
    //Threshold로 오차 범위 1 +- .n% 적용해 Purple 색상 판별
    private boolean IsPurple(NormalizedRGBA colors){
        if ((PURPLE_R*(1+ColorThreshold) >= colors.red)&&(colors.red >= PURPLE_R*(1-ColorThreshold))){
            if ((PURPLE_G*(1+ColorThreshold) >= colors.green)&&(colors.green >= PURPLE_G*(1-ColorThreshold))){
                if ((PURPLE_B*(1+ColorThreshold) >= colors.blue)&&(colors.blue >= PURPLE_B*(1-ColorThreshold))){
                    return true;
                }
            }
        }
        return false;
    }

    //각 모터들 Activate에 칸 회전 제어용 reverse (+-)
    private void Casement_Activate(Servo casement, boolean reverse) {
        if (reverse) {
            casement.setPosition(Servos_Rotation_Angle);
        } else {
            casement.setPosition(1-Servos_Rotation_Angle);
        }

    }

    private void RotateWheel_Activate(boolean reverse) {
        Rotating_time.reset();
        if (reverse) {
            rotateWheel.setPower(-1);
        } else {
            rotateWheel.setPower(1);
        }
        while (Rotating_time.seconds() < RotateWheel_Duration) {
            rotateWheel.setPower(0);
        }
    }

    private boolean TimeCheck(double Duration) {
        ElapsedTime time = new ElapsedTime();
        while (time.seconds() < Duration) {
            return false;
        }
        return true;

    }
    private void Sort(int Color_Value) { //Color_Value는 0,1(초록,보라) 사용
        //Color detecting 파트
        int Detected1 = findColor(c1);
        int Detected2 = findColor(c2);
        int Detected3 = findColor(c3);

        telemetry.addData("c1:", Detected1);
        telemetry.addData("c2:", Detected2);
        telemetry.addData("c3:", Detected3);

        //Color detecting 한거 바탕으로 동작 파트
        if (Detected3 == Color_Value) {
            Casement_Activate(casement1, false);
            Casement_Activate(casement2, false);
            if (TimeCheck(CoolDowns)){
                return;
            }
        } else if (Detected2 == Color_Value) {
            RotateWheel_Activate(false);
            if (TimeCheck(CoolDowns)){
                Casement_Activate(casement1, false);
                Casement_Activate(casement2, false);
            }

        } else if (Detected1 == Color_Value) {
            RotateWheel_Activate(true);
            if (TimeCheck(CoolDowns)){
                Casement_Activate(casement1, false);
                Casement_Activate(casement2, false);
            }
        }
        else{
            Casement_Activate(casement1, false);
            Casement_Activate(casement2, false);
        }


    }


    @Override
    public void runOpMode(){
        rotateWheel = hardwareMap.get(CRServo.class, "RotateWheel");
        casement1 = hardwareMap.get(Servo.class, "Casement1");
        casement2 = hardwareMap.get(Servo.class, "Casement2");
        c1 = hardwareMap.get(NormalizedColorSensor.class, "c1");
        c2 = hardwareMap.get(NormalizedColorSensor.class, "c2");
        c3 = hardwareMap.get(NormalizedColorSensor.class, "c3");

        rotateWheel.setDirection(CRServo.Direction.FORWARD);
        if (RotateWheel_Reverse) { //테스트 후 방향 고려해서 RotateWheel_Reverse 수정
            rotateWheel.setDirection(CRServo.Direction.REVERSE);
        }

        casement1.setDirection(Servo.Direction.FORWARD);
        casement2.setDirection(Servo.Direction.REVERSE);

        if (Servos_Reverse) { //테스트 후 방향 고려해서 Servos_Reverse 수정
            casement1.setDirection(Servo.Direction.REVERSE);
            casement2.setDirection(Servo.Direction.FORWARD);
        }

        telemetry.addLine("Ready to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            Sort(0); //Sorting Green
            //Sort(1); //Sorting Purple

            telemetry.addData("casement1 Pos:", casement1.getPosition());
            telemetry.addData("casement2 Pos:", casement2.getPosition());
            telemetry.addData("RotateWheel Power:", rotateWheel.getPower());

            telemetry.update();
        }
    }
}
