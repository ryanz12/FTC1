package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class new_teleop extends LinearOpMode {

    boolean isMoving=false;

    MecanumDrive drive = new MecanumDrive(
            new Motor(hardwareMap, "LeftFrontMotor", Motor.GoBILDA.RPM_312),
            new Motor(hardwareMap, "RightFrontMotor", Motor.GoBILDA.RPM_312),
            new Motor(hardwareMap, "LeftBackMotor", Motor.GoBILDA.RPM_312),
            new Motor(hardwareMap, "RightBackMotor", Motor.GoBILDA.RPM_312)
    );

    //move robot when dpad forward
    GamepadEx gamepad = new GamepadEx(gamepad1);

    @Override
    public void runOpMode(){

        while(opModeIsActive()){
            if(gamepad.getButton(GamepadKeys.Button.DPAD_UP)){
                drive.driveRobotCentric(0, 1, 0);
                isMoving = true;
            }
            else if(gamepad.getButton(GamepadKeys.Button.DPAD_DOWN)){
                drive.driveRobotCentric(0, -1, 0);
                isMoving = true;
            }
            else if(gamepad.getButton(GamepadKeys.Button.DPAD_LEFT)){
                drive.driveRobotCentric(1, 0, 0);
                isMoving = true;
            }
            else if(gamepad.getButton(GamepadKeys.Button.DPAD_RIGHT)){
                drive.driveRobotCentric(-1, 0, 0);
                isMoving = true;
            }
            else{
                drive.driveRobotCentric(0, 0, 0);
                isMoving = false;
            }

            telemetry.addData("Robot", isMoving);
            telemetry.update();
        }
    }
}
