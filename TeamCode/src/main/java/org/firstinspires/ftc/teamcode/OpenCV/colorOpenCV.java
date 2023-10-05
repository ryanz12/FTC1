package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
//Themika
@Autonomous
public class colorOpenCV {
    private OpenCvWebcam webcam;
    private colorDetectionOpenCV cv = null;
    private LinearOpMode op;
    public colorOpenCV(LinearOpMode p_op){
        //you can input  a hardwareMap instead of linearOpMode if you want
        op = p_op;
        //initialize webcam
        webcam = OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "webcam"));
    }
    public void observeStick(){
        //create the pipeline
        cv = new colorDetectionOpenCV();

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                Autonomous code is here
                 */
                webcam.setPipeline(cv);
                //start streaming the camera
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
                //if you are using dashboard, update dashboard camera view
                /*FtcDashboard.getInstance().startCameraStream(webcam, 5);*/

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
