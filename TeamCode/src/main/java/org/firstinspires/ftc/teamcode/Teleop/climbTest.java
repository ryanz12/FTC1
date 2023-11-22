package org.firstinspires.ftc.teamcode.Teleop;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class climbTest extends LinearOpMode {
    private DcMotor armLeft;
    private DcMotor armRight;
    public double tgtPower;

    @Override
    public void runOpMode(){
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armLeft");

        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());
        armRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE.getBehavior());

        boolean climbing = false;
        int interval = 50;

        waitForStart();
        while(!isStopRequested()){
            if (gamepad1.y && !climbing) {
                climbing = true;
                tgtPower = .5;

                telemetry.addData("trying","To move");
                telemetry.update();
            } else if (!gamepad1.y && climbing) {

                for (int i = 0; i < interval; i++) {
                    armLeft.setPower(tgtPower * (interval - i) / interval);
                    armRight.setPower(tgtPower * (interval - i) / interval);
                    sleep(20);
                }

                climbing = false;
                tgtPower = 0;
            }

            armLeft.setPower(tgtPower);
            armRight.setPower(tgtPower);

            telemetry.addData("Climbing", climbing);
            telemetry.update();
        }
    }
}
