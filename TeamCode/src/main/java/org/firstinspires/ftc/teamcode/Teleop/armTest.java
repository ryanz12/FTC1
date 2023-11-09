package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class armTest extends LinearOpMode {

    public DcMotor armLeft;
    public DcMotor armRight;

    public boolean armMove=false;

    public boolean referenceIsNotReached = false;
    @Override
    public void runOpMode() throws InterruptedException {
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
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
                    moveArm(1200);

                    }


                }
                else{
                    moveArm(-1200);

                }

            }

            if(armMove){
                int current_Position = armLeft.getCurrentPosition();
                int current_Position_Right = armRight.getCurrentPosition();
                int error = 1200 - current_Position;
                int error_Right = 1200-current_Position_Right;
                // set motor power proportional to the error
                if (error<0 && error>= -200 || error<=200 && error>0){
                    error = 0;
                    error_Right = 0;
                }
                armLeft.setPower(error);
                armRight.setPower(error_Right);

            }
//hh

        }



    public void moveArm(int ticks){
        if(opModeIsActive()){
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(.2);
            armRight.setPower(.2);

            while(opModeIsActive() && (armLeft.isBusy() && armRight.isBusy())){
                telemetry.addData("Running to",  " %7d :%7d", 1200,  2000);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        armLeft.getCurrentPosition(), armRight.getCurrentPosition());
                telemetry.update();
            }
            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

