package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Elevator {

    private DcMotorEx elevator = null;
    private Servo carriage = null;

    public Elevator(HardwareMap hardwareMap) {
        this.elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        this.carriage = hardwareMap.get(Servo.class, "carriage");
    }

    /*
            Through testing, we found out that we would need
            1000 ticks to reach level one
            2200 ticks to reach level two
            4000 ticks to reach level three
         */
    public void raiseToTheLevel(int level) {
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        int desiredPosition = 0;
        if (level == 1) {
            desiredPosition = 1000;
        } else if (level == 2) {
            desiredPosition = 2700;
        } else {
            desiredPosition = 4300;
        }

        raiseToDesiredPosition(elevator, desiredPosition);
    }

    private void raiseToDesiredPosition(DcMotorEx elevator, int desiredPosition) {

        elevator.setTargetPosition(desiredPosition);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(0.4);

        while (elevator.isBusy()) {
            sleep(50);
        }
    }

    public void dropFreight(){

        long start = System.currentTimeMillis();

        //It takes about 1 second to drop the freight
        long end = start + 1*1000;

        while (System.currentTimeMillis() < end) {
            carriage.setPosition(0.6);
        }
    }

    private final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
