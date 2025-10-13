package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name="DelayTest")
public class DelayTest extends LinearOpMode {

    NormalizedColorSensor c1, c2, c3;
    private DcMotor Dc;


    public boolean findWhite(NormalizedColorSensor sensor){
        NormalizedRGBA colors = sensor.getNormalizedColors();

        float normRed = colors.red / colors.alpha;
        float normGreen = colors.green / colors.alpha;
        float normBlue = colors.blue / colors.alpha;

        telemetry.addData("R", normRed);
        telemetry.addData("G", normGreen);
        telemetry.addData("B", normBlue);

        return normRed > 0.9 && normGreen > 0.9 && normBlue > 0.9;
    }

    @Override
    public void runOpMode() {
        Dc = hardwareMap.get(DcMotor.class, "Dc");
        c1 = hardwareMap.get(NormalizedColorSensor.class, "c1");
        c2 = hardwareMap.get(NormalizedColorSensor.class, "c2");
        c3 = hardwareMap.get(NormalizedColorSensor.class, "c3");

        telemetry.addLine("Ready to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean w1 = findWhite(c1);
            boolean w2 = findWhite(c2);
            boolean w3 = findWhite(c3);

            telemetry.addData("c1 white?", w1);
            telemetry.addData("c2 white?", w2);
            telemetry.addData("c3 white?", w3);


            if (w1 && w2 && w3) {
                Dc.setPower(1.0);

            } else if(w1 || w2 || w3) {

                Dc.setPower(-1);

            } else {
                Dc.setPower(0);
            }

            telemetry.update();
        }
    }
}
