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
                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 16.34)
                .followTrajectorySequence(drive ->
                        //blue 1
                        //left
                        drive.trajectorySequenceBuilder(new Pose2d(10,60,Math.toRadians(90)))
                        .turn(Math.toRadians(180))
                                .forward(25)
                        .turn(Math.toRadians(-90))

                        //drop pixel
                        .turn(Math.toRadians(-90))
                                .forward(25)
                        .turn(Math.toRadians(-90))
                                .forward(25)


                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}