package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp()
public class DelayTest extends LinearOpMode {
    NormalizedColorSensor c1,c2,c3;

    public boolean findWhite(NormalizedColorSensor sensor){

        NormalizedRGBA colors = sensor.getNormalizedColors();

        float normRed, normBlue, normGreen;
        normRed = colors.red/colors.alpha;
        normBlue = colors.blue/colors.alpha;
        normGreen = colors.green/colors.alpha;

        telemetry.addData("red",normRed);
        telemetry.addData("green",normGreen);
        telemetry.addData("blue",normBlue);

        return normRed > 0.9 && normGreen > 0.9 && normBlue > 0.9;
    }

    @Override
    public void runOpMode()  {

        c1 = hardwareMap.get(NormalizedColorSensor.class,"c1");
        c2 = hardwareMap.get(NormalizedColorSensor.class,"c2");
        c3 = hardwareMap.get(NormalizedColorSensor.class,"c3");
        if(opModeIsActive()){
            telemetry.addData("c1",findWhite(c1));
            telemetry.addData("c2",findWhite(c2));
            telemetry.addData("c3",findWhite(c3));
        }
    }
}
