package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class IntakeTest extends LinearOpMode {
    public DcMotor intakeMotor;

    public double motorPower=0;
    public void runOpMode(){
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        waitForStart();

        while(!isStopRequested()){
            if(gamepad1.left_trigger > 0){
                motorPower=-0.35;
            }
            else if(gamepad1.right_trigger > 0){
                motorPower=0.35;
            }

            intakeMotor.setPower(motorPower);
        }

    }
}
