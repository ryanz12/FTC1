package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

//hey!!
//Themika pipeline and camera.
//Sean and ryan Movement and roadrunner
@Disabled
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

    //Add servo to guide arm;
    private Servo guideArm;

    public DcMotor armLeft;
    public DcMotor armRight;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;


    @Override
    public void runOpMode() throws InterruptedException {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        redDetector detector = new redDetector(telemetry);
        webcam.setPipeline(detector);
        guideArm = hardwareMap.servo.get("guideArm");
        guideArm.setPosition(1);
        guideArm.setPosition(0);
        //making the trajectory
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d myPose = new Pose2d(10, -5, Math.toRadians(90));
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())


                                .forward(25)

                                .build();

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);
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
                        drive.turn(Math.toRadians(-90));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(-90));
                        drive.followTrajectory(traj1);
                        moveArm(-1200);
                        break;
                    case MIDDLE:
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);

                        //drop pixel
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(-90));
                        drive.followTrajectory(traj1);

                        break;
                    case RIGHT:
                        drive.turn(Math.toRadians(180));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(90));
                        //drop it off
                        drive.turn(Math.toRadians(90));
                        drive.followTrajectory(traj1);
                        drive.turn(Math.toRadians(-90));
                        drive.followTrajectory(traj1);
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


