package org.firstinspires.ftc.teamcode.techknowlogic.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Elevator {

    private DcMotorEx elevator = null;
    private Servo carriage = null;

    public static int LEVEL1_TICKS = 505;
    public static int LEVEL2_TICKS = 990;
    public static int LEVEL3_TICKS = 1800;

    public boolean fullyRaised = false;


    public Elevator(HardwareMap hardwareMap) {
        this.elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        this.carriage = hardwareMap.get(Servo.class, "carriage");
    }

    public void raiseToTheLevel(int level) {
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        int desiredPosition = 0;
        if (level == 1) {
            desiredPosition = LEVEL1_TICKS;
        } else if (level == 2) {
            desiredPosition = LEVEL2_TICKS;
        } else {
            desiredPosition = LEVEL3_TICKS;
        }

        raiseToDesiredPosition(elevator, desiredPosition);
    }

    private void raiseToDesiredPosition(DcMotorEx elevator, int desiredPosition) {

        elevator.setTargetPosition(desiredPosition);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(0.3);

        while (elevator.isBusy()) {
            sleep(50);
        }

        fullyRaised = true;
    }

    public void dropFreight(){

        long start = System.currentTimeMillis();

        //It takes about 1 second to drop the freight
        long end = start + 1*1000;

        //Do not drop freight if elevator is busy
        while(elevator.isBusy()) {
            sleep(50);
        }
        while (System.currentTimeMillis() < end) {
            carriage.setPosition(0.5);
        }
    }

    public void dropToZero() {

        long start = System.currentTimeMillis();

        long end = start + 1*1000;

        while (System.currentTimeMillis() < end) {
            carriage.setPosition(0.0);
        }

        elevator.setTargetPosition(0);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(0.5);

        while (elevator.isBusy()) {
            sleep(50);
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
