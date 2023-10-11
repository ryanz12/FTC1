package org.firstinspires.ftc.teamcode.OpenCV;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
//Themika
@Autonomous
public class colorOpenCV extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {

    }
    private OpenCvWebcam webcam;
    private ColorPipeline opencv = null;
    private LinearOpMode op;
    public colorOpenCV(LinearOpMode p_op){
        //you can input  a hardwareMap instead of linearOpMode if you want
        op = p_op;
        //initialize webcam
        webcam = OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "Webcam"));
    }
    public void observeStick(){
        //create the pipeline
        opencv = new ColorPipeline();

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.setPipeline(opencv);
                //start streaming the camera
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                //if you are using dashboard, update dashboard camera view
                /*FtcDashboard.getInstance().startCameraStream(webcam, 5);*/
                try{
                    if(true){
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

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    //stop streaming
    public void stopCamera(){
        webcam.stopStreaming();
    }

}
