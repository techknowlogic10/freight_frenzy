package org.firstinspires.ftc.teamcode.techknowlogic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.techknowlogic.util.CarousalSpinner;
import org.firstinspires.ftc.teamcode.techknowlogic.util.Elevator;
import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

public abstract class BaseAutonomous extends LinearOpMode {

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
        elevator.dropFreight();


        //while driving to carousal, bring the elevator down to zero in the background (in a separate thread)
//        Runnable elevatorDropThread = new Runnable() {
//            @Override
//            public void run() {
//                elevator.dropToZero();
//            }
//        };
//        new Thread(elevatorDropThread).start();

        //OPTIONAL -- Step-4 Drive to carousal and spin
        try {
            driveToCarousal(driveTrain);
            carousalSpinner.spin();
        } catch (UnsupportedOperationException ex) {
            //Ignore since the carousal spinning is not supported for that
        }

        //Step-4 Park the robot in storage unit or warehouse
        parkRobot(driveTrain);
    }

    protected abstract void driveToCarousal(SampleMecanumDrive driveTrain);

    protected abstract void parkRobot(SampleMecanumDrive driveTrain);

    protected abstract void driveToShippingHub(SampleMecanumDrive driveTrain);

    protected abstract int getElevatorLevel(String shippingElementPosition);
}
