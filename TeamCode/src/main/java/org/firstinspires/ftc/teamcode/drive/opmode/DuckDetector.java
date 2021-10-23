package org.firstinspires.ftc.teamcode.drive.opmode;

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

@Autonomous(name = "Duck Detector")
public class DuckDetector extends LinearOpMode {

    OpenCvWebcam webcam;

        Point leftRectanglePoint1 = new Point(20, 80);
    Point leftRectanglePoint2 = new Point(107, 160);

    Point rightRectanglePoint1 = new Point(190, 80);
    Point rightRectanglePoint2 = new Point(270, 160);


    @Override
    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        telemetry.log().add("cameraMonitorViewId is " + cameraMonitorViewId);

        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"), cameraMonitorViewId);

        webcam.setPipeline(new SamplePipeline());

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
            /*
             * For the purposes of this sample, throttle ourselves to 10Hz loop to avoid burning
             * excess CPU cycles for no reason. (By default, telemetry is only sent to the DS at 4Hz
             * anyway). Of course in a real OpMode you will likely not want to do this.
             */
            sleep(100);
        }

        webcam.stopStreaming();
    }

    class SamplePipeline extends OpenCvPipeline {
        boolean viewportPaused;

        @Override
        public Mat processFrame(Mat frame) {

            Imgproc.rectangle(frame, leftRectanglePoint1, leftRectanglePoint2, new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(frame, rightRectanglePoint1, rightRectanglePoint2, new Scalar(0, 255, 0), 2);

            Mat inputInYCRCB = new Mat();
            Imgproc.cvtColor(frame, inputInYCRCB, Imgproc.COLOR_RGB2YCrCb);

            Mat inputInCB = new Mat();
            Core.extractChannel(inputInYCRCB, inputInCB, 1);

            Mat leftRectangleFrame = frame.submat(new Rect(leftRectanglePoint1, leftRectanglePoint2));
            Mat rightRectangleFrame = frame.submat(new Rect(rightRectanglePoint1, rightRectanglePoint2));

            int leftRectangleMean = (int) Core.mean(leftRectangleFrame).val[0];
            int rightRectangleMean = (int) Core.mean(rightRectangleFrame).val[0];

            telemetry.log().add("leftRectangleMean is " + leftRectangleMean);
            telemetry.log().add("rightRectangleMean is " + rightRectangleMean);

            return frame;
        }

        @Override
        public void onViewportTapped() {
            /*
             * The viewport (if one was specified in the constructor) can also be dynamically "paused"
             * and "resumed". The primary use case of this is to reduce CPU, memory, and power load
             * when you need your vision pipeline running, but do not require a live preview on the
             * robot controller screen. For instance, this could be useful if you wish to see the live
             * camera preview as you are initializing your robot, but you no longer require the live
             * preview after you have finished your initialization process; pausing the viewport does
             * not stop running your pipeline.
             *
             * Here we demonstrate dynamically pausing/resuming the viewport when the user taps it
             */

            viewportPaused = !viewportPaused;

            if (viewportPaused) {
                webcam.pauseViewport();
            } else {
                webcam.resumeViewport();
            }
        }
    }


}



