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

    Point point1 = new Point(10, 110);
    Point point2 = new Point(10 + 80, 110 + 80);

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

        /*
         * NOTE: if you wish to use additional Mat objects in your processing pipeline, it is
         * highly recommended to declare them here as instance variables and re-use them for
         * each invocation of processFrame(), rather than declaring them as new local variables
         * each time through processFrame(). This removes the danger of causing a memory leak
         * by forgetting to call mat.release(), and it also reduces memory pressure by not
         * constantly allocating and freeing large chunks of memory.
         */
        /*
        @Override
        public Mat processFrame(Mat input) {

            Point point11 = new Point(20,80);            Point point12 = new Point(107,160);
            Point point21 = new Point(120,80);            Point point22 = new Point(217,160);
            Point point31 = new Point(240,80);            Point point32 = new Point(320,160);



            Imgproc.rectangle(input, point11, point12, new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(input, point21,point22, new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(input, point31,point32, new Scalar(0, 0, 255), 2);
            Mat inputInYCRCB = new Mat();
            Imgproc.cvtColor(input, inputInYCRCB, Imgproc.COLOR_RGB2YCrCb);

            Mat inputInCB = new Mat();
            Core.extractChannel(inputInYCRCB, inputInCB, 1);

            Mat rectangle1Mat = input.submat(new Rect(point11, point12));
            Mat rectangle2Mat = input.submat(new Rect(point21,point22));
            Mat rectangle3Mat = input.submat(new Rect(point31,point32));

            int meanColorForRectangle1 = (int) Core.mean(rectangle1Mat).val[0];
            int meanColorForRectangle2 = (int) Core.mean(rectangle2Mat).val[0];
            int meanColorForRectangle3 = (int) Core.mean(rectangle3Mat).val[0];

            telemetry.log().add("meanColorForRectangle1 is " + meanColorForRectangle1);
            telemetry.log().add("meanColorForRectangle2 is " + meanColorForRectangle2);
            telemetry.log().add("meanColorForRectangle3 is " + meanColorForRectangle3);


            return input;
        }*/

        //Venkat

        @Override
        public Mat processFrame(Mat input) {
            int pt1 = 1, pt2 = 1;

            Point point11 = new Point(20, 80);
            Point point12 = new Point(107, 160);
            Point point21 = new Point(190, 80);
            Point point22 = new Point(270, 160);

            Imgproc.rectangle(input, point11, point12, new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(input, point21, point22, new Scalar(0, 0, 255), 2);

            Mat inputInYCRCB = new Mat();
            Imgproc.cvtColor(input, inputInYCRCB, Imgproc.COLOR_RGB2YCrCb);

            Mat inputInCB = new Mat();
            Core.extractChannel(inputInYCRCB, inputInCB, 1);

            Mat rectangle1Mat = input.submat(new Rect(point11, point12));
            Mat rectangle2Mat = input.submat(new Rect(point21, point22));

            int meanColorForRectangle1 = (int) Core.mean(rectangle1Mat).val[0];
            int meanColorForRectangle2 = (int) Core.mean(rectangle2Mat).val[0];

            if (meanColorForRectangle1 > 110 && meanColorForRectangle1 < 120)
                pt1 = 0;
            if (meanColorForRectangle2 > 110 && meanColorForRectangle1 < 120)
                pt2 = 0;

            if (pt1 == pt2)
                telemetry.log().add("duck in 1" + meanColorForRectangle1);
            else if (pt1 > pt2)
                telemetry.log().add("duck in 2" + meanColorForRectangle1);
            else
                telemetry.log().add("duck in 3" + meanColorForRectangle1);

            telemetry.log().add("meanColorForRectangle1 is " + meanColorForRectangle1);
            telemetry.log().add("meanColorForRectangle2 is " + meanColorForRectangle2);
//            telemetry.log().add("meanColorForRectangle3 is " + meanColorForRectangle3);

            // Empty 110 - 120
            return input;
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



