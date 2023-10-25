package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public class directionColorPipeline extends OpenCvPipeline {

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

        }
        else if(leftAvgFin < rightAvgFin){

        }
        else{
            canTSee = true;
        }


        return(output);

    }

}


