package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class new_teleop extends LinearOpMode {

    public DcMotor intakeMotor;
    public double speedReducer = .6;
    public boolean dowemoveintake = false;

    @Override
    public void runOpMode() throws InterruptedException {
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        GamepadEx driverOp = new GamepadEx(gamepad2);

        Thread intakeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isStopRequested()){
                    if(gamepad1.left_bumper){
                        dowemoveintake = !dowemoveintake;
                        telemetry.addData("Trying to rotate the ntake", 's');
                        telemetry.update();
                        if(dowemoveintake){
                            moveIntake(400, 0.05);
                        }else{
                            moveIntake(0, 0.05 );
                        }
                    }
                }
            }
        });

        intakeThread.start();

        waitForStart();

        while (!isStopRequested()) {
            drive.driveRobotCentric(
                    -driverOp.getLeftX()*speedReducer,
                    -driverOp.getLeftY(),
                    -driverOp.getRightX()
            );
        }
        intakeThread.interrupt();
    }

    public void moveIntake(int ticks, double speed){
        if(opModeIsActive()){
            intakeMotor.setTargetPosition(ticks);

            intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            intakeMotor.setPower(speed);

            while(opModeIsActive() && (intakeMotor.isBusy())){
                telemetry.addData("Running to", " %7d", ticks);
                telemetry.addData("Currently at", " %7d", intakeMotor.getCurrentPosition());
                telemetry.update();
            }

            intakeMotor.setPower(0);
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}


