package org.firstinspires.ftc.teamcode.OpenCV;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
            try{
                if(ColorPipeline.canSee == true){
                    // Autonomous code
                    telemetry.addLine("HAS FOUND THING RUN AUTONOMOUS CODE");
                }
                else{
                    // Look for object
                    telemetry.addLine("HAS NOT FOUND THING RUN AUTONOMOUS CODE");

                }
            }catch (Exception e){
                telemetry.addLine("CHECK THE WONKY BOOLEAN CODE");
                telemetry.addLine(e.getMessage());
            }
        }
//        stopStreaming
        cv.stopCamera();
    }

}
