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
    public double speedReducer=.6;
    public static final double joystickDeadzone = 0.1;

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

        Thread armThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isStopRequested()){
                    if(gamepad1.right_bumper){
                        armMove = !armMove;
                        if(armMove){
                            moveArm(800, 0.3);
                        }
                        else{
                            moveArm(0,.15);
                        }
                    }
                }
            }
        });

        armThread.start();

        //MAIN LOOP
        waitForStart();
        while (!isStopRequested()) {
            //############### ROBOT DRIVING ###############

            drive.driveRobotCentric(
                    deadzoneCalc(-driverOp.getLeftX() * speedReducer),
                    deadzoneCalc(-driverOp.getLeftY()),
                    -driverOp.getRightX()
            );

            //############### INTAKE ###############
            if (gamepad1.left_trigger > 0) {
                intakePower = -1;
            } else if (gamepad1.right_trigger > 0) {
                intakePower = 1;
            } else {
                intakePower = 0;
            }
            intakeMotor.setPower(intakePower);

        }
        armThread.interrupt();

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
    public double deadzoneCalc(double input){
        if(Math.abs(input) < joystickDeadzone){
            return 0.0;
        }else{
            return input;
        }
    }
}
