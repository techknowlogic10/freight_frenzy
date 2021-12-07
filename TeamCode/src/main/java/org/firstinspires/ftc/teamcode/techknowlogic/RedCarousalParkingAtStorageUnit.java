package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
@Config
public class RedCarousalParkingAtStorageUnit extends BaseRedCarousal {

    public static double PARK_ROBOT_STEP1_STRAFE_RIGHT = 25;

    @Override
    protected void dropAdditionalFreight() {

    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {
        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(PARK_ROBOT_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeRight);
    }
}
