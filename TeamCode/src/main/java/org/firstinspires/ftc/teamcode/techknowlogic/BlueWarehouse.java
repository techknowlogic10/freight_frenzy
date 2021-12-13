package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Blue Warehouse")
@Config
public class BlueWarehouse extends BaseAutonomous {


    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 26;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_WAREHOUSE_STEP1_FORWARD = 25;
    public static double DRIVE_TO_WAREHOUSE_STEP2_STRAFE = 10;
    public static double DRIVE_TO_WAREHOUSE_STEP3_FORWARD = 75;

    private Pose2d endRobotPosition;

    @Override
    protected void dropAdditionalFreight() {

        //ADDITIONAL FREIGHT 1
        new Thread(raiseTo3rdLevelThread).start();
        driveToShippingHubToDrop1stAdditional(this.driveTrain);
        elevator.dropFreight();

        new Thread(elevatorDropThread).start();

        driveToWarehouseAndPick2ndAdditional(driveTrain);

        //Raise to 3rd level in the background
        new Thread(raiseTo3rdLevelThread).start();

        driveToShippingHubToDrop2ndAdditional(driveTrain);
        elevator.dropFreight();

        parkRobotForFinal(driveTrain);
    }

    @Override
    protected boolean isCarousalSpinReversed() {
        return false;
    }

    @Override
    protected void driveToCarousal(SampleMecanumDrive driveTrain) {
        throw new UnsupportedOperationException("Carousal is not in the picture for warehouse");
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
    protected RobotPosition getRobotPosition() {
        return RobotPosition.BLUE_WAREHOUSE;
    }

    @Override
    protected void driveToShippingHub(SampleMecanumDrive driveTrain) {
        driveTrain.setPoseEstimate(new Pose2d(12, 64, Math.toRadians(0)));
        TrajectorySequence pathToShippingHub = driveTrain.trajectorySequenceBuilder(new Pose2d(12, 64, Math.toRadians(0)))
                .back(3)
                .strafeRight(40
                )
                .back(3)
                .build();

        endRobotPosition = pathToShippingHub.end();
        servoIntakeArm.setPosition(0.0);

        driveTrain.followTrajectorySequence(pathToShippingHub);
    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {

        intake.setPower(-1);
        TrajectorySequence warehouse = driveTrain.trajectorySequenceBuilder(endRobotPosition)
                .strafeLeft(44)
                .turn(Math.toRadians(6))
                .forward(39)
                .build();

        driveTrain.followTrajectorySequence(warehouse);

        endRobotPosition = warehouse.end();

        Trajectory inchforward = driveTrain.trajectoryBuilder(endRobotPosition)
                .forward(2)
                .build();

        double distance = cargoInBayDS.getDistance(DistanceUnit.CM);

        //After parking, pick up the 1ST ADDITIONAL FREIGHT

        //if > 4, we did NOT pick up the element yet; if <= 4, we picked up the element
        while (distance > 4) {
            driveTrain.followTrajectory(inchforward);
            endRobotPosition = inchforward.end();

            //run primary intake
            intake.setPower(-0.7);

            distance = cargoInBayDS.getDistance(DistanceUnit.CM);
        }

        if (distance < 4) {
            intake.setPower(0.7);
        }
    }

    protected void driveToShippingHubToDrop1stAdditional(SampleMecanumDrive driveTrain) {
        TrajectorySequence backformore = driveTrain.trajectorySequenceBuilder(endRobotPosition)
                .turn(Math.toRadians(-12))
                .back(40)
                .splineToSplineHeading(new Pose2d(-3, 50, Math.toRadians(60)), Math.toRadians(-90))
                .build();

        servoIntakeArm.setPosition(0.8);
        driveTrain.followTrajectorySequence(backformore);
    }

    protected void driveToWarehouseAndPick2ndAdditional(SampleMecanumDrive driveTrain) {

        intake.setPower(-1);
        servoIntakeArm.setPosition(0);
        TrajectorySequence warehouse = driveTrain.trajectorySequenceBuilder(new Pose2d(-6, 48, Math.toRadians(60)))
                // .lineToLinearHeading(new Pose2d(-5, -62, Math.toRadians(0)))
                // .strafeRight(4)
                // .lineToSplineHeading(new Pose2d(3,-72, Math.toRadians(0)))
                //.turn(Math.toRadians(60))
                //.strafeTo(new Vector2d(6, -75))
                .splineToLinearHeading(new Pose2d(6,74, Math.toRadians(0)), Math.toRadians(180))
                .strafeLeft(2)
                .turn(Math.toRadians(12))
                .forward(37)
                .build();
        driveTrain.followTrajectorySequence(warehouse);

        Trajectory inchforward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate())
                .forward(4)
                .build();

        double distance = cargoInBayDS.getDistance(DistanceUnit.CM);

        while (distance > 5) {
            driveTrain.followTrajectory(inchforward);
            endRobotPosition = inchforward.end();

            //run primary intake
            intake.setPower(-1);

            distance = cargoInBayDS.getDistance(DistanceUnit.CM);
        }

        if (distance < 5) {
            intake.setPower(1);
        }
    }

    protected void parkRobotForFinal(SampleMecanumDrive driveTrain) {

        TrajectorySequence backforpark = driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                //    .lineToLinearHeading(new Pose2d(40, -50, Math.toRadians(0)))
                .turn(Math.toRadians(-40))
                .forward(60)
                .build();
        driveTrain.followTrajectorySequence(backforpark);
    }

    protected void driveToShippingHubToDrop2ndAdditional(SampleMecanumDrive driveTrain) {
        TrajectorySequence backforpark = driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
               // .strafeLeft(4)
                .turn(Math.toRadians(-20))
                .back(34)
                .splineToSplineHeading(new Pose2d(-9, 60, Math.toRadians(60)), Math.toRadians(-90))
                .build();
        servoIntakeArm.setPosition(0.8);
        driveTrain.followTrajectorySequence(backforpark);
    }
}
