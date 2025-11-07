package org.firstinspires.ftc.teamcode.Movement;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

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
            rotate2Deg(90);
        }
        else if(pos == StartPos.BLUE_R || pos == StartPos.RED_L){// 시계방향 90도 회전
            rotate2Deg(-90);
        }
        else{
            return;
        }

        imu.resetYaw();
    }

    public double rotateSlowThreshold = 50;

    /**
     * GET YAW!!
     * @param targetYaw 타켓 각도
     * @return rx
     */
    public double getRotatePower(double targetYaw){
        //yaw 거리 전처리
        double yawDist = targetYaw - getYaw(); //GetYaw !!
        if(Math.abs(yawDist) > 180){
            yawDist -= Math.signum(yawDist) * 360;
        }
        double rx;
        if(Math.abs(yawDist) > rotateSlowThreshold){//거리가 임계값 이상 이면
            rx = Math.signum(yawDist); //rx = 최대(1)
        }
        else{
            rx = yawDist/ rotateSlowThreshold; // 거리가 임계값 이하면 거리에 반비례해 1~0
        }

        return rx;
    }

    /**
     *
     * @param targetYaw 타겟 각도
     * @see IMU_Driving#getRotatePower(double) 
     */
    public void rotate2Deg(double targetYaw){
        double rx;
        do {
            rx = getRotatePower(targetYaw);
            fl.setPower(rx * speed);
            fr.setPower(-rx  * speed);
            rl.setPower(rx  * speed);
            rr.setPower(-rx  * speed);
        }while(Math.abs(rx) > 0.01);
    }

    public void getGamepad(GamepadPurpose p){
        if(p == GamepadPurpose.MOVE){

        }
        else if(p == GamepadPurpose.ROTATE){
            double x = gamepad1.right_stick_x;
            double y = -gamepad1.right_stick_y;
            double targetYaw = Math.atan2(x,y); // 90도 회전 (위 -> 0)

        }

    }

    /**
     * GetYaw!!
     * @return (dx,dy) - 이동 방향
     */
    Vector2d getMovePower(){
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double radian = Math.toRadians(-getYaw()); // 라디안 계산 때는 정방향 필요

        double a = x * Math.cos(radian) + y * Math.sin(radian);
        double b = x * -Math.sin(radian) + y * Math.cos(radian);

        if(Math.abs(a) < 0.00000025) a = 0;
        if(Math.abs(b) < 0.00000025) b = 0;
        return new Vector2d(a,b);
    }






    enum StartPos{
        BLUE_L, //작은 삼각형(런치 존)
        BLUE_R, //큰 삼각형(런치 존)
        RED_L, //큰 삼각형(런치 존)
        RED_R; //작은 삼각형(런치 존)
    }
    enum GamepadPurpose{
        MOVE,
        ROTATE;
    }

}