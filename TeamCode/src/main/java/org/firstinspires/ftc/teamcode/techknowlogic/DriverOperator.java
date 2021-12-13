package org.firstinspires.ftc.teamcode.techknowlogic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
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

    //For Carousel Spinner
    DcMotor carousel = null;

    Servo servoIntakeArm = null;
    double INTAKE_ARM_HOME = 1.0;
    double INTAKE_ARM_DOWN = 0.0;

    Servo carriageArm = null;
    public final static double CARRIAGE_HOME = 0.04;
    public final static double CARRIAGE_HOLD_POS = 0.3;
    public final static double CARRIAGE_DROP_POS = 0.6;

    //Servo cargoloaded flag indicator
    Servo cargoLoadedflagArm = null;
    public final static double FLAG_RAISE_POSITION = 0.5;
    public final static double FLAG_DOWN_POSITION = 0.1;

    DistanceSensor cargoInBayDS; // tocheck whether cargo is in the carriage
    public final static double MIN_CARGO_DISTANCE = 5;

    DistanceSensor rearCollisionDS;

    TouchSensor     carriageLimit; //tocheck whether carriage is down for cargo intake
    private boolean isCargoinCarriage = false;
    private long freightInCarousalTime;
    private ElapsedTime elapsedTime;

    // Check where the robot crossed into warehouse by checking the white color
    ColorSensor borderCrossingCheckCS;

    //For Team element capping mechanism
    Servo teamElementpickuparm = null;
    public final static double TEAM_ELEMENT_HOME_POS = 0.7;  //initial position of the arm at startup

    double PICKUP_ARM_MIN = 0.2;
    double PICKUP_ARM_MAX = 0.706;

    double CAARM_INCREMENT = 0.005;  //increment the position when the dpad is pressed
    double tep_armposition = TEAM_ELEMENT_HOME_POS;

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

        servoIntakeArm = hardwareMap.servo.get("IntakeServo");
        servoIntakeArm.setPosition(INTAKE_ARM_HOME);

        carriageArm = hardwareMap.servo.get("carriage");
        carriageArm.setPosition(CARRIAGE_HOME);

        cargoLoadedflagArm = hardwareMap.servo.get("cargoLoadedflagarm");
        cargoLoadedflagArm.setPosition(FLAG_DOWN_POSITION);


        cargoInBayDS = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");
        carriageLimit   = hardwareMap.get(TouchSensor.class, "magnetic");
        teamElementpickuparm  = hardwareMap.get(Servo.class, "caextender");

        borderCrossingCheckCS = hardwareMap.get(ColorSensor.class, "bordercheck");

        //rearCollisionDS = hardwareMap.get(DistanceSensor.class,"rearcollision");
        //INITIAL STATE MUST BE 0.6
        teamElementpickuparm.setPosition(TEAM_ELEMENT_HOME_POS);
    }

    @Override
    public void start() {
        super.start();
        servoIntakeArm.setPosition(INTAKE_ARM_DOWN);
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
        if (gamepad1.dpad_up == true){
            double dpad = -1;
            y = dpad;
        }
        if (gamepad1.dpad_down == true){
            double dpadd = 1;
            y = dpadd;
        }

        //right_stick_x left and right
        double x = gamepad1.right_stick_x; // Counteract imperfect strafing
        double rx = gamepad1.left_stick_x * 1.1;
        double denominator = 0;
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]

        double drivepower = 1;

//        telemetry.log().add("rearDistance " + rearCollisionDS.getDistance(DistanceUnit.CM));
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


        if (gamepad1.x) {
            servoIntakeArm.setPosition(INTAKE_ARM_DOWN);
        } else if (gamepad1.y) {
            servoIntakeArm.setPosition(INTAKE_ARM_HOME);
        }

        //Carriage motions are handled by Operator (gamepad2)
        if (gamepad2.a)
            carriageArm.setPosition(CARRIAGE_HOME);
        else if (gamepad2.b && !carriageLimit.isPressed())
            carriageArm.setPosition(CARRIAGE_HOLD_POS);   //hold the carriage to avoid dropping the cargo
        else if (gamepad2.x) {
            carriageArm.setPosition(CARRIAGE_DROP_POS);
            //isCargoinCarriage = false; // dropped the carriage
        }
        //In take is handled by Driver (gamepad1)
        if (gamepad1.right_bumper)
            intake.setPower(0.8);
        else if (gamepad1.left_bumper)
            intake.setPower(-0.8);
        else
            intake.setPower(0);

        if (gamepad2.y)
            elevatorpower=0.5;
        else
            elevatorpower=1;
        if (gamepad1.left_trigger > 0.01){
            carousel.setPower(0.6);
        } else{
            carousel.setPower(0);
        }
        //Elevator is handled by Operator (gamepad2)
        if (gamepad2.left_bumper) {
            elevator.setPower(elevatorpower);
            //while elevator coming down, we would like to bring the arm to initial (zero) position
            carriageArm.setPosition(CARRIAGE_HOME);
        } else if (gamepad2.right_bumper) {
            elevator.setPower(-elevatorpower);
        } else {
            elevator.setPower(0);
        }

        //Carousal is handled by Driver
