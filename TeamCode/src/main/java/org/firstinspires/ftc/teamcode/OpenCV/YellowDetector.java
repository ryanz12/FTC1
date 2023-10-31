package org.firstinspires.ftc.teamcode.OpenCV;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class YellowDetector extends OpenCvPipeline {
    public Telemetry telemetry;
    Mat mat = new Mat();

    public enum Location{
        Left,
        Right,
        NotFound
    }
    private Location location;
    static double Percentage = 0.4;
    static final Rect left_Roi = new Rect(
        new Point(60,35),
        new Point(120,75));

    static final Rect right_Roi = new Rect(
        new Point(140,35),
        new Point(200,75));

    public YellowDetector(Telemetry t){t= telemetry;}
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input,mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHsv = new Scalar(23,50,70);
        Scalar highHsv = new Scalar(32,255,255);

        Core.inRange(mat,lowHsv,highHsv,mat);

        Mat left = mat.submat(left_Roi);
        Mat right = mat.submat(right_Roi);

        double leftValue = Core.sumElems(left).val[0] / left_Roi.area()/255;
        double rightValue = Core.sumElems(right).val[0] / left_Roi.area()/255;

        left.release();
        right.release();

        telemetry.addData("Left Raw" , (int)Core.sumElems(left).val[0]);
        telemetry.addData("Left Raw" , (int)Core.sumElems(right).val[0]);
        telemetry.addData("right percent", Math.round(rightValue * 100) + "%");
        telemetry.addData("left percent", Math.round(leftValue * 100) + "%");

        boolean stoneLeft = leftValue > Percentage;
        boolean stoenRight = rightValue > Percentage;

        if(stoneLeft && stoenRight){
            location = Location.NotFound;
            telemetry.addData("Loc", "notfind");
        }
        if(stoneLeft){
            location = Location.Left;
            telemetry.addData("Loc", "Left");
        }else {
            location = Location.Right;
            telemetry.addData("Loc", "Right");
        }
        telemetry.update();
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_GRAY2BGR);
        return mat;
    }
    public Location getLocation(){
        return location;
    }
}
