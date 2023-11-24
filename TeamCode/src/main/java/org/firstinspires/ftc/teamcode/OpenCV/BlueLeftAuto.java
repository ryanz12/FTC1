package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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

@Autonomous
public class BlueLeftAuto extends LinearOpMode {
    //Change LinearOpMode If necessary
    //
    OpenCvWebcam webcam = null;
    public enum loc{
        Left,
        Right,
        Middle
    }
    private auto3.loc location;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;


    public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor intakeMotor;

    boolean canSeeR = false;
    boolean canSeeL = false;

    boolean canSeen = false;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;


    @Override
    public void runOpMode() throws InterruptedException {
        //Intake
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Arm
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

        //Webcam
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        YellowDetector detector = new YellowDetector(telemetry);
        webcam.setPipeline(detector);

        //making the trajectory
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d myPose = new Pose2d(0, 0, Math.toRadians(0));
        drive.setPoseEstimate(myPose);
        TrajectorySequence seqL = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(180))
                .forward(25)
                .turn(Math.toRadians(90))
                .build();

        TrajectorySequence seqR = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(180))
                .forward(25)
                .turn(Math.toRadians(-90))
                .build();

        TrajectorySequence seqF = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(180))
                .forward(24)

                .build();

        TrajectorySequence seqSL = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(5))
                .waitSeconds(3)
                .build();
        TrajectorySequence backward = drive.trajectorySequenceBuilder(myPose)
                .waitSeconds(1)
                .back(20)
                .build();
        TrajectorySequence seqSR = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(-10))
                .waitSeconds(3)
                .build();



        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {webcam.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);}

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
                        drive.followTrajectorySequence(seqL);
                        Thread.sleep(10000);

                        break;
                    case MIDDLE:
                        webcam.stopStreaming();
                        drive.followTrajectorySequence(seqF);
                        moveIntake(-200, .1);
                        drive.followTrajectorySequence(backward);
                        Thread.sleep(10000);

                        break;
                    case RIGHT:
                        webcam.stopStreaming();
                        drive.followTrajectorySequence(seqR);
                        //drop pixel
                        moveIntake(-200,0.1);

                        Thread.sleep(10000);

                        break;

                    case NOT_FOUND:
                        if(canSeen == false)
                            if(canSeeL == false){
                                drive.followTrajectorySequence(seqSL);
                                canSeen = true;
                                break;
                            }if(canSeeR == false){
                                drive.followTrajectorySequence(seqSR);
                                canSeen = true;
                                break;
                            }
                        //
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



