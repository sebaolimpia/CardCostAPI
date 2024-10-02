package etraveli.group.controller;

import static etraveli.group.common.Constants.*;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.response.BinInfoListDto;
import etraveli.group.service.IBinInfoService;
import etraveli.group.validation.annotation.DigitBinSizeValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(LOCALHOST_URL)
@RequestMapping(INFO_BIN_BASE_URL)
@Validated
public class BinInfoController {

    private final IBinInfoService binInfoService;

    @Autowired
    public BinInfoController(IBinInfoService binInfoService) {
        this.binInfoService = binInfoService;
    }

    @PostMapping("")
    public ResponseEntity<BinInfoDto> addBinInfo(@Valid @RequestBody BinInfoDto binInfoDto) {
        return ResponseEntity.ok(binInfoService.addBinInfo(binInfoDto));
    }

    @GetMapping("/all")
    public ResponseEntity<BinInfoListDto> findBinInfo() {
        return ResponseEntity.ok(binInfoService.findBinInfo());
    }

    @GetMapping("/{bin}")
    public ResponseEntity<BinInfoDto> findBinInfoByBin(@DigitBinSizeValid @PathVariable Integer bin) {
        return ResponseEntity.ok(binInfoService.findBinInfoByBin(bin));
    }

    @PutMapping("/{idBinInfo}")
    public ResponseEntity<BinInfoDto> updateBinInfo(
            @Positive(message = VALIDATION_THE_BIN_INFO_ID_MUST_BE_A_POSITIVE_NUMBER)
            @PathVariable Integer idBinInfo,
            @Valid @RequestBody BinInfoDto binInfoDto) {
        return ResponseEntity.ok(binInfoService.updateBinInfo(idBinInfo, binInfoDto));
    }

    @DeleteMapping("/{idBinInfo}")
    public ResponseEntity<Map<String, Boolean>> deleteBinInfo(
            @Positive(message = VALIDATION_THE_BIN_INFO_ID_MUST_BE_A_POSITIVE_NUMBER)
            @PathVariable Integer idBinInfo) {
        binInfoService.deleteBinInfo(idBinInfo);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
