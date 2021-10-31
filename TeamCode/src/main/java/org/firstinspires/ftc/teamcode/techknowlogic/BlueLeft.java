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

@Autonomous(name = "Blue Left")
@Config
public class BlueLeft extends LinearOpMode {

    //public static Pose2d startingPosition = new Pose2d(-30, -63, Math.toRadians(270));

    public static double DRIVE_TO_HUB_STEP1_STRAFE_LEFT = 29;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_CAROUSAL_STEP1_FORWARD = 16;
    public static double DRIVE_TO_CAROUSAL_STEP2_STRAFE_RIGHT = 80;
    public static double DRIVE_TO_CAROUSAL_STEP3_FORWARD = 5;
    public static double DRIVE_TO_STORAGE_UNIT_BACK = 25;

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

        //sleep(500);
        //elevator.dropToZero();

        //Step-4 Drive to carousal and spin
        driveToCarousal(driveTrain);
        carousalSpinner.spin(true);

        //Step 6 : Drive to Storage Unit
        driveToStorageUnit(driveTrain);
    }

    private void driveToStorageUnit(SampleMecanumDrive driveTrain) {
        Trajectory reversePath = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_STORAGE_UNIT_BACK)
                .build();
        driveTrain.followTrajectory(reversePath);
    }


    private int getElevatorLevel(String shippingElementPosition) {
        if (shippingElementPosition.equals("LEFT")) {
            return 3;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 2;
        } else {
            return 1;
        }
    }

    private void driveToShippingHub(SampleMecanumDrive driveTrain) {

//        Trajectory trajectoryToShippingHub = driveTrain.trajectoryBuilder(startingPosition, true)
//                .splineTo(shippingHubVector, Math.toRadians(225))
//                .build();
//        driveTrain.followTrajectory(trajectoryToShippingHub);

        Trajectory strafeRight = driveTrain.trajectoryBuilder(new Pose2d(), false)
                .strafeRight(DRIVE_TO_HUB_STEP1_STRAFE_LEFT)
                .build();
        driveTrain.followTrajectory(strafeRight);

        Trajectory pathToShippingHub = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(DRIVE_TO_HUB_STEP2_BACK)
                .build();

        driveTrain.followTrajectory(pathToShippingHub);
    }

    private void driveToCarousal(SampleMecanumDrive driveTrain) {

        Trajectory forwardPath = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_CAROUSAL_STEP1_FORWARD)
                .build();
        driveTrain.followTrajectory(forwardPath);

        Trajectory strafeLeft = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(DRIVE_TO_CAROUSAL_STEP2_STRAFE_RIGHT)
                .build();

        driveTrain.followTrajectory(strafeLeft);
        Trajectory straight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(DRIVE_TO_CAROUSAL_STEP3_FORWARD)
                .build();

        driveTrain.followTrajectory(straight);
    }

}
