package org.firstinspires.ftc.teamcode.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class teleop extends LinearOpMode {
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;
    private DcMotor armMotorLeft;
    private DcMotor armMotorRight;


    public double tgtPowerlfw;
    public double tgtPowerrfw;
    public double tgtPowerlbw;
    public double tgtPowerrbw;
    public double armTgtPower;

    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFront");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFront");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBack");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBack");
//        armMotorLeft = hardwareMap.get(DcMotor.class, "armMotor");
//        armMotorRight = hardwareMap.get(DcMotor.class, "armMotor");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
// Wait for the game to start (driver presses PLAY)
        waitForStart();
// run until the end of the match (driver presses STOP)


        while (opModeIsActive()) {

            //dpad controls robot movements, joystick strafes robot, l1 arm down r1 arm up
            if(gamepad1.right_bumper){
                leftFrontMotor.setPower(-1);
                rightFrontMotor.setPower(1);
            }

            else{
                leftFrontMotor.setPower(0);
                rightFrontMotor.setPower(0);
            }
            telemetry.addData("Status", "Running");
            telemetry.addData("Status", "compliing?");
            telemetry.update();
        }
    }
}