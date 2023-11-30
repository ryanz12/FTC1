package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.OpenCV.AprilTagCode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class RedLeftAuto extends LinearOpMode {
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

    private Servo intakeServo;
    public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor intakeMotor;

    boolean canSeeR = false;
    boolean canSeeL = false;

    boolean canSeen = false;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;


    @Override
    public void runOpMode() throws InterruptedException {
//        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFront");
//        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFront");
//        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBack");
//        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBack");
//
//        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        leftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        leftBackMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        rightFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//        rightBackMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        //Intake
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeServo = hardwareMap.servo.get("intakeServo");
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
        redDetector detector = new redDetector(telemetry);
        webcam.setPipeline(detector);

        //making the trajectory
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d myPose = new Pose2d(10, -60, Math.toRadians(90));
        drive.setPoseEstimate(myPose);

        //starting paths
        TrajectorySequence seqL = drive.trajectorySequenceBuilder(myPose)
                .back(5)
                .waitSeconds(1)
                .turn(Math.toRadians(180))
                .waitSeconds(1)
                .forward(30)
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    intakeServo.setPosition(0);
                    sleep(1000);
                })
                .waitSeconds(.3)
                .forward(1)
                .waitSeconds(1)
                .strafeRight(38)
                .waitSeconds(1)
                .turn(Math.toRadians(90))
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    moveArm(1000, 0.3);
                    sleep(1000);
                    moveIntake(800, 1);
                })
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    moveArm(0, .5);
                })
                .waitSeconds(1)
                .strafeLeft(30)
                .waitSeconds(1)
                .back(15)
                .build();


        TrajectorySequence seqR = drive.trajectorySequenceBuilder(myPose)
                .back(30)
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    intakeServo.setPosition(0);
                    sleep(1000);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(-90))
                .back(35)
                .strafeLeft(10)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    moveArm(800, 0.3);
                })
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    moveIntake(400, 0.5);
                })
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    moveArm(0, 0.15);
                })
                .waitSeconds(1)
                .strafeLeft(20)
                .waitSeconds(1)
                .back(14)
                .build();

        TrajectorySequence seqF = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(180))
                .forward(23)
                .build();


        //camera detection path
        TrajectorySequence seqSL = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(10))
                .waitSeconds(3)
                .build();
        TrajectorySequence backwards = drive.trajectorySequenceBuilder(new Pose2d(10,35, Math.toRadians(270)))
                .waitSeconds(1)
                .back(20)
                .build();

        TrajectorySequence seqSR = drive.trajectorySequenceBuilder(myPose)
                .turn(Math.toRadians(-10))
                .waitSeconds(3)
                .build();



        TrajectorySequence backwards_L = drive.trajectorySequenceBuilder(new Pose2d(10,35, Math.toRadians(0)))
                .waitSeconds(1)
                .back(3.4)
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
                        telemetry.addData("RUnning left Path", "True");
                        telemetry.update();
                        webcam.stopStreaming();
                        drive.followTrajectorySequence(seqL);
                        telemetry.addData("RUnning left Path", "False");
                        telemetry.update();
                        drive.followTrajectorySequence(backwards_L);
                        moveIntake(-300, .1);

//
                        Thread.sleep(100000);
                        break;
                    case MIDDLE:
                        webcam.stopStreaming();
                        drive.followTrajectorySequence(seqF);
                        moveIntake(-300, .1);
                        drive.followTrajectorySequence(backwards);
                        Thread.sleep(1000000);
                        break;
                    case RIGHT:
                        telemetry.addData("RUnning right Path", "True");
                        telemetry.update();
                        webcam.stopStreaming();
                        drive.followTrajectorySequence(seqR);
                        //drop pixel
                        moveIntake(-300, .1);
//                        drive.followTrajectorySequence(backwards);
                        Thread.sleep(100000);

                        telemetry.addData("RUnning right Path", "False");
                        telemetry.update();
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
    public void backward(int ticks, double speed){
        if(opModeIsActive()){
            leftFrontMotor.setTargetPosition(ticks);
            leftBackMotor.setTargetPosition(ticks);
            rightFrontMotor.setTargetPosition(ticks);
            rightBackMotor.setTargetPosition(ticks);

            leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftFrontMotor.setPower(speed);
            leftBackMotor.setPower(speed);
            rightFrontMotor.setPower(speed);
            rightBackMotor.setPower(speed);

            while(leftFrontMotor.isBusy()){
                telemetry.addData("Moving ","True");
                telemetry.update();
            }

            leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBackMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
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



