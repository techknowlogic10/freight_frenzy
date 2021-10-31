package org.firstinspires.ftc.teamcode.techknowlogic;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp
public class Test extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    double errorPrior = 0;
    double integralPrior = 0;
    double lastLoopTime = 0;
    double powerCap = 0.1;
    public static double A = 0.4;
    public static double B = 0;

    private AnalogInput potentiometer = null;
    private DcMotor arm = null;

    @Override
    public void runOpMode() {
        potentiometer = hardwareMap.get(AnalogInput.class, "potentioMeter");
        arm = hardwareMap.get(DcMotor.class, "arm");

        while(!opModeIsActive()){}
        telemetry.log().add("voltage :: "+potentiometer.getVoltage());
        while(true){

            double voltreading = (float) potentiometer.getVoltage();
            //convert voltage to distance (cm)

            double percentTurned = voltreading/5 * 100;
            pidControlArm(A);
        }
    }

    public void pidControlArm(double desiredPosition){

        if (gamepad2.dpad_down){
            desiredPosition = A;
            double changeInTime = (runtime.milliseconds() - lastLoopTime)/1000;
            double error = 0;
            double integral = 0;
            double derivitive = 0;
            double output = 0;
            double kP = 1;
            double kI = 3.4;
            double kD = 0.4;

            error = -(desiredPosition - potentiometer.getVoltage());
            if(Math.abs(error) > 0.1){
                integralPrior = 0;
            }

            integral = integralPrior + error*changeInTime;
            derivitive = (error-errorPrior)/changeInTime;
            output = kP*error + kI*integral + kD*derivitive;
            if (Math.abs(output) > powerCap){
                output = Math.signum(output)*powerCap;
            }
            integral = integralPrior + error*changeInTime;
            derivitive = (error-errorPrior)/changeInTime;
            output = kP*error + kI*integral + kD*derivitive;
            if (Math.abs(output) > powerCap){
                output = Math.signum(output)*powerCap;
            }
            errorPrior = error;
            integralPrior = integral;
            lastLoopTime = runtime.milliseconds();

            arm.setPower(output);
            telemetry.addData("pos", arm.getCurrentPosition());
            telemetry.update();
        }
        else{
            lastLoopTime = runtime.milliseconds();
        }
    }
}
