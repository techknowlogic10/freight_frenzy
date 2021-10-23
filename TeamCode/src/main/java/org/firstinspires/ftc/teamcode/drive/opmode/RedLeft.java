package org.firstinspires.ftc.teamcode.drive.opmode;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.CarousalSpinner;
import org.firstinspires.ftc.teamcode.drive.Elevator;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.TeamShippingElementDetector;

@Autonomous(name = "Red Left")
@Config
public class RedLeft extends LinearOpMode {

    public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));

    @Override
    public void runOpMode() throws InterruptedException {

        //Step-0 : Set the robot to starting position

        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        driveTrain.setPoseEstimate(startingPosition);

        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry);
        CarousalSpinner carousalSpinner = new CarousalSpinner(hardwareMap);
        Elevator elevator = new Elevator(hardwareMap);

        waitForStart();

        //Step-1 : Scan for duck or Team Shipping Element
        String shippingElementPosition = detector.detectShippingElement();
        telemetry.log().add("team shipping element position " + shippingElementPosition);

        int elevatorLevel = getElevatorLevel(shippingElementPosition);
        telemetry.log().add("elevator level " + elevatorLevel);

        //Step-2 : Drive to Team Shipping Hub
        driveToShippingHub(driveTrain);

        //Step-3 : Drop the pre-loaded box in the appropriate level
        elevator.raiseToTheLevel(elevatorLevel);

        //Step-4 Drive to carousal and spin
        driveToCarousal(driveTrain);
        carousalSpinner.spin();

        //Step 6 : Drive to Storage Unit
        driveToStorageUnit(driveTrain);
    }

    private void driveToStorageUnit(SampleMecanumDrive driveTrain) {
        Trajectory reversePath = driveTrain.trajectoryBuilder(new Pose2d(), false)
                .back(24)
                .build();
        driveTrain.followTrajectory(reversePath);
    }


    private int getElevatorLevel(String shippingElementPosition) {
        if (shippingElementPosition.equals("LEFT")) {
            return 2;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 3;
        } else {
            return 1;
        }
    }

    private void driveToShippingHub(SampleMecanumDrive driveTrain) {

//        Trajectory trajectoryToShippingHub = driveTrain.trajectoryBuilder(startingPosition, true)
//                .splineTo(shippingHubVector, Math.toRadians(225))
//                .build();
//        driveTrain.followTrajectory(trajectoryToShippingHub);

        Trajectory strafeLeft = driveTrain.trajectoryBuilder(startingPosition, false)
                .strafeLeft(36)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(strafeLeft.end(), false)
                .back(30)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);
    }

    private void driveToCarousal(SampleMecanumDrive driveTrain) {

        Trajectory forwardPath = driveTrain.trajectoryBuilder(new Pose2d(), false)
                .forward(22)
                .build();
        driveTrain.followTrajectory(forwardPath);

        Trajectory strafeRight = driveTrain.trajectoryBuilder(new Pose2d(), false)
                .strafeRight(72)
                .build();
        driveTrain.followTrajectory(strafeRight);
    }

}
