package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class armTest extends LinearOpMode {

    public DcMotor armLeft;
    public DcMotor armRight;
    @Override
    public void runOpMode() throws InterruptedException {
        armLeft = hardwareMap.get(DcMotor.class, "leftFront");
        armRight = hardwareMap.get(DcMotor.class, "rightFront");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        while (!isStopRequested()) {
//            drive.driveRobotCentric(
//                    -driverOp.getLeftX(),
//                    driverOp.getLeftY(),
//                    -driverOp.getRightX()
//            );

            if (gamepad1.a) {
                telemetry.addData("Left Bumper Pressed", "True");

                armLeft.setTargetPosition(1400);
                armRight.setTargetPosition(1400);

                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (armLeft.isBusy() && armRight.isBusy()) {
                    telemetry.addData("Motors are mpoving", "true");
                }

                armLeft.setPower(0);
                armRight.setPower(0);

                telemetry.update();
            }
        }
    }
}
