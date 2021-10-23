package org.firstinspires.ftc.teamcode.drive.opmode;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.Elevator;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.TeamShippingElementDetector;

@Autonomous(name = "Red Left")
@Config
public class RedLeft extends LinearOpMode {

    public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));
    private int elevatorPostion;

    @Override
    public void runOpMode() throws InterruptedException {

        //Step-0 : Set the robot to starting position : (-30, -63) heading : 270

        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        driveTrain.setPoseEstimate(startingPosition);
        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry);

        waitForStart();
        //Step-1 : Scan for duck or Team Shipping Element
        String position = detector.detectShippingElement();

        telemetry.log().add("team shipping element position "+position);

        if(position.equals("LEFT")){
            elevatorPostion = 2;
        } else if(position.equals("RIGHT")){
            elevatorPostion = 3;
        } else {
            elevatorPostion = 1;
        }

        telemetry.log().add("elevator level "+elevatorPostion);



        //Step-2 : Drive to Team Shipping Hub (-13, -40) in a reversed direction : end tangent is 270

       /* Trajectory trajectoryToShippingHub = driveTrain.trajectoryBuilder(startingPosition, true)
                .splineTo(shippingHubVector, Math.toRadians(225))
                .build();
        driveTrain.followTrajectory(trajectoryToShippingHub);*/

        Trajectory strafeLeft = driveTrain.trajectoryBuilder(startingPosition, false)
                .strafeLeft(36)
                .build();
        driveTrain.followTrajectory(strafeLeft);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(strafeLeft.end(), false)
                .back(26)
                .build();

        //Step-3 : Drop the pre-loaded box in the appropriate level

        Elevator elevator = new Elevator();
        elevator.raiseToTheLevel(elevatorPostion, hardwareMap);

        //Step-4 Drive to carousal

        driveTrain.followTrajectory(pathToShippingHub);
        sleep(2000);
        Trajectory forwardPath = driveTrain.trajectoryBuilder(pathToShippingHub.end(), false)
                .forward(22)
                .build();
        driveTrain.followTrajectory(forwardPath);

        Trajectory strafeRight = driveTrain.trajectoryBuilder(forwardPath.end(),false)
                .strafeRight(72)
                .build();
        driveTrain.followTrajectory(strafeRight);
        sleep(2000);
        Trajectory reversePath = driveTrain.trajectoryBuilder(strafeRight.end(),false)
                .back(24)
                .build();
        driveTrain.followTrajectory(reversePath);



        //Step-4 : Drive to Carousal (-56, -57) with end tangent of 225
        //driveTrain.setPoseEstimate(trajectoryToShippingHub.end());
       /* Trajectory trajectoryToCarousal = driveTrain.trajectoryBuilder(trajectoryToShippingHub.end(), false)
                .forward(24).strafeRight(55)
                .build();
        driveTrain.followTrajectory(trajectoryToCarousal);
        sleep(2000);*/
        //Step-5 : Spin the Carousal

        //Step 6 : Drive to Storage Unit (-61, -36) with end tangent of 0
        /*Trajectory trajectoryToStorageUnit = driveTrain.trajectoryBuilder(trajectoryToCarousal.end(), false)
                .back(24)
                .build();
        driveTrain.followTrajectory(trajectoryToStorageUnit);*/
    }
}
