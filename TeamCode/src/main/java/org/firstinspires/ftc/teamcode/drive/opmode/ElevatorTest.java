package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
@Config
public class ElevatorTest extends LinearOpMode {

    public static int FLOOR_NUMBER = 1;

    //level one 1000 ticks
    //level two 2200 ticks
    //level three 4000 ticks

    @Override
    public void runOpMode() throws InterruptedException {

        //Get elevator DCMotor
        DcMotorEx elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.log().add("current position.." + elevator.getCurrentPosition());

        waitForStart();

        int desiredPosition = 0;

        //elevator.setTargetPosition(-6000);
        while(opModeIsActive()) {

            reachToDesiredPosition(elevator, desiredPosition + 200);

            desiredPosition = desiredPosition + 200;

            sleep(2000);
        }

//        int desiredPosition = 0;
//
//        while (opModeIsActive()) {
//            elevator.setTargetPosition(desiredPosition + 100);
//            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            elevator.setPower(0.2);
//
//            telemetry.log().add("Reached..position.." + elevator.getCurrentPosition());
//
//            desiredPosition = elevator.getCurrentPosition();
//            sleep(5000);
//        }

        //telemetry.log().add("Reached..position.." + elevator.getCurrentPosition());

//        sleep(5000);
//
//        elevator.setTargetPosition(elevator.getCurrentPosition()-5000);
//        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        elevator.setPower(0.2);
//
//        telemetry.log().add("Reached..position.." + elevator.getCurrentPosition());
//
//        sleep(5000);


//        while (opModeIsActive()) {
//            telemetry.log().add("position " + elevator.getCurrentPosition());
//        }
 }

    private void reachToDesiredPosition(DcMotorEx elevator, int desiredPosition) {

        elevator.setTargetPosition(desiredPosition);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(0.2);

        while(elevator.isBusy()) {
            telemetry.log().add("current position.." + elevator.getCurrentPosition());
        }
     }
}
