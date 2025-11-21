package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static double Robot_X = 17.5;
    public static double Robot_Y = 16;
    private static final Pose2d START_POSE = new Pose2d(0, -72 + Robot_Y/2, Math.PI/2);

    private static final Vector2d BP1 = new Vector2d(-36 + Robot_X/2, -36);

    private static final Vector2d BP2 = new Vector2d(-36 + Robot_X/2, -12);

    private static final Vector2d BP3 = new Vector2d(- 36 + Robot_X/2, 12);

    private static final Vector2d G1 = new Vector2d(0, -60 + Robot_Y/2);

    private static final Vector2d G2 = new Vector2d(0, 0);
    private static final Vector2d G3 = new Vector2d(-12 -Robot_X/2,12+Robot_Y/2);
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -72, Math.PI/2))
                .splineTo(new Vector2d(0,0),Math.PI/2)
                        .splineTo(BP1,Math.PI)
                        .splineTo(BP2,Math.PI)
                        .splineTo(BP3,Math.PI)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}