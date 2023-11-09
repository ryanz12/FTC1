package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class climbTest extends LinearOpMode {
    public DcMotor armLeft;
    public DcMotor armRight;
    public double tgtPower;

    @Override
    public void runOpMode(){
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");

        waitForStart();

        while(!isStopRequested()){
            if(gamepad1.a){
                tgtPower = -1;
            }

            armLeft.setPower(tgtPower);
            armRight.setPower(tgtPower);

        }
    }
}
