package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode {

/*gg*/
@Override
public void runOpMode() {
//        initialize camera and pipeline
    colorOpenCV cv = new colorOpenCV(this);
    ColorPipeline colorPipeline = new ColorPipeline();
//      call the function to startStreaming
    cv.observeStick();
    waitForStart();
    while (opModeIsActive()) {
        if (colorPipeline.canSee = true){
            telemetry.addLine("Hello");
        }
        else{
            telemetry.addLine("NOOOOO none detected");
        }
    }
//        stopStreaming
    cv.stopCamera();
}

}
/*hello ryan*/