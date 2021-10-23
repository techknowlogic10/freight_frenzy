package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
@Config
public class ELevatorTeleOpTest extends OpMode {

    DcMotor leftFront = null;
    DcMotor rightFront = null;
    DcMotor leftRear = null;
    DcMotor rightRear = null;
    DcMotor intake = null;
    DcMotor elevator = null;
    Servo carriagearm = null;
    double ARM_SPEED = 50;
    double arm_Pos = 0.0;

    public final static double ARM_HOME = 0.0;

    @Override
    public void init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        intake = hardwareMap.get(DcMotor.class, "intake");
        elevator = hardwareMap.get(DcMotor.class, "elevator");

        carriagearm = hardwareMap.servo.get("carriage");
        carriagearm.setPosition(ARM_HOME);
    }

    @Override
    public void loop() {

        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double rightX = gamepad1.right_stick_y;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        leftFront.setPower(v1);
        rightFront.setPower(v2);
        leftRear.setPower(v3);
        rightRear.setPower(v4);

        if (gamepad1.a)
            arm_Pos += ARM_SPEED;
        else if (gamepad1.b)
            arm_Pos -= ARM_SPEED;
        elevator.setTargetPosition((int) arm_Pos);

        if (gamepad1.dpad_right)
            intake.setPower(.5);
        else if (gamepad1.dpad_left)
            intake.setPower(-.5);
        else
            intake.setPower(0);

        if (gamepad1.left_bumper)
            elevator.setPower(.5);
        else if (gamepad1.right_bumper)
            elevator.setPower(-.5);
        else
            elevator.setPower(0);

        telemetry.addData("arm", "%.2f", arm_Pos);
        telemetry.update();
    }


}
