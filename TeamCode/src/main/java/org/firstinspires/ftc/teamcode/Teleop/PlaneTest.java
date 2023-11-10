package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class PlaneTest extends LinearOpMode {
    public Servo planeServo;
    public void runOpMode(){
        planeServo = hardwareMap.servo.get("planeServo");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.b){
                planeServo.setPosition(1);
            }
            telemetry.addData("servo pos", planeServo.getPosition()+ "\nDirection", planeServo.getDirection() + "\nDirection" + planeServo.getPortNumber());
            telemetry.update();
        }
    }
}
