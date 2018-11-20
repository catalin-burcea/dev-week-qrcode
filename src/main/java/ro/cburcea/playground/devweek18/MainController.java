package ro.cburcea.playground.devweek18;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static ro.cburcea.playground.devweek18.Utils.MILLISECONDS;

@RestController
public class MainController {

    @PostMapping(path = "/stockExchange", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, StockExchangeAlgorithm.TradePair>> decodeQrCode(@RequestParam("file") MultipartFile zip) {
        System.out.println("*****************************************************");
        long startTime = System.currentTimeMillis();
        Map<String, StockExchangeAlgorithm.TradePair> result = QRCodeReader.processZipFile(zip);
        long endTime = System.currentTimeMillis();
        System.out.println("TOTAL process time: " + (endTime - startTime) + MILLISECONDS);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
