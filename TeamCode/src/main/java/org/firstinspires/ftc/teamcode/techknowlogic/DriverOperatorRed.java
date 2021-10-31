package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp
@Config
public class DriverOperatorRed extends OpMode {
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;

    DcMotor intake = null;
    DcMotor elevator = null;
    DcMotor carousel = null;
    Servo carriageArm = null;

    //Expansion Hub -- port 3
    //use low power
    DcMotor cargoPicker = null;
    CRServo caExtender = null;
    //Cargo Arm Extender -- Expansion Hub -- port 5

    int ARM_SPEED = 100;

    public final static double ARM_HOME = 0.0;

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

        cargoPicker = hardwareMap.get(DcMotor.class, "cargoPicker");
        //cargoPicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        caExtender = hardwareMap.get(CRServo.class, "caExtender");

        carriageArm = hardwareMap.servo.get("carriage");
        carriageArm.setPosition(ARM_HOME);
    }

    @Override
    public void loop() {

        //Two people i.e Driver and Operator
        //Driver drives the robot and handle intake
        //Operator does elevator, drop, arm

        //gamepad1 == Driver
        //gamepad2 == Operator

        //right_stick_y forward and backward
        double y = -gamepad1.right_stick_y; // Remember, this is reversed!

        //right_stick_x left and right
        double x = gamepad1.right_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.left_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftRear.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightRear.setPower(backRightPower);

        //CARGO PICKER settings
        double cargoPickerPower = Range.clip(gamepad2.right_stick_y, -0.3, 0.3);
        cargoPicker.setPower(cargoPickerPower);

        /* if(gamepad2.dpad_left) {
            cargoPicker.setPower(0.8);
        } else if(gamepad2.dpad_down){
            cargoPicker.setPower(-0.7);
        }else if(gamepad2.right_stick_y != 0.0) {
             gamepad2.dpad_left = false;
             gamepad2.dpad_right = false;
         }
*/

        /*if (gamepad2.dpad_up)

            cargoPicker.setPower(0.5);
        else if (gamepad2.dpad_down)
            cargoPicker.setPower(-0.5);
        else
            //cargoPicker.setPower(0.0);
            cargoPicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        if (gamepad2.dpad_left)
            caExtender.setPower(1.0);
        else if (gamepad2.dpad_right)
            caExtender.setPower(-1.0);
        else
            caExtender.setPower(0.0);

        //Carriage motions are done by Operator (gamepad2)
        if (gamepad2.a)
            carriageArm.setPosition(0);
        else if (gamepad2.b)
            carriageArm.setPosition(0.3);
        else if (gamepad2.x)
            carriageArm.setPosition(0.6);

        //Intake handled by Driver (gamepad1)
        if (gamepad1.right_bumper)
            intake.setPower(1.0);
        else if (gamepad1.left_bumper)
            intake.setPower(-1.0);
        else
            intake.setPower(0);

        //Elevator is handled by Operator (gamepad2)
        if (gamepad2.left_bumper) {
            elevator.setPower(1);
        } else if (gamepad2.right_bumper) {
            //while elevator coming down, we would like to bring the arm to initial (zero) position
            carriageArm.setPosition(0);
            elevator.setPower(-1);
        } else {
            elevator.setPower(0);
        }

        //Carousal is handled by Driver
        if (gamepad1.left_stick_button)
            carousel.setPower(1);
        else
            carousel.setPower(0);
    }
}