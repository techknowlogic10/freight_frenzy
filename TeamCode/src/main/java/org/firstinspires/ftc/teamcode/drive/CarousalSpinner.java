package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CarousalSpinner {

    private Servo spinner = null;

    public CarousalSpinner(HardwareMap hardwareMap) {
        super();
        this.spinner = hardwareMap.get(Servo.class, "spinner");
    }

    public void spin() {

        //Spinner servo is on control hub -- port 5
        spinner.setDirection(Servo.Direction.REVERSE);

        long start = System.currentTimeMillis();

        //Spin for 5 seconds
        long end = start + 5 * 1000;

        while (System.currentTimeMillis() < end) {
            spinner.setPosition(1.0);
        }
    }

}
