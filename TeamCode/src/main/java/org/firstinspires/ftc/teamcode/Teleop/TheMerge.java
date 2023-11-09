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

    /*
    Controller scheme
    First controller Gamepad 1
    A = Arm Up down
    B = Plane test
    Right and left trigger = intake

    Second controller Gamepad 2
    all driver movement that's all
     */
    @Override
    public void runOpMode() {

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");


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
                    -driverOp.getLeftX(),
                    driverOp.getLeftY(),
                    -driverOp.getRightX()
            );


            //############### INTAKE ###############
            if (gamepad1.left_trigger > 0) {
                intakePower = -0.5;
            } else if (gamepad1.right_trigger > 0) {
                intakePower = 0.8;
            } else {
                intakePower = 0;
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

        }

    }
}
