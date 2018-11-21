package ro.cburcea.playground.devweek18;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.System.out;
import static ro.cburcea.playground.devweek18.Utils.MILLISECONDS;
import static ro.cburcea.playground.devweek18.Utils.getValidDoubleArray;

class QRCodeDecoderTask implements Runnable {

    private Map<String, StockExchangeAlgorithm.TradePair> result;
    private ZipFile zipFile;
    private ZipEntry entry;

    public QRCodeDecoderTask(ZipEntry entry, ZipFile zipFile, Map<String, StockExchangeAlgorithm.TradePair> map) {
        this.result = map;
        this.zipFile = zipFile;
        this.entry = entry;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        try (InputStream stream = zipFile.getInputStream(entry)) {
            String decodedQRCode = QRCodeReader.decodeQRCode(stream);
            out.println("file name: " + entry.getName() + "; decodedQRCode: " + decodedQRCode);

            if (decodedQRCode != null /*&& decodedQRCode.matches("^([0-9]+(.)?[0-9]+( )?)+$")*/) {
                String[] decodedInput = decodedQRCode.split(" ");
                double[] decodedInputAsDoubles = getValidDoubleArray(decodedInput);
                if(decodedInputAsDoubles != null) {
                    result.put(entry.getName(), StockExchangeAlgorithm.stockExchangeN2(decodedInputAsDoubles));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("getDecodedQRCode process time: " + (endTime - startTime) + MILLISECONDS);

    }
} 