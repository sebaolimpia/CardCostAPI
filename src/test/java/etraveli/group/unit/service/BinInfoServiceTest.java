package etraveli.group.unit.service;

import static etraveli.group.util.BinInfoDtoFactory.*;
import static etraveli.group.util.BinInfoFactory.getBinInfoWithIdBinInfo;
import static etraveli.group.util.BinInfoFactory.mapBinInfoDtoToBinInfo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.response.BinInfoListDto;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.BinInfo;
import etraveli.group.repository.IBinInfoRepository;
import etraveli.group.service.impl.BinInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class BinInfoServiceTest {

    // Necessary for modelMapper to call the real methods of the ModelMapper class
    @Spy
    private ModelMapper modelMapper;

    @Mock
    private IBinInfoRepository binInfoRepository;

    @InjectMocks
    private BinInfoService binInfoService;

    @Test
    @DisplayName("Add BIN info successful.")
    void addBinInfoSuccessfulTest() {
        // GIVEN
        BinInfoDto expected = getBinInfoDtoWithoutId();
        BinInfo binInfo = mapBinInfoDtoToBinInfo(expected);

        when(binInfoRepository.save(any(BinInfo.class))).thenReturn(binInfo);

        // WHEN
        BinInfoDto current = binInfoService.addBinInfo(expected);

        // THEN
        verify(binInfoRepository).save(binInfo);
        assertEquals(expected, current);
    }

    private static Stream<Arguments> getBinInfoListParameters() {
        BinInfo binInfo = getBinInfoWithIdBinInfo();
        BinInfoDto binInfoDto = mapBinInfoToBinInfoDto(binInfo);
        return Stream.of(
                Arguments.of(List.of(binInfo), List.of(binInfoDto)),
                Arguments.of(List.of(), List.of()));
    }

    @ParameterizedTest
    @MethodSource("getBinInfoListParameters")
    @DisplayName("List BIN info with exist and not exist BIN info.")
    public void listBinInfoTest(List<BinInfo> binInfoList, List<BinInfoDto> binInfoListDto) {
        // GIVEN
        BinInfoListDto expectedBinInfoList = new BinInfoListDto(binInfoListDto);

        when(binInfoRepository.findAll()).thenReturn(binInfoList);

        // WHEN
        BinInfoListDto currentBinInfoList = binInfoService.findBinInfo();

        // THEN
        verify(binInfoRepository).findAll();
        assertEquals(expectedBinInfoList, currentBinInfoList);
    }

    @Test
    @DisplayName("Find BIN info by BIN successful.")
    void findBinInfoByBinSuccessfulTest() {
        // GIVEN
        BinInfo binInfo = getBinInfoWithIdBinInfo();
        BinInfoDto expected = mapBinInfoToBinInfoDto(binInfo);

        when(binInfoRepository.findByBin(anyInt())).thenReturn(Optional.of(binInfo));

        // WHEN
        BinInfoDto currentCardCostDto = binInfoService.findBinInfoByBin(anyInt());

        // THEN
        verify(binInfoRepository).findByBin(anyInt());
        assertEquals(expected, currentCardCostDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Find BIN info BIN id doesn't found in DB.")
    void findBinInfoByBinUnSuccessfulTest(BinInfo nullBinInfo) {
        // GIVEN
        when(binInfoRepository.findByBin(anyInt())).thenReturn(Optional.ofNullable(nullBinInfo));

        // WHEN
        BinInfoDto current = binInfoService.findBinInfoByBin(anyInt());

        // THEN
        assertNull(current);
        verify(binInfoRepository).findByBin(anyInt());
    }

    @Test
    @DisplayName("Update BIN info successful.")
    void updateBinInfoSuccessfulTest() {
        // GIVEN
        BinInfoDto expected = getBinInfoDtoWithIdBinInfo();
        BinInfo binInfo = mapBinInfoDtoToBinInfo(expected);
        when(binInfoRepository.findById(eq(1))).thenReturn(Optional.of(binInfo));
        when(binInfoRepository.save(any(BinInfo.class))).thenReturn(binInfo);

        // WHEN
        BinInfoDto current = binInfoService.updateBinInfo(1, expected);

        // THEN
        InOrder inOrder = inOrder(binInfoRepository, binInfoRepository);
        inOrder.verify(binInfoRepository).findById(eq(1));
        inOrder.verify(binInfoRepository).save(binInfo);
        assertEquals(expected, current);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Update BIN info doesn't found in DB.")
    void updateBinInfoUnSuccessfulTest(BinInfo nullBinInfo) {
        // GIVEN
        when(binInfoRepository.findById(anyInt())).thenReturn(Optional.ofNullable(nullBinInfo));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () ->
                binInfoService.updateBinInfo(1, any(BinInfoDto.class)));
        verify(binInfoRepository).findById(anyInt());
        verifyNoMoreInteractions(binInfoRepository);
    }

    @Test
    @DisplayName("Delete BIN info successful.")
    void deleteBinInfoSuccessfulTest() {
        // GIVEN
        BinInfo binInfo = getBinInfoWithIdBinInfo();
        when(binInfoRepository.findById(eq(1))).thenReturn(Optional.of(binInfo));

        doNothing().when(binInfoRepository).delete(binInfo);

        // WHEN

        // THEN
        assertDoesNotThrow(() -> binInfoService.deleteBinInfo(1));

        InOrder inOrder = inOrder(binInfoRepository, binInfoRepository);
        inOrder.verify(binInfoRepository).findById(eq(1));
        inOrder.verify(binInfoRepository).delete(binInfo);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Delete BIN info doesn't found in DB.")
    void deleteBinInfoUnSuccessfulTest(BinInfo nullBinInfo) {
        // GIVEN
        when(binInfoRepository.findById(anyInt())).thenReturn(Optional.ofNullable(nullBinInfo));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> binInfoService.deleteBinInfo(anyInt()));
        verify(binInfoRepository).findById(anyInt());
        verifyNoMoreInteractions(binInfoRepository);
    }
}
