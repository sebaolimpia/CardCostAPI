package etraveli.group.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import etraveli.group.controller.BinInfoController;
import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.response.BinInfoListDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.service.IBinInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class BinInfoControllerTest {

    @Mock
    IBinInfoService binInfoService;

    @InjectMocks
    BinInfoController binInfoController;

    @Test
    @DisplayName("Add BIN info successful test.")
    public void addBinInfoSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto expected = new BinInfoDto();
        when(binInfoService.addBinInfo(any())).thenReturn(expected);

        // WHEN
        ResponseEntity<BinInfoDto> current = binInfoController.addBinInfo(expected);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("List BIN info successful test.")
    public void findBinInfoSuccessfulTest() {
        // GIVEN
        BinInfoListDto expected = new BinInfoListDto();
        when(binInfoService.findBinInfo()).thenReturn(expected);

        // WHEN
        ResponseEntity<BinInfoListDto> current = binInfoController.findBinInfo();

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Find BIN info by BIN successful test.")
    public void findBinInfoByBinSuccessfulTest() {
        // GIVEN
        BinInfoDto expected = new BinInfoDto();
        when(binInfoService.findBinInfoByBin(anyInt())).thenReturn(expected);

        // WHEN
        ResponseEntity<BinInfoDto> current = binInfoController.findBinInfoByBin(anyInt());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Update BIN info successful test.")
    public void updateBinInfoSuccessfulTest() {
        // GIVEN
        BinInfoDto expected = new BinInfoDto();
        when(binInfoService.updateBinInfo(1, expected)).thenReturn(expected);

        // WHEN
        ResponseEntity<BinInfoDto> current = binInfoController.updateBinInfo(1, expected);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Delete BIN info successful test.")
    public void deleteBinInfoSuccessfulTest() {
        // GIVEN
        Map<String, Boolean> expected = Map.of("deleted", true);
        doNothing().when(binInfoService).deleteBinInfo(anyInt());

        // WHEN
        ResponseEntity<Map<String, Boolean>> current = binInfoController.deleteBinInfo(anyInt());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }
}
