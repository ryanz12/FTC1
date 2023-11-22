package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class IntakeTest extends LinearOpMode {
    public DcMotor intakeMotor;

    public double motorPower=0;
    public void runOpMode(){
        intakeMotor=hardwareMap.get(DcMotor.class, "intakeMotor");

        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while(!isStopRequested()){
            if(gamepad1.left_trigger > 0){
                motorPower=-0.5;
            }
            else if(gamepad1.right_trigger > 0){
                motorPower=0.8;
            }
            else if(gamepad1.a){
                moveIntake(400, 1);
            }
            else{
                motorPower=0;
            }

            intakeMotor.setPower(motorPower);
        }
    }


    public void moveIntake(int ticks, double speed){
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
