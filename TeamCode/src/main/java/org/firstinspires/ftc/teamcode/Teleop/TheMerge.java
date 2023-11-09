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
    public double intakePower=0;
    public boolean armMove=false;

    @Override
    public void runOpMode(){
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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
        while(!isStopRequested()){
            //############### ROBOT DRIVING ###############
            drive.driveRobotCentric(
                    -driverOp.getLeftX(),
                    driverOp.getLeftY(),
                    -driverOp.getRightX()
            );


            //############### INTAKE ###############
            if(gamepad1.left_trigger > 0){
                intakePower=-0.5;
            } else if(gamepad1.right_trigger > 0){
                intakePower=0.8;
            }else{
                intakePower=0;
            }
            intakeMotor.setPower(intakePower);


            //Solution 1 of ############### ARM ###############
            /*
            if(gamepad1.right_bumper){
                armMove = !armMove;

                if(armMove){
                    moveArm(600);
                }
                else{
                    moveArm(-600);
                }
            }
*/


            //Solution 2
            Gamepad currentGamepad = new Gamepad();
            Gamepad previousGamepad1 = new Gamepad();

            previousGamepad1.copy(currentGamepad);
            currentGamepad.copy(gamepad1);
            boolean intakeToggle = false;

            if (currentGamepad.a && !previousGamepad1.a) {
                intakeToggle = !intakeToggle;
            }
            if (intakeToggle) {
                moveArm(600);
            }
            else {
                moveArm(-600);
            }
            //This will turn on the arm when a is pressed, and leave it on until it is pressed again. YAY!!
        }
    }

    //arm method
    public void moveArm(int ticks){
        if(opModeIsActive()){
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(0.2);
            armRight.setPower(0.2);

            while(opModeIsActive() && (armLeft.isBusy() && armRight.isBusy())){
                telemetry.addData("Running to",  " %7d :%7d", ticks,  ticks);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        armLeft.getCurrentPosition(), armRight.getCurrentPosition());
                telemetry.update();
            }
            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
