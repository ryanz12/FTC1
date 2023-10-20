package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class new_teleop extends LinearOpMode {

    static final boolean FIELD_CENTRIC = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Motor leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_312);
        leftBack.setInverted(true);
        MecanumDrive drive = new MecanumDrive(
                new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_312),
                leftBack,
                new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_312)
        );

        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {

                drive.driveRobotCentric(
                        -driverOp.getLeftX(),
                        driverOp.getLeftY(),
                        driverOp.getRightX()
                );

        }
    }

}