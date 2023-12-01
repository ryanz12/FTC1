package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MyClass {
    public static void main(String[] args){
        MeepMeep meepMeep = new MeepMeep(700);
        //sean
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 16.92)
                .followTrajectorySequence(drive ->
                        //red
                        //left
                        drive.trajectorySequenceBuilder(new Pose2d(12, -60,Math.toRadians(270)))
                                .back(5)
                                .waitSeconds(1)
                                .turn(Math.toRadians(180))
                                .waitSeconds(1)
                                .forward(20)
                                .waitSeconds(1)
                                .turn(Math.toRadians(-90))
                                .waitSeconds(1)
                                .strafeLeft(4)
                                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {
                                })
                                .waitSeconds(1)
                                .forward(33)
                                .waitSeconds(1)
                                .strafeRight(4)
                                .turn(Math.toRadians(180))
                                .UNSTABLE_addDisplacementMarkerOffset(0, () -> {

                                })
                                .waitSeconds(1)
                                .strafeLeft(24)
                                .waitSeconds(1)
                                .back(14)
                                .build()

                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}