package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class newIntakeTest extends LinearOpMode {
    private Servo intakeServo;
    @Override
    public void runOpMode(){
        if(gamepad1.a){
            intakeServo.setPosition(0);
        }
        if(gamepad1.b){
            intakeServo.setPosition(1);
        }
        if(gamepad1.x){
            intakeServo.setPosition(-1);
        }
    }
}
