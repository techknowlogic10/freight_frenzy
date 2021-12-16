package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(preselectTeleOp = "DriverOperatorBlue")
@Config
public class BlueCarousalParkingAtStorageUnit extends BaseBlueCarousal {

    public static double PARK_ROBOT_STEP1_STRAFE_LEFT = 27;
    public static double PARK_ROBOT_STEP2_FORWARD = 4;

    @Override
    protected void dropAdditionalFreight() {

    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {
        Trajectory strafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(PARK_ROBOT_STEP1_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory forward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(PARK_ROBOT_STEP2_FORWARD)
                .build();
        driveTrain.followTrajectory(forward);
    }
}
