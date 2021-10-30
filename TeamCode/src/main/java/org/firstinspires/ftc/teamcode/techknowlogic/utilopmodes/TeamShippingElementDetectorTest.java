package org.firstinspires.ftc.teamcode.techknowlogic.utilopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.techknowlogic.util.TeamShippingElementDetector;

@Autonomous
public class TeamShippingElementDetectorTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {

        TeamShippingElementDetector detector = new TeamShippingElementDetector(hardwareMap, telemetry);

        waitForStart();

        long start = System.currentTimeMillis();
        String position = detector.detectShippingElement();
        long end = System.currentTimeMillis();

        long elapsed = end-start;

        telemetry.log().add("Time took to scan " + elapsed);

        telemetry.log().add("Duck is in " + position);

        while(opModeIsActive()) {
            sleep(100);
        }

    }
}
