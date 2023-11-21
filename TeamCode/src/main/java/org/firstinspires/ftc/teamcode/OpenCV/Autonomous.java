package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.trajectory.TrajectoryConfig;
import com.arcrobotics.ftclib.vision.UGBasicHighGoalPipeline;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;

@Config
@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode {
    public static DcMotor leftFrontMotor;
    public static DcMotor rightFrontMotor;
    public static DcMotor leftBackMotor;
    public static DcMotor rightBackMotor;

    FtcDashboard dashboard;

    public static double TARGET_POS = 100;
/*gg*/
@Override
public void runOpMode() {

    //dashboard code

    dashboard = FtcDashboard.getInstance();
    ////Encoder code
    leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFront");
    rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFront");
     leftBackMotor = hardwareMap.get(DcMotor.class, "leftBack");
    rightBackMotor = hardwareMap.get(DcMotor.class, "rightBack");

//        initialize camera and pipeline

//      call the function to startStreaming
    int MOTOR_TICKS_COUNT = (int) leftFrontMotor.getMotorType().getTicksPerRev();


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

    int number = 1;
    waitForStart();
    leftFrontMotor.setPower(0.1);
    while (opModeIsActive()) {
      telemetry.addData("Ticcks", leftFrontMotor.getMotorType().getTicksPerRev());
      telemetry.update();
    }
//        stopStreaming
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

    private void turnClockwise(int whatAngle, double speed) {
        // whatAngle is in degrees. A negative whatAngle turns counterclockwise.
        int MOTOR_TICKS_COUNT = (int) leftFrontMotor.getMotorType().getTicksPerRev();
        // fetch motor positions
        int lfPos = leftFrontMotor.getCurrentPosition();
        int rfPos = rightFrontMotor.getCurrentPosition();
        int lrPos = leftBackMotor.getCurrentPosition();
        int rrPos = rightBackMotor.getCurrentPosition();

        // calculate new targets
        lfPos += whatAngle * MOTOR_TICKS_COUNT;
        rfPos -= whatAngle * MOTOR_TICKS_COUNT;
        lrPos += whatAngle * MOTOR_TICKS_COUNT;
        rrPos -= whatAngle * MOTOR_TICKS_COUNT;

        // move robot to new position
        leftFrontMotor.setTargetPosition(lfPos);
        rightFrontMotor.setTargetPosition(rfPos);
        leftBackMotor.setTargetPosition(lrPos);
        rightBackMotor.setTargetPosition(rrPos);
        leftFrontMotor.setPower(speed);
        rightFrontMotor.setPower(speed);
        leftBackMotor.setPower(speed);
        rightBackMotor.setPower(speed);
        while (leftFrontMotor.isBusy() && rightFrontMotor.isBusy() &&
                leftBackMotor.isBusy() && rightBackMotor.isBusy()) {

            // Display it for the driver.
            telemetry.addLine("Turn Clockwise");

        }

        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightBackMotor.setPower(0);

    }

}
/*hello ryan*/
/*ye*/



