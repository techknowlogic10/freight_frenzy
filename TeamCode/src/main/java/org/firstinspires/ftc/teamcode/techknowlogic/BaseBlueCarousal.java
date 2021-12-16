package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;

public abstract class BaseBlueCarousal extends BaseAutonomous {

    public static double DRIVE_TO_HUB_STEP1_STRAFE_LEFT = 40;
    public static double DRIVE_TO_HUB_STEP2_BACK = 3;

    public static double DRIVE_TO_CAROUSAL_STEP1_FORWARD = 26;
    public static double DRIVE_TO_CAROUSAL_STEP2_STRAFE_RIGHT = 38;
    public static double DRIVE_TO_CAROUSAL_STEP3_FORWARD = 2;

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

        Trajectory forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_CAROUSAL_STEP1_FORWARD)
                .build();
        driveTrain.followTrajectory(forward);

        Trajectory step2StrafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_CAROUSAL_STEP2_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(step2StrafeRight);

//        Trajectory step3forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
//                .forward(2)
//                .build();
//        driveTrain.followTrajectory(step3forward);
    }

    @Override
    protected RobotPosition getRobotPosition() {
        return RobotPosition.BLUE_CAROUSAL;
    }
}
