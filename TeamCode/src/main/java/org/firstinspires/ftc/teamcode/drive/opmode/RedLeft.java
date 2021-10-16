package org.firstinspires.ftc.teamcode.drive.opmode;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Red Left")
@Config
public class RedLeft extends LinearOpMode {

    public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));

    public static Vector2d shippingHubVector = new Vector2d(-13, -40);
    public static Pose2d shippingHubPose = new Pose2d(shippingHubVector, Math.toRadians(270));

    public static Vector2d carousalVector = new Vector2d(-56, -57);
    public static Pose2d carousalPose = new Pose2d(carousalVector, Math.toRadians(225));

    @Override
    public void runOpMode() throws InterruptedException {

        //Step-0 : Set the robot to starting position : (-30, -63) heading : 270

        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        driveTrain.setPoseEstimate(startingPosition);

        waitForStart();

        //Step-1 : Scan for duck or Team Shipping Element

        //Step-2 : Drive to Team Shipping Hub (-13, -40) in a reversed direction : end tangent is 270

        Trajectory trajectoryToShippingHub = driveTrain.trajectoryBuilder(startingPosition, true)
                .splineTo(shippingHubVector, Math.toRadians(270))
                .build();
        driveTrain.followTrajectory(trajectoryToShippingHub);

        //Step-3 : Drop the pre-loaded box in the appropriate level

        //Step-4 : Drive to Carousal (-56, -57) with end tangent of 225
        Trajectory trajectoryToCarousal = driveTrain.trajectoryBuilder(shippingHubPose, false)
                .splineTo(carousalVector, Math.toRadians(225))
                .build();
        driveTrain.followTrajectory(trajectoryToCarousal);

        //Step-5 : Spin the Carousal

        //Step 6 : Drive to Storage Unit (-61, -36) with end tangent of 0
        Trajectory trajectoryToStorageUnit = driveTrain.trajectoryBuilder(carousalPose, false)
                .strafeRight(24)
                .build();
        driveTrain.followTrajectory(trajectoryToStorageUnit);
    }
}
