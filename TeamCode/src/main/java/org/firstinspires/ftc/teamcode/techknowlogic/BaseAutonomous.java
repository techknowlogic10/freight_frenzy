package org.firstinspires.ftc.teamcode.techknowlogic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.CarousalSpinner;
import org.firstinspires.ftc.teamcode.techknowlogic.util.Elevator;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;
import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

public abstract class BaseAutonomous extends LinearOpMode {

    protected Servo servoIntakeArm;
    protected DcMotorEx intake;
    protected DistanceSensor cargoInBayDS;
    protected Elevator elevator;
    protected SampleMecanumDrive driveTrain;
    protected Runnable raiseToLevel;

    private double INTAKE_ARM_HOME = 1.0;

    protected Runnable elevatorDropThread = new Runnable() {
        @Override
        public void run() {
            elevator.dropCarriageArmToHome();
            sleep(1000);
            elevator.dropELevatorToZero();
        }
    };

    protected Runnable raiseTo3rdLevelThread = new Runnable() {
        @Override
        public void run() {
            sleep(1000);
            elevator.raiseToTheLevel(3);
        }
    };

    @Override
    public void runOpMode() throws InterruptedException {

        this.servoIntakeArm = hardwareMap.servo.get("IntakeServo");
        this.intake = hardwareMap.get(DcMotorEx.class, "intake");
        this.cargoInBayDS = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

        servoIntakeArm.setPosition(INTAKE_ARM_HOME);

        //Step-0 : Set the robot to starting position
        this.driveTrain = new SampleMecanumDrive(hardwareMap);
        //driveTrain.setPoseEstimate(startingPosition);

        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry, getRobotPosition(), false);
        CarousalSpinner carousalSpinner = new CarousalSpinner(hardwareMap);
        this.elevator = new Elevator(hardwareMap);

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

        raiseToLevel = new Runnable() {
            @Override
            public void run() {
                elevator.raiseToTheLevel(elevatorLevel);
            }
        };
        //Step-2 : Drive to Team Shipping Hub
        new Thread(raiseToLevel).start();
        driveToShippingHub(driveTrain);

        //Step-3 : Drop the pre-loaded box in the appropriate level



        elevator.dropFreight();

        //while driving to carousal, bring the elevator down to zero in the background (in a separate thread)
        new Thread(elevatorDropThread).start();

        //OPTIONAL -- Step-4 Drive to carousal and spin
        try {
            driveToCarousal(driveTrain);
            carousalSpinner.spin(isCarousalSpinReversed());

        } catch (UnsupportedOperationException ex) {
            //Ignore since the carousal spinning is not supported for that
        }

        //Step-4 Park the robot in storage unit or warehouse
        parkRobot(driveTrain);

        try {
            dropAdditionalFreight();
        } catch (UnsupportedOperationException ex) {
            //Ignore since the additional freight is done by only warehouse
        }
    }

    protected abstract void dropAdditionalFreight();

    protected abstract boolean isCarousalSpinReversed();

    protected abstract void driveToCarousal(SampleMecanumDrive driveTrain);

    protected abstract void parkRobot(SampleMecanumDrive driveTrain);

    protected abstract void driveToShippingHub(SampleMecanumDrive driveTrain);

    protected abstract int getElevatorLevel(String shippingElementPosition);

    protected abstract RobotPosition getRobotPosition();

}