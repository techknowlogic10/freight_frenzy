package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.CarousalSpinner;
import org.firstinspires.ftc.teamcode.techknowlogic.util.Elevator;
import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

@Autonomous
@Config

public class BlueCarousalParkingAtWarehouse extends LinearOpMode {

    public static double STEP1 = 2;
    public static double STRAFE_LEFT = 54;

    public static double STEP2 = 59;
    public static double STEP3 = 33;

    public static double STEP4 = 35;
    public static double STEP5 = 10;
    public static double STEP6 = 90;

    @Override
    public void runOpMode() throws InterruptedException {

        //Step-0 : Set the robot to starting position

        SampleMecanumDrive driveTrain = new SampleMecanumDrive(hardwareMap);
        //driveTrain.setPoseEstimate(startingPosition);

        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry);
        CarousalSpinner carousalSpinner = new CarousalSpinner(hardwareMap);
        Elevator elevator = new Elevator(hardwareMap);

        //Detection continue to happen throughout init
        detector.startDetection();

        waitForStart();

        //As detection continue to happen since init, we can stop detection (stop streaming)
        detector.stopDetection();

        //Step-1 : Scan for duck or Team Shipping Element
        String shippingElementPosition = detector.getElementPosition();
        telemetry.log().add("team shipping element position " + shippingElementPosition);

        int elevatorLevel = getElevatorLevel(shippingElementPosition);
        telemetry.log().add("elevator level " + elevatorLevel);

        //Step-2 : Drive to Team Shipping Hub
        driveToShippingHub(driveTrain);

        //Step-3 : Drop the pre-loaded box in the appropriate level
        elevator.raiseToTheLevel(elevatorLevel);
        sleep(1000);
        elevator.dropFreight();

        Runnable elevatorDownThread = new Runnable() {
            @Override
            public void run() {
                elevator.dropToZero();
            }
        };
        new Thread(elevatorDownThread).start();

        //Step-4 Drive to carousal and spin
        driveToCarousal(driveTrain);
        carousalSpinner.spin(true);

        driveToWarehouse(driveTrain);
    }

    private void driveToWarehouse(SampleMecanumDrive driveTrain) {
        Trajectory path1 = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(STEP4)
                .build();
        driveTrain.followTrajectory(path1);

        Trajectory path2 = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(STEP5)
                .build();
        driveTrain.followTrajectory(path2);

        Trajectory path3 = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(STEP6)
                .build();
        driveTrain.followTrajectory(path3);
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

        driveTrain.turn(Math.toRadians(90));

        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeLeft(STRAFE_LEFT)
                .build();

        driveTrain.followTrajectory(strafeRight);

        Trajectory step1 = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .back(STEP1)
                .build();

        driveTrain.followTrajectory(step1);
    }

    private void driveToCarousal(SampleMecanumDrive driveTrain) {

        Trajectory strafeRight = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .strafeRight(STEP2)
                .build();

        driveTrain.followTrajectory(strafeRight);

        Trajectory step1 = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate(), false)
                .forward(STEP3)
                .build();

        driveTrain.followTrajectory(step1);
    }

}
