package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous
public class OpticalAutonomousDriver extends OpMode {
    OpenCvWebcam webcam = null;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam");
        int cameraViewID = hardwareMap.appContext.getResources().getIdentifier("cameraViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName,cameraViewID);
        webcam.setPipeline(new directioColorPipeline());

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


    @Override
    public void loop() {

    }
    class directioColorPipeline extends OpenCvPipeline {

        Mat YCbC = new Mat();
        Mat lCrop;
        Mat rCrop;
        double leftAvgFin = 1;
        double rightAvgFin = 1;
        public boolean canSeeLeft;
        public boolean canSeeRight;
        public boolean canTSee;

        Mat output = new Mat();
        Scalar rectColor = new Scalar(255,0,0);
        @Override
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input,YCbC,Imgproc.COLOR_RGB2YCrCb);

            org.opencv.core.Rect leftRect = new org.opencv.core.Rect(1,1,319,479);
            org.opencv.core.Rect rightRect = new Rect(320,1,319,479);

            input.copyTo(output);
            Imgproc.rectangle(output,leftRect,rectColor,2);
            Imgproc.rectangle(output,rightRect,rectColor,2);

            lCrop = YCbC.submat(leftRect);
            rCrop = YCbC.submat(rightRect);

            Scalar leftAvg = Core.mean(lCrop);
            Scalar rightAvg = Core.mean(rCrop);

            leftAvgFin = leftAvg.val[0];
            rightAvgFin = rightAvg.val[0];

            if(leftAvgFin > rightAvgFin){
                telemetry.addLine("Yay left");
                //To move left


            }
            else if(leftAvgFin < rightAvgFin){
                telemetry.addLine("Yay right");
                leftFrontMotor.setPower(1);
                //to move right
            }
            else if(leftAvgFin == rightAvgFin){
                canTSee = true;
                telemetry.addLine("Middle");
            }
            else{
                telemetry.addLine("You done Fucked up");
            }


            return(output);

        }

    }




}

