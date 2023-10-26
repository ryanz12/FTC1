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

    public DcMotor armLeft;
    public DcMotor armRight;

    static final boolean FIELD_CENTRIC = false;

    @Override
    public void runOpMode() throws InterruptedException {
        armLeft = hardwareMap.get(DcMotor.class, "leftFront");
        armRight = hardwareMap.get(DcMotor.class, "rightFront");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        Motor leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_312);
//        leftBack.setInverted(true);
//        MecanumDrive drive = new MecanumDrive(
//                new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_312),
//                new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_312),
//                leftBack,
//                new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_312)
//        );

        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx driverArm = new GamepadEx(gamepad2);

        waitForStart();

        while (!isStopRequested()) {
//            drive.driveRobotCentric(
//                    -driverOp.getLeftX(),
//                    driverOp.getLeftY(),
//                    -driverOp.getRightX()
//            );

            if(gamepad1.a){
                telemetry.addData("Left Bumper Pressed", "True");

                armLeft.setTargetPosition(1400);
                armRight.setTargetPosition(1400);

                armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while(armLeft.isBusy() && armRight.isBusy()){
                    telemetry.addData("Motors are mpoving", "true");
                }

                armLeft.setPower(0);
                armRight.setPower(0);

                telemetry.update();
            }
        }
    }

}