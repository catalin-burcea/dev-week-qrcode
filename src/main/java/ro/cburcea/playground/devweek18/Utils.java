package ro.cburcea.playground.devweek18;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static final String MILLISECONDS = "ms";

    public static File convertMultipartToFile(MultipartFile multipart) throws IOException {
        File file = new File(multipart.getOriginalFilename());
//        multipart.transferTo(file);
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipart.getBytes());
        }
        return file;
    }


    public static double[] getDoubleArray(String[] decodedInput) {
        double[] result = new double[decodedInput.length];
        for (int i = 0; i < decodedInput.length; i++) {
            result[i] = Double.parseDouble(decodedInput[i]);
        }
        return result;
    }

    public static int[] marchThroughImage(BufferedImage image) {
        int[] result = new int[image.getHeight() * image.getWidth()];
        int w = image.getWidth();
        int h = image.getHeight();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = image.getRGB(j, i);
                result[(i + 1) * j + 1] = pixel;
            }
        }
        return result;
    }

    public static int[] convertByteToIntArray(byte[] input) {
        int[] result = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (int) input[i];
        }
        return result;
    }


}
