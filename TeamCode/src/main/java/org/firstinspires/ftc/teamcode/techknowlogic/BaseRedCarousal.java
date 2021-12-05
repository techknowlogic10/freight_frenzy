package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;

public abstract class BaseRedCarousal extends BaseAutonomous {

    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 38;
    public static double DRIVE_TO_HUB_STEP2_BACK = 3;

    public static double DRIVE_TO_CAROUSAL_STEP1_FORWARD = 25;
    public static double DRIVE_TO_CAROUSAL_STEP2_STRAFE_LEFT = 42;

    @Override
    protected boolean isCarousalSpinReversed() {
        return false;
    }

    @Override
    protected int getElevatorLevel(String shippingElementPosition) {
        if (shippingElementPosition.equals("LEFT")) {
            return 2;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 3;
        } else {
            return 1;
        }
    }

    @Override
    protected void driveToShippingHub(SampleMecanumDrive driveTrain) {

        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_HUB_STEP1_STRAFE_RIGHT)
                .build();

        driveTrain.followTrajectory(strafeRight);

        Trajectory back = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();

        driveTrain.followTrajectory(back);
    }

    @Override
    protected void driveToCarousal(SampleMecanumDrive driveTrain) {

        Trajectory forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_CAROUSAL_STEP1_FORWARD)
                .build();
        driveTrain.followTrajectory(forward);

        Trajectory step2StrafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(DRIVE_TO_CAROUSAL_STEP2_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(step2StrafeLeft);
    }

    @Override
    protected RobotPosition getRobotPosition() {
        return RobotPosition.RED_CAROUSAL;
    }
}
