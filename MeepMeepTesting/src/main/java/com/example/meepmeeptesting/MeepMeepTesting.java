package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

//    public static double Robot_X = 17.5;
//    public static double Robot_Y = 16;
//    private static final Pose2d START_POSE = new Pose2d(0, -72 + Robot_Y/2, Math.PI/2);
//
//    private static final Vector2d BP1 = new Vector2d(-36 + Robot_X/2, -36);
//
//    private static final Vector2d BP2 = new Vector2d(-36 + Robot_X/2, -12);
//
//    private static final Vector2d BP3 = new Vector2d(- 36 + Robot_X/2, 12);
//
//    private static final Vector2d G1 = new Vector2d(0, -60 + Robot_Y/2);
//
//    private static final Vector2d G2 = new Vector2d(0, 0);
//    private static final Vector2d G3 = new Vector2d(-12 -Robot_X/2,12+Robot_Y/2);
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        Action path = new SequentialAction(
                myBot.getDrive().actionBuilder(START_POSE)
                        .strafeTo(G2)
                        .build(),
                myBot.getDrive().actionBuilder(myBot.getPose())
                                .lineToX(G2.x)
                                        .build(),
//                new SleepAction(0.5),
                myBot.getDrive().actionBuilder(myBot.getPose())
                        .strafeTo(new Vector2d(myBot.getPose().position.x,BP1.y))
                        .strafeTo(BP1)
                        .turn(Math.PI/2)
                        .build(),
//                new SleepAction(0.5),
                myBot.getDrive().actionBuilder(myBot.getPose())
                        .strafeTo(BP2)
                        .build(),
//                new SleepAction(0.5),
                myBot.getDrive().actionBuilder(myBot.getPose())
                        .strafeTo(BP3)
                        .build(),
                new SleepAction(0.5)
        );

        myBot.runAction(path);

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}