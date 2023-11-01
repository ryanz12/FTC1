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
    private DcMotor armLeft = null;
    private DcMotor armRight = null;
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

        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {
            drive.driveRobotCentric(
                    -driverOp.getLeftX(),
                     driverOp.getLeftY(),
                    -driverOp.getRightX()
            );

            if(gamepad1.a){
                moveArm();
            }
        }
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

