package ro.cburcea.playground.devweek18.demos;


import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import ro.cburcea.playground.devweek18.QRCodeReader;

import java.awt.image.BufferedImage;
import java.util.List;

import static ro.cburcea.playground.devweek18.Utils.MILLISECONDS;


public class ExampleDetectQrCodeBoofCV {

    void decodeQRCodeBoofCV(){
        BufferedImage input = UtilImageIO.loadImage(UtilIO.pathExample("C:\\Users\\cburcea\\Desktop\\dev-week\\inputs\\sample-input-2.png"));
        GrayU8 gray = ConvertBufferedImage.convertFrom(input, (GrayU8) null);

        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);

        detector.process(gray);

        // Get's a list of all the qr codes it could successfully detect and decode
        List<QrCode> detections = detector.getDetections();

//        Graphics2D g2 = input.createGraphics();
//        int strokeWidth = Math.max(4, input.getWidth() / 200); // in large images the line can be too thin
//        g2.setColor(Color.GREEN);
//        g2.setStroke(new BasicStroke(strokeWidth));
        for (QrCode qr : detections) {
            // The message encoded in the marker
            System.out.println("message: " + qr.message);

            // Visualize its location in the image
//            VisualizeShapes.drawPolygon(qr.bounds, true, 1, g2);
        }

        // List of objects it thinks might be a QR Code but failed for various reasons
//        List<QrCode> failures = detector.getFailures();
//        g2.setColor(Color.RED);
//        for (QrCode qr : failures) {
//            // If the 'cause' is ERROR_CORRECTION or later then it's probably a real QR Code that
//            if (qr.failureCause.ordinal() < QrCode.Failure.ERROR_CORRECTION.ordinal())
//                continue;
//
//            VisualizeShapes.drawPolygon(qr.bounds, true, 1, g2);
//        }

//        ShowImages.showWindow(input, "Example QR Codes", true);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String result = QRCodeReader.decodeQRCodeBoofCV();
        System.out.println("decodeQRCodeBoofCV" + result);
        long endTime = System.currentTimeMillis();
        System.out.println("BOOF CV process time: " + (endTime - startTime) + MILLISECONDS);
    }
}