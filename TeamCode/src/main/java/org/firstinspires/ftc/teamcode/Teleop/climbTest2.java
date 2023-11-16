package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class climbTest2 extends LinearOpMode{
    public DcMotor armLeft;
    public DcMotor armRight;
    public double tgtPower;

    @Override
    public void runOpMode() {
        armLeft = hardwareMap.get(DcMotor.class, "armLeft");
        armRight = hardwareMap.get(DcMotor.class, "armRight");

        waitForStart();

        boolean climbing = false;

        while (!isStopRequested()) {
            if (gamepad1.y && !climbing) {
                climbing = true;
                tgtPower = .5;

                telemetry.addData("trying","To move");
                telemetry.update();
            } else if (!gamepad1.y && climbing) {

                for (int i = 0; i < 100; i++) {
                    armLeft.setPower(tgtPower * (100 - i) / 100);
                    armRight.setPower(tgtPower * (100 - i) / 100);
                    sleep(20);
                }

                climbing = false;
                tgtPower = 0;
            }

            armLeft.setPower(tgtPower);
            armRight.setPower(tgtPower);

        }
    }

}
