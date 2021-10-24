package org.firstinspires.ftc.teamcode.drive.opmode;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.TeamShippingElementDetector;

@Autonomous(name = "Red Right")
@Config
public class RedRight extends LinearOpMode {

    public static Pose2d startingPosition = new Pose2d(12, -62, Math.toRadians(270));

    private int elevatorPostion;
    public static double ANGLE = 90;
    @Override
    public void runOpMode() throws InterruptedException {


        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        driveTrain.setPoseEstimate(startingPosition);

        waitForStart();



        Trajectory strafeRight = driveTrain.trajectoryBuilder(startingPosition, false)
                .strafeRight(27)
                .build();
        driveTrain.followTrajectory(strafeRight);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(strafeRight.end(), false)
                .back(26)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);


        //Step-1 : Scan for duck or Team Shipping Element
        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry);
        String position = detector.detectShippingElement();

        telemetry.log().add("team shipping element position "+position);

        if(position.equals("LEFT")){
            elevatorPostion = 1;
        } else if(position.equals("RIGHT")){
            elevatorPostion = 2;
        } else {
            elevatorPostion = 3;
        }

        telemetry.log().add("elevator level "+elevatorPostion);
        sleep(2000);
        Trajectory forwardPath = driveTrain.trajectoryBuilder(pathToShippingHub.end(), false)
                .forward(24)
                .build();

        driveTrain.followTrajectory(forwardPath);
        driveTrain.turn(Math.toRadians(ANGLE));
        Trajectory strafeLeft = driveTrain.trajectoryBuilder(forwardPath.end(),false)
                .strafeLeft(55)
                .build();
        driveTrain.followTrajectory(strafeLeft);


    }
}
