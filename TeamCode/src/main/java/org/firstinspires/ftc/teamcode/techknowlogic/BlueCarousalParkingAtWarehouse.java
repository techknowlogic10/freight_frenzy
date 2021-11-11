package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
public class BlueCarousalParkingAtWarehouse extends BaseAutonomous {

    public static double DRIVE_TO_HUB_STEP1_STRAFE_LEFT = 54;
    public static double DRIVE_TO_HUB_STEP2_BACK = 5;

    public static double DRIVE_TO_CAROUSAL_STEP1_STRAFE_RIGHT = 59;
    public static double DRIVE_TO_CAROUSAL_STEP2_FORWARD = 33;

    public static double PARK_ROBOT_STEP1_BACK = 35;
    public static double PARK_ROBOT_STEP2_STRAFE_RIGHT = 10;
    public static double PARK_ROBOT_STEP3_BACK = 90;

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {
        Trajectory step1Back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(PARK_ROBOT_STEP1_BACK)
                .build();
        driveTrain.followTrajectory(step1Back);

        Trajectory step2StrafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(PARK_ROBOT_STEP2_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(step2StrafeRight);

        Trajectory step3Back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(PARK_ROBOT_STEP3_BACK)
                .build();
        driveTrain.followTrajectory(step3Back);
    }

    @Override
    protected int getElevatorLevel(String shippingElementPosition) {
        if (shippingElementPosition.equals("LEFT")) {
            return 1;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    protected void driveToShippingHub(SampleMecanumDrive driveTrain) {

        driveTrain.turn(Math.toRadians(90));

        Trajectory strafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(DRIVE_TO_HUB_STEP1_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();
        driveTrain.followTrajectory(back);
    }

    @Override
    protected boolean isCarousalSpinReversed() {
        return true;
    }

    @Override
    protected void driveToCarousal(SampleMecanumDrive driveTrain) {

        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_CAROUSAL_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeRight);

        Trajectory forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_CAROUSAL_STEP2_FORWARD)
                .build();
        driveTrain.followTrajectory(forward);
    }
}
