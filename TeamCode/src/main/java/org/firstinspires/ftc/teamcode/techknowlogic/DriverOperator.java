package org.firstinspires.ftc.teamcode.techknowlogic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DriverOperator extends OpMode {

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;

    // #1
    // Touch sensor tells us that elevator is at the bottom
    //If elevator is at the bottom, intake should be spinning..
    //If elevator is NOT at the bottom, intake should be spinning opposite (to eject)

    // #2
    // Use color sensor to identify if the freight element is in the basket or not
    //If in the basket, rotate the carousal (5 rotations) to let operator know

    DcMotor intake = null;
    DcMotor elevator = null;
    DcMotor carousel = null;
    Servo carriageArm = null;

    DistanceSensor distanceSensor;
    TouchSensor     carriageLimit;
    public final static double ARM_HOME = 0.03;

    private long freightInCarousalTime;
    private boolean isEmptyElevator;

    private ElapsedTime elapsedTime;

    //For Team element capping mechanism
    Servo caExtender = null;
    double ca_armPosition = 0.6;  //initial position of the arm at startup
    double CANE_MIN = 0.0;
    double CANE_MAX = 1.0;
    double CAARM_INCREMENT = 0.001;  //increment the position when the dpad is pressed


    @Override
    public void init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);

        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);

        intake = hardwareMap.get(DcMotor.class, "intake");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        carousel = hardwareMap.get(DcMotor.class, "spinner");

        carriageArm = hardwareMap.servo.get("carriage");
        carriageArm.setPosition(ARM_HOME);

        distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");
        carriageLimit   = hardwareMap.get(TouchSensor.class, "magnetic");
        caExtender  = hardwareMap.get(Servo.class, "caextender");
        //INITIAL STATE MUST BE 0.6
        caExtender.setPosition(0.6);
    }

    @Override
    public void start() {
        super.start();

        elapsedTime = new ElapsedTime();

    }

    @Override
    public void loop() {

        //Two people i.e Driver and Operator
        //Driver drives the robot and handle intake
        //Operator does elevator, drop, arm

        //gamepad1 == Driver
        //gamepad2 == Operator
        double elevatorpower = 1;
        //right_stick_y forward and backward
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!

        //right_stick_x left and right
        double x = gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.left_stick_x;
        double denominator = 0;
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]

        double drivepower = 1;
        if ( gamepad1.a )
            drivepower = 1.8;
        if (gamepad1.b)
            drivepower = 2.2;


        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), drivepower);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        //telemetry.log().add( "drivepower " + frontLeftPower);
        //telemetry.log().add("drivepower " + drivepower);

        leftFront.setPower(frontLeftPower);
        leftRear.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightRear.setPower(backRightPower);

        //Carriage motions are handled by Operator (gamepad2)
        if (gamepad2.a)
            carriageArm.setPosition(ARM_HOME);
        else if (gamepad2.b)
            carriageArm.setPosition(0.3);
        else if (gamepad2.x)
            carriageArm.setPosition(0.6);

        //Intake is handled by Driver (gamepad1)
        if (gamepad1.right_bumper)
            intake.setPower(1.0);
        else if (gamepad1.left_bumper)
            intake.setPower(-1.0);
        else
            intake.setPower(0);

        if (gamepad2.y)
            elevatorpower=0.5;
        else
            elevatorpower=1;

        //Elevator is handled by Operator (gamepad2)
        if (gamepad2.left_bumper) {
            elevator.setPower(elevatorpower);
            //while elevator coming down, we would like to bring the arm to initial (zero) position
            carriageArm.setPosition(ARM_HOME);
        } else if (gamepad2.right_bumper) {
            elevator.setPower(-elevatorpower);
        } else {
            elevator.setPower(0);
        }

        //Carousal is handled by Driver
        if (gamepad1.left_stick_button)
            carousel.setPower(1);
        else
            carousel.setPower(0);
    }

    private void checkIfFreightIsInCarousal() {

        double distance = distanceSensor.getDistance(DistanceUnit.CM);
        if(carriageLimit.isPressed())
        {
            telemetry.log().add(" distance " + distance);
            if (distance > 3  && distance < 8) {
                freightInCarousalTime = System.currentTimeMillis();
            }
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.freightInCarousalTime;

        if(elapsedTime < 1000) {
            carousel.setPower(0.5);
        }
    }
}
