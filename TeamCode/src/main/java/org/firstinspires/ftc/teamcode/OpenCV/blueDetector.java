package org.firstinspires.ftc.teamcode.OpenCV;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class blueDetector extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();
    public enum Location {
        LEFT,
        RIGHT,
        MIDDLE,
        NOT_FOUND
    }
    private blueDetector.Location location;

    static final Rect LEFT_ROI = new Rect(
            new Point(0, 0),
            new Point(425, 720));
    static final Rect MIDDLE_ROI = new Rect(
            new Point(426, 0),
            new Point(851, 720));
    static final Rect RIGHT_ROI = new Rect(
            new Point(853, 0),
            new Point(1280, 720));
    static double PERCENT_COLOR_THRESHOLD = 0.02;

    public blueDetector(Telemetry t) { telemetry = t; }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        /*
      --Yellow HSV
        Scalar lowHSV = new Scalar(23, 50, 70);
       Scalar highHSV = new Scalar(32, 255, 255);

        Red HSV
        Scalar lowHSV = new Scalar(140, 50, 70);
        Scalar highHSV = new Scalar(180, 255, 255);

        --Green HSV
          Scalar lowHSV = new Scalar(36, 50, 70);
          Scalar highHSV = new Scalar(89, 255, 255);
        --Orange HSV
          Scalar lowHSV = new Scalar(10, 50, 70);
          Scalar highHSV = new Scalar(24, 255, 255);
         */
        //Blue
        Scalar lowHSV = new Scalar(90 , 50, 70);
        Scalar highHSV = new Scalar(128, 255, 255);
        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat left = mat.submat(LEFT_ROI);
        Mat middle = mat.submat(MIDDLE_ROI);
        Mat right = mat.submat(RIGHT_ROI);

        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
        double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area()/255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;

        left.release();
        middle.release();
        right.release();

        telemetry.addData("Left raw value", (int) Core.sumElems(left).val[0]);
        telemetry.addData("Middle raw value", (int) Core.sumElems(middle).val[0]);
        telemetry.addData("Right raw value", (int) Core.sumElems(right).val[0]);

        telemetry.addData("Left percentage", Math.round(leftValue * 100) + "%");
        telemetry.addData("Middle percentage", Math.round(middleValue * 100) + "%");
        telemetry.addData("Right percentage", Math.round(rightValue * 100) + "%");

        boolean stoneLeft = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean stoneMiddle = middleValue > PERCENT_COLOR_THRESHOLD;
        boolean stoneRight = rightValue > PERCENT_COLOR_THRESHOLD;

        if (stoneRight) {
            location = blueDetector.Location.RIGHT;
            telemetry.addData("Prop Location", "RIGHT");
        }
        else if (stoneLeft) {
            location = blueDetector.Location.LEFT;
            telemetry.addData("Prop Location", "LEFT");
        }
        else if(stoneMiddle){
            location = blueDetector.Location.MIDDLE;
            telemetry.addData("Prop Location", "MIDDLE");
        }
        else{
            location = blueDetector.Location.NOT_FOUND;
            telemetry.addData("Prop Location", "NOT FOUND");
        }
        telemetry.update();

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        Scalar color = new Scalar(255, 0, 0);
        Scalar colorObject = new Scalar(0, 255, 0);

        Imgproc.rectangle(mat, LEFT_ROI, location == blueDetector.Location.LEFT? colorObject:color);
        Imgproc.rectangle(mat, MIDDLE_ROI, location == blueDetector.Location.MIDDLE? colorObject:color);
        Imgproc.rectangle(mat, RIGHT_ROI, location == blueDetector.Location.RIGHT? colorObject:color);

        return mat;
    }

    public blueDetector.Location getLocation() {
        return location;
    }

}
