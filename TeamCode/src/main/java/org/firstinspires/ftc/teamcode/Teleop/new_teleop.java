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
    public void runOpMode() throws InterruptedException{
        MecanumDrive drive = new MecanumDrive(
                new Motor(hardwareMap, "LeftFrontMotor", Motor.GoBILDA.RPM_435),
                new Motor(hardwareMap, "RightFrontMotor", Motor.GoBILDA.RPM_435),
                new Motor(hardwareMap, "LeftBackMotor", Motor.GoBILDA.RPM_435),
                new Motor(hardwareMap, "RightBackMotor", Motor.GoBILDA.RPM_435)
        );

        GamepadEx gamepad = new GamepadEx(gamepad1);

        if(gamepad.getButton(GamepadKeys.Button.DPAD_UP)){
            drive.driveRobotCentric(0, 1, 0);
        }else{
            drive.driveRobotCentric(0, 0, 0);
        }
    }
}