/*

        if (gamepad1.left_trigger > 0.1)
            carousel.setPower(.7);
        else
            carousel.setPower(0);


*/
        int n;
        double caroselspeed = 0;
        caroselspeed = 0.05;



    //    carousel.setPower(0);

   /*
        telemetry.addData("carosel", caroselspeed);
        while (gamepad1.left_trigger > 0.1){
            caroselspeed = caroselspeed * 1.3;
            caroselspeed = Range.clip(caroselspeed, 0,0.8);
            carousel.setPower(caroselspeed);
            try {
                Thread.sleep(50);
            } catch (Exception e){}

            telemetry.update();

        }
        if (gamepad1.left_trigger <= 0.1){
            caroselspeed = 0;
            carousel.setPower(caroselspeed);
        }
        /*
    */



        //Element pickup  arm movement for freight element pickup
        if(gamepad2.dpad_down) {
            tep_armposition = tep_armposition - CAARM_INCREMENT;
        } else if(gamepad2.dpad_up) {
            tep_armposition = tep_armposition + CAARM_INCREMENT;
        }
        tep_armposition = Range.clip(tep_armposition, PICKUP_ARM_MIN, PICKUP_ARM_MAX);
        teamElementpickuparm.setPosition(tep_armposition);

        checkIfFreightIsInCarousal();
        checkBorderCrossing();
    }
    private void checkBorderCrossing(){
        if(carriageLimit.isPressed())
        {
           // telemetry.log().add("carriage is down");
        }else
        {
            //telemetry.log().add("carriage is up");
        }
        // convert the RGB values to HSV values.
        // multiply by the SCALE_FACTOR.
        // then cast it back to int (SCALE_FACTOR is a double)
        //Color.RGBToHSV((int) (borderCrossingCheckCS.red() * SCALE_FACTOR),
        //        (int) (borderCrossingCheckCS.green() * SCALE_FACTOR),
        //        (int) (borderCrossingCheckCS.blue() * SCALE_FACTOR),
        //        hsvValues);

        // send the info back to driver station using telemetry function.
        //telemetry.addData("Alpha", borderCrossingCheckCS.alpha());
        //telemetry.addData("Red  ", borderCrossingCheckCS.red());
        //telemetry.addData("Green", borderCrossingCheckCS.green());
        //telemetry.addData("Blue ", borderCrossingCheckCS.blue());
        if( borderCrossingCheckCS.red() > 100 &&
                borderCrossingCheckCS.blue() > 100 &&
                borderCrossingCheckCS.green() > 100)
        {
            telemetry.log().add("Border White line Crossed");
        }
    }
    private void checkIfFreightIsInCarousal() {

        double distance = cargoInBayDS.getDistance(DistanceUnit.CM);

        //telemetry.log().add(" distance " + distance);

        if (distance < MIN_CARGO_DISTANCE )
        {
            if (isCargoinCarriage == false) {
                freightInCarousalTime = System.currentTimeMillis();
                isCargoinCarriage = true;
            }
            cargoLoadedflagArm.setPosition(FLAG_RAISE_POSITION);
            //servoIntakeArm.setPosition(INTAKE_ARM_HOME);
            //isCargoinCarrige is set to false when the carriage is delivered or lost during transit
        }
        else {
            isCargoinCarriage = false;
            cargoLoadedflagArm.setPosition(FLAG_DOWN_POSITION);
            servoIntakeArm.setPosition(INTAKE_ARM_DOWN);
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.freightInCarousalTime;
//
        if(elapsedTime < 2000) {
////            //carousel.setPower(0.5);
////            elevator.setPower(0.5);
//            //reverse the intake to avoid additional cargo getting inside the robot.
            intake.setPower(1.0);
////            //raise the elevator
        }
    }
}