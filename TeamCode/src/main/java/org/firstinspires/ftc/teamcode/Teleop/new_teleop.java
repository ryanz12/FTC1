package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class new_teleop extends LinearOpMode {

    public double speedReducer = .6;

    @Override
    public void runOpMode() throws InterruptedException {
        Motor leftFront = new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_312);
        Motor leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_312);
        Motor rightFront = new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_312);
        Motor rightBack = new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_312);

        leftFront.resetEncoder();
        leftBack.resetEncoder();
        rightFront.resetEncoder();
        rightBack.resetEncoder();

        MecanumDrive drive = new MecanumDrive(
                leftFront,
                rightFront,
                leftBack,
                rightBack
        );

        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {
            drive.driveRobotCentric(
                    -driverOp.getLeftX()*speedReducer,
                    -driverOp.getLeftY()*speedReducer,
                    -driverOp.getRightX()*speedReducer
            );


        }
    }

}


