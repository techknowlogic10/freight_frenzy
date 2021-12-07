package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
public class BlueCarousalParkingWarehouseThruBarriers extends BaseBlueCarousal {

    public static double PARK_ROBOT_STEP1_BACK = 50;
    public static double PARK_ROBOT_STEP2_STRAFE_LEFT = 22;
    public static double PARK_ROBOT_STEP3_BACK = 50;

    public static double TURN_ANGLE = 180;

    @Override
    protected void dropAdditionalFreight() {

    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {

        Trajectory step1Back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(PARK_ROBOT_STEP1_BACK)
                .build();
        driveTrain.followTrajectory(step1Back);

        Trajectory strafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(PARK_ROBOT_STEP2_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory step3Back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(PARK_ROBOT_STEP3_BACK)
                .build();
        driveTrain.followTrajectory(step3Back);

        driveTrain.turn(Math.toRadians(TURN_ANGLE));
    }
}
