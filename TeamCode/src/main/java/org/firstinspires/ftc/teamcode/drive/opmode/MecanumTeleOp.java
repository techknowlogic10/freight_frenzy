package org.firstinspires.ftc.teamcode.drive.opmode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp
@Config

public class MecanumTeleOp extends OpMode {
    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;
    DcMotor intake = null;
    DcMotor elevator = null;
    DcMotor carousell = null;
    Servo carriagearm = null;
    double ARM_SPEED = 0.1;
    double arm_Pos = 0.0;

    public final static double ARM_HOME = 0.0;

    @Override
    public void init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);
        intake = hardwareMap.get(DcMotor.class, "intake");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        carousell = hardwareMap.get(DcMotor.class, "spinner");

        carriagearm = hardwareMap.servo.get("carriage");
        carriagearm.setPosition(ARM_HOME);
    }

    @Override
    public void loop() {

        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

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

        if (gamepad1.a)
            arm_Pos += ARM_SPEED;
        else if (gamepad1.b)
            arm_Pos -= ARM_SPEED;
        arm_Pos = Range.clip(arm_Pos, 0.0,0.5);
        carriagearm.setPosition(arm_Pos);

        if (gamepad1.dpad_right)
            intake.setPower(.5);
        else if (gamepad1.dpad_left)
            intake.setPower(-.5);
        else
            intake.setPower(0);

        if (gamepad1.left_bumper)
            elevator.setPower(1);
        else if (gamepad1.right_bumper)
            elevator.setPower(-1);
        else
            elevator.setPower(0);

        if (gamepad1.left_stick_button)
            carousell.setPower(1);
        else
            carousell.setPower(0);

        telemetry.addData("arm", "%.2f", arm_Pos);
        telemetry.update();
    }

}
//   for our alliance
//never gonna give you up
//never gonna let you down
