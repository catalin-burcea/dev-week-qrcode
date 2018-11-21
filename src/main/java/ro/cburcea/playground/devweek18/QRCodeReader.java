package ro.cburcea.playground.devweek18;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static ro.cburcea.playground.devweek18.Utils.MILLISECONDS;
import static ro.cburcea.playground.devweek18.Utils.convertMultipartToFile;

public class QRCodeReader {

    public static String decodeQRCode(InputStream qrCodeimage) throws IOException {
        long startTime = currentTimeMillis();
        try {
            BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            long endTime = currentTimeMillis();
            out.println("ZXING loadImage process time: " + (endTime - startTime) + MILLISECONDS);
            long startTime2 = currentTimeMillis();

            Result result = new com.google.zxing.qrcode.QRCodeReader().decode(bitmap);
            long endTime2 = currentTimeMillis();
            out.println("ZXING alg process time: " + (endTime2 - startTime2) + MILLISECONDS);
            return result.getText();
        } catch (IOException e) {
            out.println("Could not decode QR Code, IOException: " + e.getMessage());
        } catch (Exception e) {
            out.println("There is no QR code in the image");

        }
        return null;
    }

    public static Map<String, StockExchangeAlgorithm.TradePair> processZipFile(MultipartFile zip) {
        long decodeProcessTime = 0;
        long stockExchangeProcessTime = 0;
        Map<String, StockExchangeAlgorithm.TradePair> result = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            File file = convertMultipartToFile(zip);
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                pool.execute(new QRCodeDecoderTask(entry, zipFile, result));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("getDecodedQRCode alg: process time " + decodeProcessTime + MILLISECONDS);
        out.println("stockExchange alg: process time  " + stockExchangeProcessTime + MILLISECONDS);
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
        }
        return result;
    }

//    public static Map<String, StockExchangeAlgorithm.TradePair> processZipFileInputStream(MultipartFile zip) {
//        Map<String, StockExchangeAlgorithm.TradePair> result = new ConcurrentHashMap<>();
//        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//            try {
//            File file = convertMultipartToFile(zip);
//
//            try (FileOutputStream outputStream = new FileOutputStream(file.getCanonicalPath())) {
//                ZipInputStream is;
//                ZipEntry entry;
//                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
//                    is = new ZipInputStream(bis);
//                    out.println("available" + is.available());
//                    ZipEntry entry2 = is.getNextEntry();
//                    while ((entry = is.getNextEntry()) != null) {
//                        FileInputStream stream = new FileInputStream(entry.getName());
//
//                        String decodedQRCode = QRCodeReader.decodeQRCode(stream);
//                        out.println("file name: " + entry.getName() + "; decodedQRCode: " + decodedQRCode);
//                        if (decodedQRCode != null) { // && decodedQRCode.matches("^([0-9]+(.)?[0-9]+( )?)+$")
//                            String[] decodedInput = decodedQRCode.split(" ");
//                            double[] decodedInputAsDoubles = getValidDoubleArray(decodedInput);
//                            result.put(entry.getName(), StockExchangeAlgorithm.stockExchangeN2(decodedInputAsDoubles));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    public static String decodeQRCodeBoofCV() {
        long startTime = currentTimeMillis();

        BufferedImage input = UtilImageIO.loadImage(UtilIO.pathExample("C:\\Users\\cburcea\\Desktop\\dev-week\\inputs3\\1.png"));
        GrayU8 gray = ConvertBufferedImage.convertFrom(input, (GrayU8) null);
        long endTime = currentTimeMillis();
        out.println("BOOF CV loadImage process time: " + (endTime - startTime) + MILLISECONDS);

        long startTime2 = currentTimeMillis();

        QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null, GrayU8.class);
        detector.process(gray);

        long endTime2 = currentTimeMillis();
        out.println("BOOF CV QrCodeDetector process time: " + (endTime2 - startTime2) + MILLISECONDS);

        List<QrCode> detections = detector.getDetections();
        return detections.get(0).message;
    }

}