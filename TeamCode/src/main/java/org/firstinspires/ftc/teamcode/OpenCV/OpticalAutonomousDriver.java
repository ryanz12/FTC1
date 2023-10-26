package org.firstinspires.ftc.teamcode.OpenCV;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

//Themika pipeline and camera.
//Sean and ryan Movemnt and roadrunner
@Config
@Autonomous
public class OpticalAutonomousDriver extends LinearOpMode {
//h
    OpenCvWebcam webcam = null;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    FtcDashboard dashboard;

    boolean canSeeMiddle = false;
    boolean canSeeRight = false;
    boolean canSeeLeft = false;

    @Override
    public void runOpMode() throws InterruptedException {
        dashboard = FtcDashboard.getInstance();
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        webcam.setPipeline(new directionColorPipeline());

        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFront");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFront");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBack");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBack");
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addLine("HSHSHHHDHDHDHDHDHD");
            }
        });
        while(opModeIsActive()){

        }
    }
    class directionColorPipeline extends OpenCvPipeline {

        //Creates a YCbc mat which is YCbCr represents color as brightness and two color difference signals, while RGB represents color as red, green and blue. In YCbCr, the Y is the brightness
        Mat YCbC = new Mat();
        //Left side mat
        Mat lCrop;
        //Right side mat
        Mat rCrop;
        //Left average final variable which is used to identify that the left side has more of the color.
        double leftAvgFin = 0;
        //Rght average final variable which is used to identify that the left side has more of the color.
        double rightAvgFin = 0;
        //Boolean which identify which directional path to go.
        public boolean canSeeLeft;
        public boolean canSeeRight;
        public boolean canTSee;

        //Mat too see out put
        Mat output = new Mat();
        //Color can be changed to any color knowing rbg values right now it is red
        Scalar rectColor = new Scalar(255,0,0);
        @Override
        public Mat processFrame(Mat input) {
            //Converts the input mat to preferred color type
            Imgproc.cvtColor(input,YCbC,Imgproc.COLOR_RGB2YCrCb);

            //Splits the screen into two using a function similar to javas rect function
            org.opencv.core.Rect leftRect = new org.opencv.core.Rect(1,1,319,479);
            org.opencv.core.Rect rightRect = new Rect(320,1,319,479);

            //To see the actual split on the camera stream
            input.copyTo(output);
            Imgproc.rectangle(output,leftRect,rectColor,2);
            Imgproc.rectangle(output,rightRect,rectColor,2);

            //Create a submat of all to detect the color
            lCrop = YCbC.submat(leftRect);
            rCrop = YCbC.submat(rightRect);
            //Finds the average amount of color on both sides
            Scalar leftAvg = Core.mean(lCrop);
            Scalar rightAvg = Core.mean(rCrop);

            //The zero element is where the avg is stored.
            leftAvgFin = leftAvg.val[0];
            rightAvgFin = rightAvg.val[0];

            //Idea
            /*
            SO basically first we turn the robot to see color. Once identified turn fully that
             way. Place pixel. Turn to center the robot. Move the robot around the Object.
             Then stop using that camera pipeline and switch to the april tag pipeline.
             */
            //Finds where the color is most prominent
            if(leftAvgFin > rightAvgFin){
                telemetry.addLine("Yay left");
                canSeeLeft = true;
                //To move left
            }
            else if(leftAvgFin < rightAvgFin){
                telemetry.addLine("Yay right");
                canSeeRight = true;
                //to move right
            }
            else if(leftAvgFin == rightAvgFin){
                telemetry.addLine("Middle");
                canSeeMiddle = true;
            }
            else{
                canSeeLeft = false;
                canSeeRight = false;
                canSeeMiddle = false;
                telemetry.addLine("You done Fucked up");
            }
            //returns the split screen
            return(output);

        }

    }




}

