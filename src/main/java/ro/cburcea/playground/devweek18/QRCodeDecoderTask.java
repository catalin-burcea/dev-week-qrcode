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
                if (decodedInputAsDoubles != null) {
                    StockExchangeAlgorithm.TradePair pair = StockExchangeAlgorithm.stockExchangeN2(decodedInputAsDoubles);
                    if (pair.getBuyPoint() != -1 && pair.getSellPoint() != -1) {
                        result.put(entry.getName(), pair);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        out.println("QRCodeDecoderTask process time: " + (endTime - startTime) + MILLISECONDS);

    }
} 