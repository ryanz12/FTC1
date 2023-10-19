package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class new_teleop extends LinearOpMode {

    @Override
    public void runOpMode(){

        Motor leftFront = new Motor(hardwareMap, "leftFront");
        Motor rightFront = new Motor(hardwareMap, "rightFront");
        Motor leftBack = new Motor(hardwareMap, "leftBack");
        Motor rightBack = new Motor(hardwareMap, "rightBack");

        leftBack.setInverted(true);

        MecanumDrive drive = new MecanumDrive(
                leftFront, rightFront, leftBack, rightBack
        );

        //move robot when dpad forward
        GamepadEx gamepad = new GamepadEx(gamepad1);

        waitForStart();

        while(opModeIsActive()){
            drive.driveRobotCentric(
                    gamepad.getLeftX(),
                    gamepad.getLeftY(),
                    gamepad.getRightY()
            );

            telemetry.addData("Robot", "rujnning");
            telemetry.addData("leftx", gamepad.getLeftX());
            telemetry.addData("lefty", gamepad.getLeftY());
            telemetry.addData("righy", gamepad.getRightY());

            telemetry.update();
        }
    }
}
