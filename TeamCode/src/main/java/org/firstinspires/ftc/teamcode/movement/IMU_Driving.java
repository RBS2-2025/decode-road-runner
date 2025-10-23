package org.firstinspires.ftc.teamcode.movement;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IMU_Driving {

    public IMU_Driving(DcMotor fl, DcMotor fr, DcMotor rl, DcMotor rr, IMU imu, Telemetry telemetry, Gamepad gamepad){
        this.fl = fl;
        this.fr = fr;
        this.rl = rl;
        this.rr = rr;
        this.imu = imu;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
    }
    DcMotor fl,fr,rl,rr;
    IMU imu;
    Telemetry telemetry;
    Gamepad gamepad;

    public double speed = 1;
    double yaw;

    public void init(){
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
        telemetry.addData("IMU: ", "INITIALIZED");
        telemetry.update();
    }


    public double getYaw(){
        yaw = imu.getRobotYawPitchRollAngles().getYaw();

        telemetry.addData("yaw: ",yaw);
        telemetry.update();
        return yaw;
    }


    public double rotaeSlowThreshold = 50;
    public double getRotatePower(){
        double x = gamepad.right_stick_x;
        double y = -gamepad.right_stick_y;

        double targetYaw = Math.toDegrees(Math.atan2(y,x));
        getYaw();

        double yawDistance = targetYaw - getYaw();

        if(Math.abs(yawDistance) > 180){
            yawDistance -= Math.signum(yawDistance) * 360;
        }


        double rx;


        if(yawDistance > rotaeSlowThreshold){
            rx = Math.signum(yawDistance);
        }
        else{
            rx = yawDistance/rotaeSlowThreshold;
        }

        return rx;
    }

    public Vector2d getMovePower(){
        double x,y;
        x = gamepad.left_stick_x;
        y = -gamepad.left_stick_y;

        return new Vector2d(x,y);
    }

    public void move(){
        double rx = getRotatePower();
        Vector2d moveVec = getMovePower();
        double deno = JavaUtil.maxOfList(
                JavaUtil.createListWith(
                        Math.abs(moveVec.x),
                        Math.abs(moveVec.y),
                        Math.abs(rx),
                        1));

        fl.setPower((moveVec.x + moveVec.y -rx) / deno * speed);
        fr.setPower((-moveVec.x + moveVec.y +rx) / deno * speed);
        rl.setPower((-moveVec.x + moveVec.y -rx) / deno * speed);
        rr.setPower((moveVec.x + moveVec.y +rx) / deno * speed);

    }


}
