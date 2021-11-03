package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp
@Disabled
public class PotentioMeterTest extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    double errorPrior = 0;
    double integralPrior = 0;
    double lastLoopTime = 0;

    public static double powerCap = 0.1;
    public static double desiredPosition = 0.4;

    private AnalogInput potentiometer = null;
    private DcMotor cargoPicker = null;

    private boolean buttonPressed;

    @Override
    public void init() {
        potentiometer = hardwareMap.get(AnalogInput.class, "potentioMeter");
        cargoPicker = hardwareMap.get(DcMotor.class, "cargoPicker");

        telemetry.log().add("Initial potentiometer voltage :: " + potentiometer.getVoltage());
    }

    @Override
    public void loop() {

        if (gamepad2.dpad_down) {
            buttonPressed = true;
        } else if (gamepad2.dpad_up) {
            buttonPressed = false;
        }

        if (buttonPressed) {
            pidControlCargoPicker();
        }
    }

    public void pidControlCargoPicker() {

        double changeInTime = (runtime.milliseconds() - lastLoopTime) / 1000;

        double error = 0;
        double integral = 0;
        double derivitive = 0;
        double output = 0;

        double kP = 1;
        double kI = 3.4;
        double kD = 0.4;

        error = (desiredPosition - potentiometer.getVoltage());

        telemetry.log().add("error " + error);

        if (Math.abs(error) > 0.1) {
            integralPrior = 0;
        }

        integral = integralPrior + error * changeInTime;
        derivitive = (error - errorPrior) / changeInTime;
        output = kP * error + kI * integral + kD * derivitive;

        telemetry.addData("output after pid call ", output);

        if (Math.abs(output) > powerCap) {
            output = Math.signum(output) * powerCap;
        }

        telemetry.addData("output after powerCap ", output);

        errorPrior = error;
        integralPrior = integral;
        lastLoopTime = runtime.milliseconds();

        cargoPicker.setPower(output);

        telemetry.addData("current DC meter reading", cargoPicker.getCurrentPosition());
        telemetry.update();
    }
}

