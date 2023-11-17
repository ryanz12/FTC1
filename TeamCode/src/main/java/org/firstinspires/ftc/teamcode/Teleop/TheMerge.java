package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
public class TheMerge extends LinearOpMode {
    public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor intakeMotor;
    public double intakePower = 0;
    public boolean armMove = false;
    public double speedReducer=0.75;

    @Override
    public void runOpMode() {
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());
        armRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());

        // FTC LIB INITIALIZATION
        Motor leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_312);
        leftBack.setInverted(true);
        Motor rightBack = new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_312);
        rightBack.setInverted(true);
        MecanumDrive drive = new MecanumDrive(
                new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_312),
                leftBack,
                rightBack
        );

        GamepadEx driverOp = new GamepadEx(gamepad2);

        //MAIN LOOP
        waitForStart();
        while (!isStopRequested()) {
            //############### ROBOT DRIVING ###############
            drive.driveRobotCentric(
                    -driverOp.getLeftX() * speedReducer,
                    driverOp.getLeftY() * speedReducer,
                    -driverOp.getRightX() * speedReducer
            );


            //############### INTAKE ###############
            if (gamepad1.left_trigger > 0) {
                intakePower = -0.5;
            } else if (gamepad1.right_trigger > 0) {
                intakePower = 1;
            } else {
                intakePower = 0;
            }
            intakeMotor.setPower(intakePower);

            //############## ARM ##################
            if(gamepad1.right_bumper){
                armMove = !armMove;
                if(armMove){
                    moveArm(800, 0.3);
                }
                else{
                    moveArm(0,.1);
                }
            }

        }
        telemetry.addLine("Controller 1 " +
                "\n ARM = Right bumper" +
                "\n Left trigger and right trigger is for input adn output "+
                "Controller 2" +
                "\n DRIVE BASE is all joysticks");
        telemetry.update();
    }

    public void moveArm(int ticks, double speed){
        if(opModeIsActive()){
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(speed);
            armRight.setPower(speed);

            while(opModeIsActive() && (armLeft.isBusy() && armRight.isBusy())){
                telemetry.addData("Running to",  " %7d :%7d", ticks,  ticks);
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
