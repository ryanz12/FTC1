package org.firstinspires.ftc.teamcode.PIDController;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class armPID extends OpMode {
    private PIDController controller;

    public double p=0, i=0, d=0;
    public double f=0;
    public static int target = 0;
    private final double ticks_in_degree = 700 / 180.0;

    private DcMotorEx armLeft;

    @Override
    public void init(){
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        armLeft = hardwareMap.get(DcMotorEx.class, "armLeft");
    }

    @Override
    public void loop(){
        controller.setPID(p,i,d);

        int armPos = armLeft.getCurrentPosition();
        double pid = controller.calculate(armPos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

        double power = pid+ff;

        armLeft.setPower(power);

        telemetry.addData("pos", armPos);
        telemetry.addData("target", target);
        telemetry.update();
    }

}
