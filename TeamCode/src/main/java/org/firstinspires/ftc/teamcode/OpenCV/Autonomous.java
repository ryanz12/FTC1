package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode {

/*gg*/
    public void runOpMode() {

//       initialize camera and pipeline
        colorOpenCV cv = new colorOpenCV(this);
//      call the function to startStreaming
        cv.observeStick();
        cv.observeStick();
        waitForStart();
        while (opModeIsActive()) {

        }
//        stopStreaming
        cv.stopCamera();
    }

}
/*hello ryan*/