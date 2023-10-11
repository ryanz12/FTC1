package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class teleop extends LinearOpMode {
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    private Servo planeLauncher;
    public double tgtPowerlfw;
    public double tgtPowerrfw;
    public double tgtPowerlbw;
    public double tgtPowerrbw;
    @Override
    public void runOpMode() {
        leftFrontMotor = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "RightBackMotor");


        telemetry.addData("Status", "Initialized");
        telemetry.update();
// Wait for the game to start (driver presses PLAY)
        waitForStart();
// run until the end of the match (driver presses STOP)


        while (opModeIsActive()) {


            //dpad controls robot movements, joystick strafes robot


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
            else{
                resetMotors();
            }
            if(gamepad2.y){
                planeLauncher.setPosition(1);
            }else {
                planeLauncher.setPosition(0);
            }

            leftFrontMotor.setPower(tgtPowerlfw);
            rightFrontMotor.setPower(tgtPowerrfw);
            leftBackMotor.setPower(tgtPowerlbw);
            rightBackMotor.setPower(tgtPowerrbw);


            telemetry.addData("awd", gamepad2.left_stick_x);


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
    }


    public void resetMotors(){
        tgtPowerlfw = 0;
        tgtPowerrfw = 0;
        tgtPowerlbw = 0;
        tgtPowerrbw = 0;
    }
}