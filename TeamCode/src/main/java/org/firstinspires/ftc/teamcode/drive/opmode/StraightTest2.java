package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
public class StraightTest2 extends LinearOpMode {
    public static double DISTANCE = 50; // in
    public static double STARTING_X = 0;
    public static double STARTING_Y = 0;
    public static double STARTING_HEADING = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        OurMecanumDrive drive = new OurMecanumDrive(hardwareMap);

        Pose2d startingPosition = new Pose2d(STARTING_X, STARTING_Y, STARTING_HEADING);

        drive.setPoseEstimate(startingPosition);

        Trajectory trajectory = drive.trajectoryBuilder(startingPosition)
                .forward(DISTANCE)
                .build();

        waitForStart();

        // if (isStopRequested()) return;

        drive.followTrajectory(trajectory);

        Pose2d poseEstimate = drive.getPoseEstimate();

        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        // while (!isStopRequested() && opModeIsActive()) ;
    }
}
