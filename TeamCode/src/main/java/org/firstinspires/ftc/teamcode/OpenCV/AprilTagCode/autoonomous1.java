package org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.OpenCV.OpticalAutonomousDriver;
import org.firstinspires.ftc.teamcode.OpenCV.YellowDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class autoonomous1 extends LinearOpMode {
    OpenCvWebcam webcam = null;
    public enum loc{
        Left,
        Right,
        Middle
    }
    private OpticalAutonomousDriver.loc location;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;


    public DcMotor armLeft;
    public DcMotor armRight;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;


    boolean canSeeMiddle = false;
    boolean canSeeRight = false;
    boolean canSeeLeft = false;

    @Override
    public void runOpMode() throws InterruptedException {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        YellowDetector detector = new YellowDetector(telemetry);
        webcam.setPipeline(detector);
        //making the trajectory
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d myPose = new Pose2d(10, -5, Math.toRadians(90));
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())

                .forward(25)

                .build();

        //making the trajectory

        Trajectory traj2 = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(6.5)
                .forward(25)
                .forward(15)
                        .build();

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

                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(-90));

                        //drop pixel


                        moveArm(-1200);
                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
                    case MIDDLE:
                        webcam.stopStreaming();
                        webcam.setPipeline(aprilTagDetectionPipeline);
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);

                        //drop pixel
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(-90));
                        drive.followTrajectory(traj1);

                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);

                    case RIGHT:
                        webcam.stopStreaming();
                        webcam.setPipeline(aprilTagDetectionPipeline);
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(90));
                        //drop it off


                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj2);
                        //drop off pixel



                        webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
                        break;
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
    public void moveArm(int ticks) {
        if (opModeIsActive()) {
            armLeft.setTargetPosition(ticks);
            armRight.setTargetPosition(ticks);

            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            armLeft.setPower(0.2);
            armRight.setPower(0.2);

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
}
