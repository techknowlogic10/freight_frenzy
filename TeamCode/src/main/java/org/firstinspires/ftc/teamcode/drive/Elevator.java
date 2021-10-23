package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevator {

    /*
        Through testing, we found out that we would need
        1000 ticks to reach level one
        2200 ticks to reach level two
        4000 ticks to reach level three
     */
    public void raiseToTheLevel(int level, HardwareMap hardwareMap) {

        DcMotorEx elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        int desiredPosition = 0;
        if (level == 1) {
            desiredPosition = 1000;
        } else if (level == 2) {
            desiredPosition = 2200;
        } else {
            desiredPosition = 4000;
        }

        raiseToDesiredPosition(elevator, desiredPosition);
    }

    private void raiseToDesiredPosition(DcMotorEx elevator, int desiredPosition) {

        elevator.setTargetPosition(desiredPosition);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(0.2);

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
