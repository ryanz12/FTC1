package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.List;

//Themika pipeline and camera.
//Sean and ryan Movemnt and roadrunner
@Autonomous
public class OpticalAutonomousDriver extends LinearOpMode {
//Change LinearOpMode If necessary
    OpenCvWebcam webcam = null;
    public enum loc{
        Left,
        Right,
        NotFound
    }
    private  loc location;
    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;


    boolean canSeeMiddle = false;
    boolean canSeeRight = false;
    boolean canSeeLeft = false;

    @Override
    public void runOpMode() throws InterruptedException {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        webcam.setPipeline(new ColorPipeline());

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
    }

    class directionColorPipeline extends OpenCvPipeline {

        //Creates a YCbc mat which is YCbCr represents color as brightness and two color difference signals, while RGB represents color as red, green and blue. In YCbCr, the Y is the brightness
        Mat YCbC = new Mat();
        //Left side mat
        Mat lCrop;
        //Right side mat
        Mat rCrop;
        //Left average final variable which is used to identify that the left side has more of the color.
        double leftAvgFin;
        //Rght average final variable which is used to identify that the left side has more of the color.
        double rightAvgFin;

        //Mat too see out put
        Mat output = new Mat();
        //Color can be changed to any color knowing rbg values right now it is red
        Scalar rectColor = new Scalar(255.0,0.0,0);
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
            telemetry.addData("LeftAvg", leftAvg);
            Scalar rightAvg = Core.mean(rCrop);
            telemetry.addData("RightAvg", rightAvg);

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
            else if(leftAvgFin < 130 || rightAvgFin <130){
                telemetry.addLine("Can't see");
                canSeeMiddle = true;
            }
            else{
                telemetry.addLine("Middle");
            }
            //returns the split screen
            return(output);

        }

    }
    class ColorPipeline extends OpenCvPipeline {
        //backlog of frames to average out to reduce noise
        ArrayList<double[]> frameList;
        //these are public static to be tuned in dashboard
        public  double strictLowS = 150;
        public  double strictHighS = 255;

        public ColorPipeline() {
            frameList = new ArrayList<>();
        }
/////////////////////////////////////////////////////////////////////////////Logic/////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public Mat processFrame(Mat input) {
            Mat mat = new Mat();


            //mat turns into HSV value
            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
            if (mat.empty()) {
                return input;
            }

            // lenient bounds will filter out near yellow, this should filter out all near yellow things(tune this if needed)
            Scalar lowHSV = new Scalar(20, 70, 80); // lenient lower bound HSV for yellow
            Scalar highHSV = new Scalar(32, 255, 255); // lenient higher bound HSV for yellow
//          Scalar lowHSV =  new Scalar(0, 57, 49);//RED
//          Scalar highHSV = new Scalar(10, 100, 50);//RED


            Mat thresh = new Mat();

            // Get a black and white image of yellow objects
            Core.inRange(mat, lowHSV, highHSV, thresh);

            Mat masked = new Mat();
            //color the white portion of thresh in with HSV from mat
            //output into masked
            Core.bitwise_and(mat, mat, masked, thresh);
            //calculate average HSV values of the white thresh values
            Scalar average = Core.mean(masked, thresh);

            Mat scaledMask = new Mat();
            //scale the average saturation to 150
            masked.convertTo(scaledMask, -1, 150 / average.val[1], 0);


            Mat scaledThresh = new Mat();
            //you probably want to tune this
            Scalar strictLowHSV = new Scalar(0, strictLowS, 0); //strict lower bound HSV for yellow
            Scalar strictHighHSV = new Scalar(255, strictHighS, 255); //strict higher bound HSV for yellow

            //apply strict HSV filter onto scaledMask to get rid of any yellow other than pole
            Core.inRange(scaledMask, strictLowHSV, strictHighHSV, scaledThresh);

            Mat finalMask = new Mat();
            //color in scaledThresh with HSV, output into finalMask(only useful for showing result)(you can delete)
            Core.bitwise_and(mat, mat, finalMask, scaledThresh);

            Mat edges = new Mat();
            //detect edges(only useful for showing result)(you can delete)
            Imgproc.Canny(scaledThresh, edges, 100, 200);

            //contours, apply post processing to information
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            //find contours, input scaledThresh because it has hard edges
            Imgproc.findContours(scaledThresh, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            //list of frames to reduce inconsistency, not too many so that it is still real-time, change the number from 5 if you want
            if (frameList.size() > 5) {
                frameList.remove(0);
            }


            //release all the data
            input.release();
            scaledThresh.copyTo(input);
            scaledThresh.release();
            scaledMask.release();
            mat.release();
            masked.release();
            edges.release();
            thresh.release();
            finalMask.release();
            //change the return to whatever mat you want
            //for example, if I want to look at the lenient thresh:
            // return thresh;
            // note that you must not do thresh.release() if you want to return thresh
            // you also need to release the input if you return thresh(release as much as possible)
///////////////////////////////////////////////////////////////////Visualization///////////////////////////////////////////////////////////////////////////////////////////////

            telemetry.addLine("Logic is about to run");
            ///Finding values
            Mat output = new Mat();
            //Color can be changed to any color knowing rbg values right now it is red
            Scalar rectColor = new Scalar(250,0,0);

            double percentage = 0.4;
            //Splits the screen into two using a function similar to javas rect function
            org.opencv.core.Rect leftRect = new org.opencv.core.Rect(1,1,319,479);
            org.opencv.core.Rect rightRect = new Rect(320,1,319,479);

            //To see the actual split on the camera stream
            input.copyTo(output);
            Imgproc.rectangle(output,leftRect,rectColor,2);
            Imgproc.rectangle(output,rightRect,rectColor,2);

            Mat left = mat.submat(leftRect);
            Mat right = mat.submat(rightRect);

            double leftValue = Core.sumElems(left).val[0] / leftRect.area() / 255;
            double rightValue = Core.sumElems(right).val[0] / rightRect.area() / 255;
            left.release();
            right.release();

            telemetry.addData("Left value Raw", (int) Core.sumElems(left).val[0]);
            telemetry.addData("Right value Raw", (int) Core.sumElems(right).val[0]);


            boolean leftSide = leftValue > percentage;
            boolean rightSide = rightValue > percentage;

            if(leftSide && rightSide){
                telemetry.addLine("Not Found");
                location = loc.NotFound;
            }
            if(leftSide){
                telemetry.addLine("Right side");
                location = loc.Left;


            }else{
                telemetry.addLine("Left");
                location = loc.Right;
            }


            return output;
        }
    }

}

