package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class teleop extends LinearOpMode {
    public DcMotor armLeft;
    public DcMotor armRight;

    @Override
    public void runOpMode(){
        armLeft = hardwareMap.get(DcMotor.class, "leftFront");
        armRight = hardwareMap.get(DcMotor.class, "rightFront");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.left_bumper){
                telemetry.addData("Left Bumper Pressed", "True");
                telemetry.update();

                armLeft.setTargetPosition(1400);
                armRight.setTargetPosition(1400);

                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                armLeft.setPower(1);
                armRight.setPower(1);

                while(armLeft.isBusy() && armRight.isBusy()){
                    telemetry.addData("Motors are mpoving", "true");
                    telemetry.update();
                }

                armLeft.setPower(0);
                armRight.setPower(0);
            }
        }
    }
}
