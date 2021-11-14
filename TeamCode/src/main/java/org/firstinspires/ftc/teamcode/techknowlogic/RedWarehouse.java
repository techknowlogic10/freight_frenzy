package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "Red Warehouse")
@Config
public class RedWarehouse extends BaseAutonomous {

    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 26;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_WAREHOUSE_STEP1_FORWARD = 25;
    public static double DRIVE_TO_WAREHOUSE_STEP2_STRAFE = 10;
    public static double DRIVE_TO_WAREHOUSE_STEP3_FORWARD = 75;

    @Override
    protected boolean isCarousalSpinReversed() {
        return false;
    }

    @Override
    protected void driveToCarousal(SampleMecanumDrive driveTrain) {
        throw new UnsupportedOperationException("Carousal is not in the picture for warehouse");
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
        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_HUB_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeRight);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);
    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {

        Trajectory step1Forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_WAREHOUSE_STEP1_FORWARD)
                .build();
        driveTrain.followTrajectory(step1Forward);

        driveTrain.turn(Math.toRadians(90));

        Trajectory step2StrafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_WAREHOUSE_STEP2_STRAFE)
                .build();
        driveTrain.followTrajectory(step2StrafeRight);

        Trajectory step3Forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_WAREHOUSE_STEP3_FORWARD)
                .build();
        driveTrain.followTrajectory(step3Forward);
    }
}
