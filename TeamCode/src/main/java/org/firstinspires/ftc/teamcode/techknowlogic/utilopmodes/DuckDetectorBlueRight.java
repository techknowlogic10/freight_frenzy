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

@Autonomous(name = "Duck Detector Blue Right")
//@Disabled
@Config
public class DuckDetectorBlueRight extends LinearOpMode {

    OpenCvWebcam webcam;

    //ONLY CHANGE is LEFT X
    public static double leftX = 100;
    public static double leftY = 0;

    public static double rightX = 225;
    public static double rightY = 0;

    public static double boxHeight=75;
    public static double boxWidth=75;

    public static String webcamName = "WebcamL";

    private Mat inputInYCRCB = new Mat();
    private Mat inputInCB = new Mat();



    @Override
    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);

        webcam.setPipeline(new DuckDetectionPipeline());

        webcam.setMillisecondsPermissionTimeout(2500);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.log().add("Not able to open Camera");
            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            sleep(100);
        }

        webcam.stopStreaming();
    }

    class DuckDetectionPipeline extends OpenCvPipeline {
        boolean viewportPaused;

        @Override
        public Mat processFrame(Mat frame) {
            Imgproc.rectangle(frame, getLeftRectanglePoint1(), getLeftRectanglePoint2(), new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(frame, getRightRectanglePoint1(), getRightRectanglePoint2(), new Scalar(0, 255, 0), 2);

            Imgproc.cvtColor(frame, inputInYCRCB, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(inputInYCRCB, inputInCB, 1);

            Mat leftRectangleFrame = frame.submat(new Rect(getLeftRectanglePoint1(), getLeftRectanglePoint2()));
            Mat rightRectangleFrame = frame.submat(new Rect(getRightRectanglePoint1(), getRightRectanglePoint2()));

            int leftRectangleMean = (int) Core.mean(leftRectangleFrame).val[0];
            int rightRectangleMean = (int) Core.mean(rightRectangleFrame).val[0];

            telemetry.log().add("leftRectangleMean is " + leftRectangleMean);
            telemetry.log().add("rightRectangleMean is " + rightRectangleMean);

            return frame;
        }

        @Override
        public void onViewportTapped() {
        }
    }

    public Point getLeftRectanglePoint1() {
        return new Point(leftX, leftY);
    }

    public Point getLeftRectanglePoint2() {
        return new Point(leftX + boxWidth, leftY+boxHeight);
    }

    public Point getRightRectanglePoint1() {
        return new Point(rightX, rightY);
    }

    public Point getRightRectanglePoint2() {
        return new Point(rightX+boxWidth, rightY+boxHeight);
    }
}




