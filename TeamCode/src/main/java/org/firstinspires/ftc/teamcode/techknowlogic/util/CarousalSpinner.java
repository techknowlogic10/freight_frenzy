package org.firstinspires.ftc.teamcode.techknowlogic.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CarousalSpinner {

    private DcMotor spinner = null;

    public CarousalSpinner(HardwareMap hardwareMap) {
        super();
        this.spinner = hardwareMap.get(DcMotor.class, "spinner");
    }

    public void spin() {

        //Spinner REV Ultra Planitary is on control hub -- port 3

        long start = System.currentTimeMillis();

        //Spin for 5 seconds
        long end = start + 5 * 1000;

        while (System.currentTimeMillis() < end) {
            spinner.setPower(0.4);
        }
    }

    public void spin(boolean reversed) {

        //Spinner REV Ultra Planitary is on control hub -- port 3

        if(reversed) {
            spinner.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        long start = System.currentTimeMillis();

        //Spin for 5 seconds
        long end = start + 3 * 1000;

        while (System.currentTimeMillis() < end) {
            spinner.setPower(1);
        }
    }

}
