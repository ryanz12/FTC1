package org.firstinspires.ftc.teamcode.TheoreticalPaths;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

@Autonomous
public class leftblue extends LinearOpMode {
    private DcMotor armLeft;
    private DcMotor armRight;
    private DcMotor intakeMotor;
    private Servo intakeServo;

    @Override
    public void runOpMode(){
        intakeServo = hardwareMap.servo.get("intakeServo");

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

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPos = new Pose2d(12,60, Math.toRadians(90));

        drive.setPoseEstimate(startPos);

        TrajectorySequence trajLeft = drive.trajectorySequenceBuilder(startPos)
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
                .back(1)
                .waitSeconds(1)
                .strafeLeft(38)
                .waitSeconds(1)
                .turn(Math.toRadians(-90))
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
                .strafeRight(45)
                .waitSeconds(1)
                .back(25)
                .build();

        TrajectorySequence trajMiddle = drive.trajectorySequenceBuilder(startPos)
                .back(5)
                .waitSeconds(1)
                .turn(Math.toRadians(180))
                .waitSeconds(1)
                .forward(20)
                .waitSeconds(1)
                .turn(Math.toRadians(-90))
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    intakeServo.setPosition(0);
                })
                .waitSeconds(1)
                .forward(33)
                .turn(Math.toRadians(-180))
                .waitSeconds(1)
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
                .strafeLeft(24)
                .waitSeconds(1)
                .back(14)
                .build();

        TrajectorySequence trajRight = drive.trajectorySequenceBuilder(startPos)
                .back(30)
                .waitSeconds(1)
                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                    intakeServo.setPosition(0);
                })
                .waitSeconds(1)
                .turn(Math.toRadians(90))
                .back(33)
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
                .strafeRight(30)
                .waitSeconds(1)
                .back(14)
                .build();

        waitForStart();
        if(isStopRequested()) return;

        drive.followTrajectorySequence(trajLeft);
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
            armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
