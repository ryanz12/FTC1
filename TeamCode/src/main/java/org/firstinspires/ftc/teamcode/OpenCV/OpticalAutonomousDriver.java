package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

import java.util.ArrayList;
import java.util.List;

//Themika pipeline and camera.
//Sean and ryan Movement and roadrunner
@Autonomous
public class OpticalAutonomousDriver extends LinearOpMode {
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
                        drive.turn(Math.toRadians(-90));
                        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())

                                .strafeRight(24)
                                .build();


                        // than drop pixel
                        break;
                    case RIGHT:
                        drive.turn(Math.toRadians(90));
                        Trajectory myTrajectory2 = drive.trajectoryBuilder(new Pose2d())

                                .strafeLeft(24)
                                .build();
                        // drop pixel
                        break;
                    case NOT_FOUND:
                        Trajectory myTrajectory3 = drive.trajectoryBuilder(new Pose2d())
                                .forward(24)
                                .build;
                        //drop pixel
                        break;

                }
            }
        }
        webcam.stopStreaming();

    }

}

