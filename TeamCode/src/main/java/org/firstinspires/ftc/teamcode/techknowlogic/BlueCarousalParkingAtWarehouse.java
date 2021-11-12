package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
public class BlueCarousalParkingAtWarehouse extends BaseBlueCarousal {

    /*
     1. strafe right
     2. turn -90
     3. strafe left
     4. forward
     */

    public static double PARK_ROBOT_STEP1_STRAFE_RIGHT = 50;
    public static double PARK_ROBOT_STEP2_STRAFE_LEFT = 20;
    public static double PARK_ROBOT_STEP3_FORWARD = 95;

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {
        Trajectory step1StrafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(PARK_ROBOT_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(step1StrafeRight);

        driveTrain.turn(Math.toRadians(-90));

        Trajectory step2StrafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(PARK_ROBOT_STEP2_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(step2StrafeLeft);

        Trajectory step3Forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(PARK_ROBOT_STEP3_FORWARD)
                .build();
        driveTrain.followTrajectory(step3Forward);
    }
}
