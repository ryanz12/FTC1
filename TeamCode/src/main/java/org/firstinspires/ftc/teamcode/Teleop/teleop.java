package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class teleop extends LinearOpMode {
    private DcMotor armLeft = null;
    private DcMotor armRight = null;

    @Override
    public void runOpMode(){
        armLeft = hardwareMap.get(DcMotor.class, "leftFront");
        armRight = hardwareMap.get(DcMotor.class, "rightFront");

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at",  "%7d :%7d",
                armLeft.getCurrentPosition(),
                armRight.getCurrentPosition());
        telemetry.update();

        waitForStart();

        moveArm();

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void moveArm(){
        if(opModeIsActive()){
            armLeft.setTargetPosition(2000);
            armRight.setTargetPosition(2000);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(1);
            armRight.setPower(1);

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
