package org.firstinspires.ftc.teamcode;

import android.content.OperationApplicationException;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp
public class FLywheelTunnertutorial extends OpMode {
    public DcMotorEx Turret_S;
    double highvelocity = 2000;
    double lowvelocity  = 1500;

    double curTargetVelocity = highvelocity;
    double F = 0; //15.5
    double P = 0;
    double nominalVoltage = 12.0;
    double[] stepSizes = {10.0, 1.0, 0.1, 0.001};

    int stepIndex = 1;

    @Override
    public void init() {
        Turret_S = hardwareMap.get(DcMotorEx.class, "Turret_S");
        Turret_S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Turret_S.setDirection(DcMotor.Direction.FORWARD);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0, F);
        Turret_S.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );
        telemetry.addLine("Init complete");

    }

    @Override
    public void loop() {
        double batteryVoltage = hardwareMap.voltageSensor.iterator().next().getVoltage();

        double compensatedF = F * (nominalVoltage / batteryVoltage);

        if(gamepad1.yWasPressed()) {
            if(curTargetVelocity == highvelocity) {
                curTargetVelocity = lowvelocity;
            } else{curTargetVelocity = highvelocity;}
        }
        if(gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad1.dpadUpWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0, compensatedF);
        Turret_S.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        Turret_S.setVelocity(curTargetVelocity);

        double curVelocity = Turret_S.getVelocity();
        double error = curTargetVelocity - curVelocity;

        telemetry.addData("Target Velocity",curTargetVelocity);
        telemetry.addData("Current Velocity","%.2f",curVelocity);
        telemetry.addData("Error","%.2f",error);
        telemetry.addLine("------------------------");
        telemetry.addData("Tuning P","%.4f (D-Pad U/D)",P);
        telemetry.addData("Tuning F","%.4f (D-Pad L/R)",F);
        telemetry.addData("Step Size","%.4f (B button)",stepSizes[stepIndex]);


        telemetry.update();
    }
}
