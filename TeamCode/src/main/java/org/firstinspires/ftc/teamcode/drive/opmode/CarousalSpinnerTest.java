package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.CarousalSpinner;

@Autonomous
public class CarousalSpinnerTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        CarousalSpinner spinner = new CarousalSpinner(hardwareMap);

        waitForStart();

        spinner.spin();

    }
}
