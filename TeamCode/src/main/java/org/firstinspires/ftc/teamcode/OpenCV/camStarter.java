package org.firstinspires.ftc.teamcode.OpenCV;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import  org.firstinspires.ftc.teamcode.OpenCV.colorOpenCV;
@Autonomous
public class camStarter extends LinearOpMode {
    @Override

    public void runOpMode() {
//        initialize camera and pipeline
        colorOpenCV cv = new colorOpenCV(this);
//      call the function to startStreaming
        cv.observeStick();
        waitForStart();
        while (opModeIsActive()) {
            if(ColorPipeline.canSee == true){
                // Autonomous code
            }
            else{
                // Look for object
            }
        }
//        stopStreaming
        cv.stopCamera();
    }

}
