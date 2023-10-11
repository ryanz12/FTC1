package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode {

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    int MOTOR_TICKS_COUNT = (int) leftFrontMotor.getMotorType().getTicksPerRev();
/*gg*/
    public void runOpMode() {
        leftFrontMotor = hardwareMap.get(DcMotor.class, "LeftFrontMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "RightFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "LeftBackMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "RightBackMotor");


        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int position = leftFrontMotor.getCurrentPosition();
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        initialize camera and pipeline
        colorOpenCV cv = new colorOpenCV(this);
//      call the function to startStreaming
        cv.observeStick();
        waitForStart();
        while (opModeIsActive()) {
            double circumference = 3.14*2.938;
            double rotationsNeeded = 18/circumference;
            int encoderDrivingTarget = (int)(rotationsNeeded*1200);
            leftFrontMotor.setTargetPosition(encoderDrivingTarget);
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
//        stopStreaming
        cv.stopCamera();
    }

}
