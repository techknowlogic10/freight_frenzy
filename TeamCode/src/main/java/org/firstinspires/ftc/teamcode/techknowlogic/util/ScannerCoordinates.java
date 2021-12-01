package org.firstinspires.ftc.teamcode.techknowlogic.util;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.techknowlogic.util.RobotPosition;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.openftc.easyopencv.OpenCvWebcam;

@Config
public class ScannerCoordinates {

    //WebcamR
    public static Point RED_CAROUSAL_LEFT_RECTANGLE_POINT1 = new Point(15, 25);
    public static Point RED_CAROUSAL_RIGHT_RECTANGLE_POINT1 = new Point(165, 25);

    //WebcamL
    public static Point RED_WAREHOUSE_LEFT_RECTANGLE_POINT1 = new Point(50, 0);
    public static Point RED_WAREHOUSE_RIGHT_RECTANGLE_POINT1 = new Point(225, 0);

    //WebcamL
    public static Point BLUE_CAROUSAL_LEFT_RECTANGLE_POINT1 = new Point(100, 0);
    public static Point BLUE_CAROUSAL_RIGHT_RECTANGLE_POINT1 = new Point(225, 0);

    //WebcamR
    public static Point BLUE_WAREHOUSE_LEFT_RECTANGLE_POINT1 = new Point(100, 0);
    public static Point BLUE_WAREHOUSE_RIGHT_RECTANGLE_POINT1 = new Point(225, 0);

    public static String LEFT_CAMERA_NAME = "WebcamL";
    public static String RIGHT_CAMERA_NAME = "WebcamR";

    private String webcamName;

    private Point leftRectanglePoint1;
    private Point rightRectanglePoint1;

    private Point leftRectanglePoint2;
    private Point rightRectanglePoint2;

    private double boxHeight = 75;
    private double boxWidth = 75;

    public ScannerCoordinates(RobotPosition robotPosition) {

        if(RobotPosition.RED_CAROUSAL.equals(robotPosition)) {
            this.webcamName = RIGHT_CAMERA_NAME;
            this.leftRectanglePoint1 = RED_CAROUSAL_LEFT_RECTANGLE_POINT1;
            this.rightRectanglePoint1 = RED_CAROUSAL_RIGHT_RECTANGLE_POINT1;
        } else if(RobotPosition.RED_WAREHOUSE.equals(robotPosition)) {
            this.webcamName = LEFT_CAMERA_NAME;
            this.leftRectanglePoint1 = RED_WAREHOUSE_LEFT_RECTANGLE_POINT1;
            this.rightRectanglePoint1 = RED_WAREHOUSE_RIGHT_RECTANGLE_POINT1;
        } else if(RobotPosition.BLUE_CAROUSAL.equals(robotPosition)) {
            this.webcamName = LEFT_CAMERA_NAME;
            this.leftRectanglePoint1 = BLUE_CAROUSAL_LEFT_RECTANGLE_POINT1;
            this.rightRectanglePoint1 = BLUE_CAROUSAL_RIGHT_RECTANGLE_POINT1;
        } else {
            this.webcamName = RIGHT_CAMERA_NAME;
            this.leftRectanglePoint1 = BLUE_WAREHOUSE_LEFT_RECTANGLE_POINT1;
            this.rightRectanglePoint1 = BLUE_WAREHOUSE_RIGHT_RECTANGLE_POINT1;
        }

        leftRectanglePoint2 = new Point(leftRectanglePoint1.x + boxWidth, leftRectanglePoint1.y + boxHeight);
        rightRectanglePoint2 = new Point(rightRectanglePoint1.x + boxWidth, rightRectanglePoint1.y + boxHeight);
    }


    public ScannerCoordinates(String webcamName, double leftX, double leftY, double rightX, double rightY) {
        this.webcamName = webcamName;

        leftRectanglePoint1 = new Point(leftX, leftY);
        rightRectanglePoint1 = new Point(rightX, rightY);

        leftRectanglePoint2 = new Point(leftX + boxWidth, leftY + boxHeight);
        rightRectanglePoint2 = new Point(rightX + boxWidth, rightY + boxHeight);
    }

    public String getWebcamName() {
        return webcamName;
    }

    public Point getLeftRectanglePoint1() {
        return leftRectanglePoint1;
    }

    public Point getRightRectanglePoint1() {
        return rightRectanglePoint1;
    }

    public Point getLeftRectanglePoint2() {
        return leftRectanglePoint2;
    }

    public Point getRightRectanglePoint2() {
        return rightRectanglePoint2;
    }
}
