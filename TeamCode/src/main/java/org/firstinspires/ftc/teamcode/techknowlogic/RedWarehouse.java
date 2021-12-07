package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Red Warehouse")
@Config
public class RedWarehouse extends BaseAutonomous {

    public static double DRIVE_TO_HUB_STEP1_STRAFE_RIGHT = 26;
    public static double DRIVE_TO_HUB_STEP2_BACK = 25;

    public static double DRIVE_TO_WAREHOUSE_STEP1_FORWARD = 25;
    public static double DRIVE_TO_WAREHOUSE_STEP2_STRAFE = 10;
    public static double DRIVE_TO_WAREHOUSE_STEP3_FORWARD = 75;

    private Pose2d endPositionAtShippingHub;

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
            return 1;
        } else if (shippingElementPosition.equals("RIGHT")) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    protected RobotPosition getRobotPosition() {
        return RobotPosition.RED_WAREHOUSE;
    }

    @Override
    protected void driveToShippingHub(SampleMecanumDrive driveTrain) {
        driveTrain.setPoseEstimate(new Pose2d(12, -64, Math.toRadians(0)));
        TrajectorySequence pathToShippingHub = driveTrain.trajectorySequenceBuilder(new Pose2d(12, -64, Math.toRadians(0)))
                .strafeLeft(42
                )
                .back(3)
                .build();

        endPositionAtShippingHub = pathToShippingHub.end();
        servoIntakeArm.setPosition(0.0);

        driveTrain.followTrajectorySequence(pathToShippingHub);
    }

    @Override
    protected void parkRobot(SampleMecanumDrive driveTrain) {

        intake.setPower(-1);
        TrajectorySequence warehouse = driveTrain.trajectorySequenceBuilder(endPositionAtShippingHub)
                // .lineToLinearHeading(new Pose2d(-5, -62, Math.toRadians(0)))
                //  .UNSTABLE_addTemporalMarkerOffset(2, () -> {})
                .strafeRight(41)
                .turn(Math.toRadians(-3))
                .forward(40)
                .build();

        driveTrain.followTrajectorySequence(warehouse);

        endPositionAtShippingHub = warehouse.end();

        Trajectory inchforward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate())
                .forward(4)
                .build();

        Trajectory inchback = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate())
                .back(2)
                .build();

        double distance = cargoInBayDS.getDistance(DistanceUnit.CM);

        //After parking, pick up the 1ST ADDITIONAL FREIGHT

        int n = 0;
        while (distance > 4) {
            driveTrain.followTrajectory(inchforward);
            intake.setPower(-1);
            distance = cargoInBayDS.getDistance(DistanceUnit.CM);
        }

        if (distance < 4) {
            intake.setPower(1);
        }
    }

    protected void driveToShippingHubToDrop1stAdditional(SampleMecanumDrive driveTrain) {
        TrajectorySequence backformore = driveTrain.trajectorySequenceBuilder(endPositionAtShippingHub)
                .turn(Math.toRadians(12))
                .back(40)
                .splineToSplineHeading(new Pose2d(3, -42, Math.toRadians(300)), Math.toRadians(90))
                .build();

        servoIntakeArm.setPosition(0.8);
        driveTrain.followTrajectorySequence(backformore);
    }

    protected void driveToWarehouseAndPick2ndAdditional(SampleMecanumDrive driveTrain) {

        intake.setPower(-1);
        servoIntakeArm.setPosition(0);
        TrajectorySequence warehouse = driveTrain.trajectorySequenceBuilder(new Pose2d(-6, -48, Math.toRadians(300)))
                // .lineToLinearHeading(new Pose2d(-5, -62, Math.toRadians(0)))
                // .strafeRight(4)
                // .lineToSplineHeading(new Pose2d(3,-72, Math.toRadians(0)))
                //.turn(Math.toRadians(60))
                //.strafeTo(new Vector2d(6, -75))
                .splineToSplineHeading(new Pose2d(20,-65, Math.toRadians(-6)), Math.toRadians(0))
                .forward(36)
                .build();
        driveTrain.followTrajectorySequence(warehouse);

        Trajectory inchforward = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate())
                .forward(4)
                .build();

        Trajectory inchback = driveTrain.trajectoryBuilder(driveTrain.getPoseEstimate())
                .back(2)
                .build();

        double distance = cargoInBayDS.getDistance(DistanceUnit.CM);

        int n = 0;
        while (distance > 5) {
            driveTrain.followTrajectory(inchforward);
            intake.setPower(-1);
            distance = cargoInBayDS.getDistance(DistanceUnit.CM);
            ;
        }
        if (distance < 5) {
            intake.setPower(1);
        }
    }

    protected void parkRobotForFinal(SampleMecanumDrive driveTrain) {

        TrajectorySequence backforpark = driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                //    .lineToLinearHeading(new Pose2d(40, -50, Math.toRadians(0)))
                .turn(Math.toRadians(50))
                .forward(60)
                .build();
        driveTrain.followTrajectorySequence(backforpark);
    }

    protected void driveToShippingHubToDrop2ndAdditional(SampleMecanumDrive driveTrain) {
        TrajectorySequence backforpark = driveTrain.trajectorySequenceBuilder(driveTrain.getPoseEstimate())
                .turn(Math.toRadians(10))
                .back(34)
                .splineToSplineHeading(new Pose2d(3, -46, Math.toRadians(300)), Math.toRadians(90))
                .build();
        servoIntakeArm.setPosition(0.8);
        driveTrain.followTrajectorySequence(backforpark);
    }
}
