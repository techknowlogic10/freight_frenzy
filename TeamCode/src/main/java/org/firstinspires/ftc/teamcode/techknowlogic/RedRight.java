package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.techknowlogic.util.CarousalSpinner;
import org.firstinspires.ftc.teamcode.techknowlogic.util.Elevator;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

@Autonomous(name = "Red Right")
@Config
public class RedRight extends LinearOpMode {

    public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));

    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 26;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_WAREHOUSE_STEP1_FORWARD = 25;
    public static double DRIVE_TO_WAREHOUSE_STEP2_STRAFE = 10;
    public static double DRIVE_TO_WAREHOUSE_STEP3_FORWARD = 75;




    @Override
    public void runOpMode() throws InterruptedException {

        //Step-0 : Set the robot to starting position

        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        //driveTrain.setPoseEstimate(startingPosition);

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
        elevator.dropFreight();

        //Step-4 Drive to warehouse
        driveToWarehouse(driveTrain);

    }



    private int getElevatorLevel(String shippingElementPosition) {
        if (shippingElementPosition.equals("LEFT")) {
            return 1;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 2;
        } else {
            return 3;
        }
    }

    private void driveToShippingHub(SampleMecanumDrive driveTrain) {

//        Trajectory trajectoryToShippingHub = driveTrain.trajectoryBuilder(startingPosition, true)
//                .splineTo(shippingHubVector, Math.toRadians(225))
//                .build();
//        driveTrain.followTrajectory(trajectoryToShippingHub);

        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(DRIVE_TO_HUB_STEP1_STRAFE_RIGHT)
                .build();
        driveTrain.followTrajectory(strafeRight);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);
    }

    private void driveToWarehouse(SampleMecanumDrive driveTrain) {

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
                .forward(DRIVE_TO_WAREHOUSE_STEP3_FORWARD)
                .build();

        driveTrain.followTrajectory(pathForward);

    }

}
