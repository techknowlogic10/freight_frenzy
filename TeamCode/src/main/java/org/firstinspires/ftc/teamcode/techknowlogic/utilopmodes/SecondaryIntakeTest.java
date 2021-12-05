package org.firstinspires.ftc.teamcode.techknowlogic.utilopmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
@Config
public class SecondaryIntakeTest extends LinearOpMode {

    public static double ARM_HIGH = 1.0;

    @Override
    public void runOpMode() throws InterruptedException {

        while(true) {
            Servo secondaryIntakeArm = hardwareMap.servo.get("IntakeServo");
            secondaryIntakeArm.setPosition(ARM_HIGH);
        }
    }
}
