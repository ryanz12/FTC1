package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

//hey!!
//Themika pipeline and camera.
//Sean and ryan Movement and roadrunner
@Autonomous
public class cameraTest extends LinearOpMode {
    //Change LinearOpMode If necessary
    OpenCvWebcam webcam = null;
    public enum loc{
        Left,
        Right,
        Middle
    }
    private  loc location;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;


    public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor intakeMotor;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;


    boolean canSeeMiddle = false;
    boolean canSeeRight = false;
    boolean canSeeLeft = false;

    @Override
    public void runOpMode() throws InterruptedException {
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());
        armRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        YellowDetector detector = new YellowDetector(telemetry);
        webcam.setPipeline(detector);
        //making the trajectory
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("HSHSHHHDHDHDHDHDHD");
            }
        });

        waitForStart();
        while(opModeIsActive()){
            if (detector.getLocation() != null) {
                switch (detector.getLocation()) {
                    case LEFT:
                        webcam.stopStreaming();
                        webcam.setPipeline(aprilTagDetectionPipeline);
                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);

                        telemetry.addData("left detected", "True");
                        telemetry.update();
                    case MIDDLE:
                        webcam.stopStreaming();
                        webcam.setPipeline(aprilTagDetectionPipeline);
                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);

                        telemetry.addData("middle detected", "True");
                        telemetry.update();
                    case RIGHT:
                        webcam.stopStreaming();
                        webcam.setPipeline(aprilTagDetectionPipeline);
                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);

                        telemetry.addData("right detected", "True");
                        telemetry.update();
                    case NOT_FOUND:
                        break;

                }
            }
        }
        webcam.stopStreaming();

    }

    public void forward(int inchesForward) {
        double circumference = 3.14 * 2.938;


        double rotationsNeeded = inchesForward / circumference;
        int encoderDrivingTarget = (int) (rotationsNeeded * 1200);
        leftFrontMotor.setTargetPosition(encoderDrivingTarget);
        rightFrontMotor.setTargetPosition(encoderDrivingTarget);
        leftBackMotor.setTargetPosition(encoderDrivingTarget);
        rightBackMotor.setTargetPosition(encoderDrivingTarget);

    }
    public void moveArm(int ticks, double speed) {
        if (opModeIsActive()) {
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(speed);
            armRight.setPower(speed);

            while (opModeIsActive() && (armLeft.isBusy() && armRight.isBusy())) {
                telemetry.addData("Running to", " %7d :%7d", ticks, ticks);
                telemetry.addData("Currently at", " at %7d :%7d",
                        armLeft.getCurrentPosition(), armRight.getCurrentPosition());
                telemetry.update();
            }
            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
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


