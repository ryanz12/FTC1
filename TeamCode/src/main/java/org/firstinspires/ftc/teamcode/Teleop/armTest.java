package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class armTest extends LinearOpMode {

    public DcMotor armLeft;
    public DcMotor armRight;

    public boolean armMove=false;
    @Override
    public void runOpMode() throws InterruptedException {
        armLeft = hardwareMap.get(DcMotor.class, "armLet");
        armRight = hardwareMap.get(DcMotor.class, "armRight");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(!isStopRequested()){
            if(gamepad1.a){
                armMove = !armMove;

                if(armMove){
                    moveArm(200);
                }
                else{
                     moveArm(-200);
                }
            }
        }

    }

    public void moveArm(int ticks){
        if(opModeIsActive()){
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(0.1);
            armRight.setPower(0.1);

            while(opModeIsActive() && (armLeft.isBusy() && armRight.isBusy())){
                telemetry.addData("Running to",  " %7d :%7d", 2000,  2000);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        armLeft.getCurrentPosition(), armRight.getCurrentPosition());
                telemetry.update();
            }

            armLeft.setPower(0);
            armRight.setPower(0);

            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

