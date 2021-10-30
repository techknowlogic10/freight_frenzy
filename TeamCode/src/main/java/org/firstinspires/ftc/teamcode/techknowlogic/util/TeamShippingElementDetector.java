package org.firstinspires.ftc.teamcode.techknowlogic.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

public class TeamShippingElementDetector {

    OpenCvWebcam webcam;

    Point leftRectanglePoint1 = new Point(20, 80);
    Point leftRectanglePoint2 = new Point(107, 160);

    Point rightRectanglePoint1 = new Point(190, 80);
    Point rightRectanglePoint2 = new Point(270, 160);

    private String ELEMENT_POSITION = null;

    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;

    public TeamShippingElementDetector(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        this.webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"));
        webcam.setPipeline(new SamplePipeline());
        webcam.setMillisecondsPermissionTimeout(2500);
    }

    public String detectShippingElement() {
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

        while (true) {
            if (ELEMENT_POSITION == null) {
                sleep(100);
            } else {
                webcam.stopStreaming();
                break;
            }

        }

        return ELEMENT_POSITION;
    }

    class SamplePipeline extends OpenCvPipeline {

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

            if (leftRectangleMean < 90) {
                ELEMENT_POSITION = "LEFT";
            } else if (rightRectangleMean < 90) {
                ELEMENT_POSITION = "RIGHT";
            } else {
                ELEMENT_POSITION = "NEITHER";
            }

            telemetry.log().add("Element position " + ELEMENT_POSITION);

            return frame;
        }
    }

    private final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
