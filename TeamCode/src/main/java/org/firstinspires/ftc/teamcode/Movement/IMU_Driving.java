package org.firstinspires.ftc.teamcode.Movement;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IMU_Driving {

    public IMU_Driving(DcMotor fl, DcMotor fr, DcMotor rl, DcMotor rr, IMU imu, Telemetry telemetry, Gamepad gamepad1){
        this.fl = fl;
        this.fr = fr;
        this.rl = rl;
        this.rr = rr;
        this.imu = imu;
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
    }
    DcMotor fl,fr,rl,rr;
    IMU imu;
    Telemetry telemetry;
    Gamepad gamepad1;

    public double speed = 1;
    double yaw;

    public void init(){
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
        imu.resetYaw();
        telemetry.addData("IMU: ", "INITIALIZED");
        telemetry.update();
    }


    public double getYaw(){
        yaw = imu.getRobotYawPitchRollAngles().getYaw();

        telemetry.addData("yaw: ",yaw);
        return yaw;
    }

    public void resetYaw(StartPos pos){
        if(pos == StartPos.BLUE_L || pos == StartPos.RED_R){ //반시게방향 90도 회전
            rotate(90);
        }
        else if(pos == StartPos.BLUE_R || pos == StartPos.RED_L){// 시계방향 90도 회전
            rotate(-90);
        }
        else{
            return;
        }

        imu.resetYaw();
    }

    public double rotaeSlowThreshold = 50;
    public double gamepadRotate(){
        double x = gamepad1.right_stick_x;
        double y =  -gamepad1.right_stick_y;

        double targetYaw = -Math.toDegrees(Math.atan2(x,y));
        getYaw();

        if(Math.abs(x) <= 0.1 && Math.abs(y)  <= 0.1){
            return 0;
        }


        return getRotatePower(targetYaw);
    }
    public double getRotatePower(double targetYaw){
        double yawDistance = targetYaw - yaw;
        if(Math.abs(yawDistance) > 180){
            yawDistance -= Math.signum(yawDistance) * 360;
        }

        double rx;
        if(Math.abs(yawDistance) > rotaeSlowThreshold){
            rx = Math.signum(yawDistance);
        }
        else{
            rx = yawDistance/rotaeSlowThreshold;

        }

        telemetry.addData("rx: ",rx);
        telemetry.addData("yawD: ", yawDistance);
        telemetry.addData("target: ", targetYaw);
        return -rx;
    }

    public Vector2d getMovePower(){
        double rad = -Math.toRadians(getYaw());
        double x,y,a,b;
        x = gamepad1.left_stick_x;
        y = -gamepad1.left_stick_y;

        a = x * Math.cos(rad) + y * Math.sin(rad);
        b = x * -Math.sin(rad) + y * Math.cos(rad);

        if(Math.abs(a) < 0.00000025) a = 0;
        if(Math.abs(b) < 0.00000025) b = 0;

        return new Vector2d(a,b);
    }

    public void move(){
        if(gamepad1.left_stick_x <= 0.1 && gamepad1.left_stick_y <= 0.1 && gamepad1.right_stick_x <= 0.1) return;
        double rx = gamepadRotate();
        Vector2d moveVec = getMovePower();
        double deno = JavaUtil.maxOfList(
                JavaUtil.createListWith(
                        Math.abs(moveVec.x),
                        Math.abs(moveVec.y),
                        Math.abs(rx),
                        1));

        fl.setPower((-moveVec.x + moveVec.y +rx) / deno * speed);
        fr.setPower((moveVec.x + moveVec.y -rx) / deno * speed);
        rl.setPower((moveVec.x + moveVec.y +rx) / deno * speed);
        rr.setPower((-moveVec.x + moveVec.y -rx) / deno * speed);

    }

    public void rotate(double targetYaw){
        double rx = getRotatePower(targetYaw);
        while(Math.abs(rx) > 0){
            fl.setPower(rx * speed);
            fr.setPower(-rx  * speed);
            rl.setPower(rx  * speed);
            rr.setPower(-rx  * speed);
        }

    }

    public void rota(){
        double rx = gamepadRotate();
        while(Math.abs(rx) > 0){
            fl.setPower(rx * speed);
            fr.setPower(-rx  * speed);
            rl.setPower(rx  * speed);
            rr.setPower(-rx  * speed);
        }

    }


    public enum StartPos{

        BLUE_L, //작은 삼각형(런치 존)
        BLUE_R, //큰 삼각형(런치 존)
        RED_L, //큰 삼각형(런치 존)
        RED_R; //작은 삼각형(런치 존)
    }

}