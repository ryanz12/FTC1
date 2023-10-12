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
        leftFrontMotor = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "RightBackMotor");
        armMotorLeft = hardwareMap.get(DcMotor.class, "armMotorLeft");
        armMotorRight = hardwareMap.get(DcMotor.class, "armMotorRight");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
// Wait for the game to start (driver presses PLAY)
        waitForStart();
// run until the end of the match (driver presses STOP)


        while (opModeIsActive()) {

            //dpad controls robot movements, joystick strafes robot, l1 arm down r1 arm up

            if(gamepad2.dpad_up) {
                moveMotors("forward");
            }
            else if(gamepad2.dpad_down){
                moveMotors("backward");
            }
            else if(gamepad2.dpad_right){
                moveMotors("strafeRight");
            }
            else if(gamepad2.left_stick_x < 0){
                moveMotors("turnLeft");
            }
            else if(gamepad2.left_stick_x > 0){
                moveMotors("turnRight");
            }
            else if(gamepad2.left_bumper){
                moveMotors("armDown");
            }
            else if (gamepad2.right_bumper){
                moveMotors("armUp");
            }
            else{
                resetMotors();
            }


            leftFrontMotor.setPower(tgtPowerlfw);
            rightFrontMotor.setPower(tgtPowerrfw);
            leftBackMotor.setPower(tgtPowerlbw);
            rightBackMotor.setPower(tgtPowerrbw);
            armMotorLeft.setPower(armTgtPower);
            armMotorRight.setPower(armTgtPower);

            telemetry.addData("Status", "Running");
            telemetry.addData("Status", "compliing?");
            telemetry.update();
        }
    }


    public void moveMotors(String str){
        if(str == "forward"){
            tgtPowerlfw = 1;
            tgtPowerrfw = 1;
            tgtPowerlbw = 1;
            tgtPowerrbw = 1;
        }
        else if(str == "backward"){
            tgtPowerlfw = -1;
            tgtPowerrfw = -1;
            tgtPowerlbw = -1;
            tgtPowerrbw = -1;
        }
        else if(str == "strafeRight"){
            tgtPowerlfw = 1;
            tgtPowerrfw = -1;
            tgtPowerlbw = 1;
            tgtPowerrbw = -1;
        }
        else if(str == "strafeLeft"){
            tgtPowerlfw = -1;
            tgtPowerrfw = 1;
            tgtPowerlbw = -1;
            tgtPowerrbw = 1;
        }
        else if(str == "turnLeft"){
            tgtPowerlfw = 1;
            tgtPowerlbw = 1;
            tgtPowerrfw = -1;
            tgtPowerrbw = -1;
        }
        else if(str == "turnRight"){
            tgtPowerlfw = -1;
            tgtPowerlbw = -1;
            tgtPowerrfw = 1;
            tgtPowerrbw = 1;
        }
        else if(str == "armDown"){
            armTgtPower = -1;
        }
        else if(str == "armUp"){
            armTgtPower = 1;
        }
    }


    public void resetMotors(){
        tgtPowerlfw = 0;
        tgtPowerrfw = 0;
        tgtPowerlbw = 0;
        tgtPowerrbw = 0;
    }
}