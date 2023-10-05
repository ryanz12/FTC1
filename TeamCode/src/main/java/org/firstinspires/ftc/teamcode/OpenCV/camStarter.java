package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class camStarter extends LinearOpMode {
    @Override
    //
    public void runOpMode() {
//        initialize camera and pipeline
        colorOpenCV cv = new colorOpenCV(this);
//      call the function to startStreaming
        cv.observeStick();
        waitForStart();
        while (opModeIsActive()) {
        }
//        stopStreaming
        cv.stopCamera();
    }

}
