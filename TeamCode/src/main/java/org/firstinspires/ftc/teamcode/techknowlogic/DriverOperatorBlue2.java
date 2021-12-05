package org.firstinspires.ftc.teamcode.techknowlogic;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
@Config
public class DriverOperatorBlue2 extends DriverOperator2 {

    @Override
    public void init() {
        super.init();

        carousel.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}