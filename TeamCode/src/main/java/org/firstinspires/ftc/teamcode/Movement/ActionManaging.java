package org.firstinspires.ftc.teamcode.Movement;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class ActionManaging {
//    Servo lifting;
    public DcMotor Turret_S,Turret_R , IntakeDc;


    public ActionManaging(DcMotor Turret_S, DcMotor Turret_R, DcMotor IntakeDc) {
        this.Turret_S = Turret_S;
        this.Turret_R = Turret_R;
        this.IntakeDc = IntakeDc;
    }

    public void intake(double power_in) {
        IntakeDc.setPower(power_in);
    }

    public void intake_stop() {
        IntakeDc.setPower(0);
    }
    public void intake_r() {
        IntakeDc.setPower(-1);
    }

    public void turret_rotation() {
    }

    public void outtake(double power){
        Turret_S.setPower(power);
    }

    public void outtake_stop(){
        Turret_S.setPower(0);
        IntakeDc.setPower(0);
    }


}