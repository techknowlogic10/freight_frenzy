package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
@Config
public class ElevatorTest extends LinearOpMode {

    public static int FLOOR_NUMBER = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        //Get elevator DCMotor
        DcMotorEx elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotorEx carriage = hardwareMap.get(DcMotorEx.class, "carriage");
        carriage.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        if(FLOOR_NUMBER == 1) {
            elevator.setTargetPosition(2000);
        } else if (FLOOR_NUMBER == 2) {
            elevator.setTargetPosition(4000);
        } else {
            elevator.setTargetPosition(8000);
        }

        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        carriage.setTargetPosition(1000);
        carriage.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
