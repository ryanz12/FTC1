package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class RedLeftAuto extends LinearOpMode {
    OpenCvWebcam webcam = null;
    public DcMotor armLeft;
    public DcMotor armRight;
    public DcMotor intakeMotor;

    @Override
    public void runOpMode(){
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

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {webcam.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);}

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("HSHSHHHDHDHDHDHDHD");
            }
        });

        //Paths
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPos = new Pose2d(-36, -60, Math.toRadians(270));

        TrajectorySequence pathLeft = drive.trajectorySequenceBuilder(startPos)
                .back(4)
                .turn(Math.toRadians(179))
                .waitSeconds(1)
                .splineTo(new Vector2d(-37, -34), Math.toRadians(180))
                .back(5)
                .addDisplacementMarker(() -> {
                    moveIntake(-400, 0.05);
                })
//                .splineTo(new Vector2d(-37, -11), 0)
//                .waitSeconds(1)
//                .splineTo(new Vector2d(20, 0), 0)
//                .splineTo(new Vector2d(40, -30), 0)
//                .waitSeconds(1)
//                .turn(Math.toRadians(179))
//                .waitSeconds(1)
//                .back(8)
//                .waitSeconds(5)
//                .strafeRight(20)
//                .waitSeconds(1)
//                .back(10)
//                .waitSeconds(5)
                .build();

        TrajectorySequence pathMiddle = drive.trajectorySequenceBuilder(startPos)
                .back(4)
                .turn(Math.toRadians(179))
                .waitSeconds(1)
                .splineTo(new Vector2d(-36, -35), Math.toRadians(90))
                .addDisplacementMarker(() -> {
                    moveIntake(-400, 0.05);
                })
//                .strafeRight(76)
//                .waitSeconds(1)
//                .turn(Math.toRadians(90))
//                .waitSeconds(1)
//                .back(8)
//                .addDisplacementMarker(() -> {
//                    moveArm(800, 0.3);
//                    moveIntake(400, 0.5);
//                    moveArm(0, .2);
//                })
//                .strafeRight(25)
//                .waitSeconds(1)
//                .back(11)
                .build();

        TrajectorySequence pathRight = drive.trajectorySequenceBuilder(startPos)
                .back(4)
                .turn(Math.toRadians(179))
                .waitSeconds(1)
                .splineTo(new Vector2d(-35, -33), 0)
                .back(5)
                .addDisplacementMarker(() -> {
                    moveIntake(-400, 0.05);
                })
//                .strafeRight(3)
//                .waitSeconds(1)
//                .strafeRight(25)
//                .forward(44)
//                .waitSeconds(1)
//                .splineTo(new Vector2d(40, -41), 0)
//                .turn(Math.toRadians(179))
//                .back(8)
//                .addDisplacementMarker(() -> {
//                    moveArm(800, 0.3);
//                    moveIntake(400, 0.5);
//                    moveArm(0, .2);
//                })
//                .strafeRight(30)
//                .waitSeconds(1)
//                .back(10)
                .build();

        waitForStart();
        while(opModeIsActive()){
            if (detector.getLocation() != null) {
                switch (detector.getLocation()) {
                    case LEFT:
                        drive.followTrajectorySequence(pathLeft);
                        break;
                    case MIDDLE:
                        drive.followTrajectorySequence(pathMiddle);
                        break;
                    case RIGHT:
                        drive.followTrajectorySequence(pathRight);
                        break;
                    case NOT_FOUND:
                        drive.followTrajectorySequence(pathMiddle);
                        break;
                }
            }
        }
        webcam.stopStreaming();
//
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

            armLeft.setPower(0);
            armRight.setPower(0);

            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}
