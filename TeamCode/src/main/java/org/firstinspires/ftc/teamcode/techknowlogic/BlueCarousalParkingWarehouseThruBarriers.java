package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
public class BlueCarousalParkingWarehouseThruBarriers extends BaseBlueCarousal {

        /*
     1. strafe right
     2. turn -90
     4. forward
     */

    public static double PARK_ROBOT_STEP1_STRAFE_RIGHT = 50;
    public static double PARK_ROBOT_STEP2_FORWARD = 90;

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {
        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(PARK_ROBOT_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeRight);

        driveTrain.turn(Math.toRadians(-90));

        Trajectory forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(PARK_ROBOT_STEP2_FORWARD)
                .build();
        driveTrain.followTrajectory(forward);
    }
}
