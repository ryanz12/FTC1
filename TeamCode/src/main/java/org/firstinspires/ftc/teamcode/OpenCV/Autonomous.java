package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode {
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

/*gg*/
@Override
public void runOpMode() {

    ////Encoder code
    leftFrontMotor = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
    rightFrontMotor = hardwareMap.get(DcMotor.class, "RightFrontMotor");
    leftBackMotor = hardwareMap.get(DcMotor.class, "LeftBackMotor");
    rightBackMotor = hardwareMap.get(DcMotor.class, "RightBackMotor");

//        initialize camera and pipeline
    colorOpenCV cv = new colorOpenCV(this);
    ColorPipeline colorPipeline = new ColorPipeline();
//      call the function to startStreaming
    int MOTOR_TICKS_COUNT = (int) leftFrontMotor.getMotorType().getTicksPerRev();
    double circumference = 3.14*2.938;
    double rotationsNeeded = 18/circumference;
    int encoderDrivingTarget = (int)(rotationsNeeded*1200);
    /*leftFrontMotor.setTargetPosition(encoderDrivingTarget);*/


    leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    int position = 0;
    leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    ////Encoder code


    cv.observeStick();
    waitForStart();
    while (opModeIsActive()) {
        try {
            if (colorPipeline.canSee = true){
                telemetry.addLine("Hello");
            }
            else{
                telemetry.addLine("NOOOOO none detected");
            }
        }catch (Exception e){
            telemetry.addLine(String.valueOf(e));
        }
    }
//        stopStreaming
    cv.stopCamera();
}

    public void forward(int inchesForward) {
        double circumference = 3.14*2.938;


        double rotationsNeeded = inchesForward/circumference;
        int encoderDrivingTarget = (int)(rotationsNeeded*1200);
        leftFrontMotor.setTargetPosition(encoderDrivingTarget);
        rightFrontMotor.setTargetPosition(encoderDrivingTarget);
        leftBackMotor.setTargetPosition(encoderDrivingTarget);
        rightBackMotor.setTargetPosition(encoderDrivingTarget);

    }

}

/*hello ryan*/