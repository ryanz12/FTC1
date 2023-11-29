package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class newIntakeTest extends LinearOpMode {
    public Servo intakeServo;
    public void runOpMode(){
        intakeServo = hardwareMap.servo.get("intakeServo");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.y){
                intakeServo.setPosition(1);
            }
            if (gamepad1.x){
                intakeServo.setPosition(0);
            }
            telemetry.addData("servo pos", intakeServo.getPosition()+ "\nDirection", intakeServo.getDirection() + "\nDirection" + intakeServo.getPortNumber());
            telemetry.update();
        }
    }
}

