package org.firstinspires.ftc.teamcode.techknowlogic.utilopmodes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
//@Disabled
@Config
public class DuckDetectorRedCarousal extends BaseDuckDetector {

    public static double leftX = 15;
    public static double leftY = 25;

    public static double rightX = 165;
    public static double rightY = 25;

    public static String webcamName = "WebcamR";

    public DuckDetectorRedCarousal(String webcamName, double leftX, double leftY, double rightX, double rightY) {
        super(webcamName, leftX, leftY, rightX, rightY);
    }
}




