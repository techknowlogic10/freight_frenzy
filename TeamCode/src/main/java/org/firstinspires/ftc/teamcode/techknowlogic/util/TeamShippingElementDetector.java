package org.firstinspires.ftc.teamcode.techknowlogic.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;
import org.firstinspires.ftc.teamcode.techknowlogic.util.ScannerCoordinates;
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

    private Point leftRectanglePoint1;
    private Point leftRectanglePoint2;

    private Point rightRectanglePoint1;
    private Point rightRectanglePoint2;

    private String elementPosition = null;

    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;

    Mat inputInYCRCB = new Mat();
    Mat inputInCB = new Mat();

    public TeamShippingElementDetector(HardwareMap hardwareMap, Telemetry telemetry, RobotPosition robotPosition, boolean monitorViewNeeded) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        ScannerCoordinates coordinates = new ScannerCoordinates(robotPosition);

        this.leftRectanglePoint1 = coordinates.getLeftRectanglePoint1();
        this.leftRectanglePoint2 = coordinates.getLeftRectanglePoint2();
        this.rightRectanglePoint1 = coordinates.getRightRectanglePoint1();
        this.rightRectanglePoint2 = coordinates.getRightRectanglePoint2();

        if(monitorViewNeeded) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            this.webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, coordinates.getWebcamName()), cameraMonitorViewId);
        } else {
            this.webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, coordinates.getWebcamName()));
        }

        webcam.setPipeline(new TeamShippingElementDetectorPipeline());
        webcam.setMillisecondsPermissionTimeout(2500);
    }

    public void startDetection() {

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
    }

    public void stopDetection() {
        webcam.stopStreaming();
    }

    public String getElementPosition() {
        return elementPosition;
    }

    class TeamShippingElementDetectorPipeline extends OpenCvPipeline {

        @Override
        public Mat processFrame(Mat frame) {

            Imgproc.rectangle(frame, leftRectanglePoint1, leftRectanglePoint2, new Scalar(0, 0, 255), 2);
            Imgproc.rectangle(frame, rightRectanglePoint1, rightRectanglePoint2, new Scalar(0, 255, 0), 2);

            Imgproc.cvtColor(frame, inputInYCRCB, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(inputInYCRCB, inputInCB, 1);

            Mat leftRectangleFrame = frame.submat(new Rect(leftRectanglePoint1, leftRectanglePoint2));
            Mat rightRectangleFrame = frame.submat(new Rect(rightRectanglePoint1, rightRectanglePoint2));

            int leftRectangleMean = (int) Core.mean(leftRectangleFrame).val[0];
            int rightRectangleMean = (int) Core.mean(rightRectangleFrame).val[0];

            telemetry.log().add("leftRectangleMean is " + leftRectangleMean);
            telemetry.log().add("rightRectangleMean is " + rightRectangleMean);

            if (leftRectangleMean < 80) {
                elementPosition = "LEFT";
            } else if (rightRectangleMean < 80) {
                elementPosition = "RIGHT";
            } else {
                elementPosition = "NEITHER";
            }

            telemetry.log().add("Element position " + elementPosition);

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

