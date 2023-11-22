package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class EncoderTest extends LinearOpMode {

    public double speedReducer = .6;
    @Override
    public void runOpMode(){
        Motor leftFront = new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_312);
        Motor leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_312);
        Motor rightFront = new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_312);
        Motor rightBack = new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_312);

        leftFront.resetEncoder();
        leftBack.resetEncoder();
        rightFront.resetEncoder();
        rightBack.resetEncoder();

        leftFront.setInverted(true);
        rightBack.setInverted(true);

        MecanumDrive drive = new MecanumDrive(
                leftFront,
                rightFront,
                leftBack,
                rightBack
        );

        GamepadEx gamepad = new GamepadEx(gamepad1);

        waitForStart();
        while(!isStopRequested()){
            drive.driveRobotCentric(
                    -gamepad.getLeftX()*speedReducer,
                    -gamepad.getLeftY()*speedReducer,
                    -gamepad.getRightX()*speedReducer
            );

            telemetry.addData("leftFrontMotor ", leftFront.getCurrentPosition());
            telemetry.addData("leftBackMotor ", leftBack.getCurrentPosition());
            telemetry.addData("rightFrontMotor ", rightFront.getCurrentPosition());
            telemetry.addData("rightBackMotor ", rightBack.getCurrentPosition());
            telemetry.update();
        }
    }
}
