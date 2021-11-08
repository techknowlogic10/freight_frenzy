package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.CarousalSpinner;
import org.firstinspires.ftc.teamcode.techknowlogic.util.Elevator;
import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

@Autonomous(name = "Blue Warehouse")
@Config
public class BlueWarehouse extends BaseAutonomous {

    //public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));

    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 26;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_WAREHOUSE_STEP1_FORWARD = 25;
    public static double DRIVE_TO_WAREHOUSE_STEP2_STRAFE = 10;
    public static double DRIVE_TO_WAREHOUSE_STEP3_FORWARD = 68;

    @Override
    protected void driveToCarousal(SampleMecanumDrive driveTrain) {
        throw new UnsupportedOperationException("Carousal is not in the picture for warehouse side");
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
        Trajectory strafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(DRIVE_TO_HUB_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);
    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {

        Trajectory forwardPath = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_WAREHOUSE_STEP1_FORWARD)
                .build();
        driveTrain.followTrajectory(forwardPath);

        driveTrain.turn(Math.toRadians(90));

        Trajectory pathStrafe = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_WAREHOUSE_STEP2_STRAFE)
                .build();

        driveTrain.followTrajectory(pathStrafe);

        Trajectory pathForward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_WAREHOUSE_STEP3_FORWARD)
                .build();

        driveTrain.followTrajectory(pathForward);
    }
}
