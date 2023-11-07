package org.firstinspires.ftc.teamcode.OpenCV;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
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
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    //--IntakeMotor
    //--public DcMotor intakeMotor;


    @Override
    public void runOpMode() throws InterruptedException {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraViewID);
        YellowDetector detector = new YellowDetector(telemetry);
        webcam.setPipeline(detector);
        /*
          intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");
          SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
          Trajectory leftTrajectory = drive.trajectoryBuilder(new Pose2d())
                  .strafeLeft(24)
                  .lineToConstantHeading(new Vector2d(24, 24))
                  .build();
          Trajectory middleTrajectory = drive.trajectoryBuilder(new Pose2d())
                  .forward(30)
                  .build();
          Trajectory rightTrajectory = drive.trajectoryBuilder(new Pose2d())
                  .strafeTo(new Vector2d(36, 0))
                  .build();
        Trajectory not_found = drive.trajectoryBuilder(new Pose2d())
                 .strafeLeft(24)
                 .strafeRight(24)
                 .build();
        */
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("HSHSHHHDHDHDHDHDHD");
            }
        });

        waitForStart();
        while (opModeIsActive()) {
            if (detector.getLocation() != null) {
                switch (detector.getLocation()) {
                    case LEFT:
                        //Autonomous code
                        webcam.stopStreaming();
                        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                        webcam.setPipeline(aprilTagDetectionPipeline);
//                        drive.followTrajectory(leftTrajectory);
//                        intakeMotor.setPower(-0.5);
                        break;
                    case MIDDLE:
                        //Autonomous code
                        webcam.stopStreaming();
                        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                        webcam.setPipeline(aprilTagDetectionPipeline);
//                         drive.followTrajectory(middleTrajectory);
//                         intakeMotor.setPower(-0.5);
                        break;
                    case RIGHT:
                        //Autonomous code
                        webcam.stopStreaming();
                        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                        webcam.setPipeline(aprilTagDetectionPipeline);
//                         drive.followTrajectory(rightTrajectory);
//                         intakeMotor.setPower(-0.5);
                        break;
                    case NOT_FOUND:

                }
            }
        }
        webcam.stopStreaming();

    }
}



