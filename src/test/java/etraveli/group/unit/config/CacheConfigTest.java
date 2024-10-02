package etraveli.group.unit.config;

import static etraveli.group.util.BinInfoDtoFactory.mapBinInfoToBinInfoDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.CardCostDto;
import etraveli.group.model.BinInfo;
import etraveli.group.model.CardCost;
import etraveli.group.repository.IBinInfoRepository;
import etraveli.group.repository.ICardCostRepository;
import etraveli.group.service.impl.BinInfoService;
import etraveli.group.service.impl.CardCostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import java.util.Optional;

public class CacheConfigTest {

    @Mock
    private Cache cache;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ICardCostRepository cardCostRepository;

    @Mock
    private IBinInfoRepository binInfoRepository;

    // Necessary for modelMapper to call the real methods of the ModelMapper class
    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private CardCostService cardCostService;

    @InjectMocks
    private BinInfoService binInfoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(cacheManager.getCache("card_cost")).thenReturn(cache);
        when(cacheManager.getCache("bin_info")).thenReturn(cache);
    }

    @Test
    @DisplayName("Test cacheable card cost method.")
    void testCacheableCardCostMethod() {
        String expectedCountry = "DK";
        CardCost cardCost = new CardCost(1, expectedCountry, 10.0);

        // First call, result shouldn't be in the cache but should be in the repository
        when(cache.get(expectedCountry, CardCost.class)).thenReturn(null);
        when(cache.putIfAbsent(expectedCountry, cardCost)).thenReturn(null);
        when(cardCostRepository.findByCountry(anyString())).thenReturn(Optional.of(cardCost));

        CardCostDto currentCardCostFromRepository = cardCostService.findCardCostByCountry(expectedCountry);
        assertNotNull(currentCardCostFromRepository);
        assertEquals(expectedCountry, currentCardCostFromRepository.getCountry());

        // Second call, should return cached value
        when(cache.get(expectedCountry, CardCost.class)).thenReturn(cardCost);

        CardCostDto currentCardCostFromCache = cardCostService.findCardCostByCountry(expectedCountry);
        assertNotNull(currentCardCostFromCache);
        assertEquals(expectedCountry, currentCardCostFromCache.getCountry());

        assertEquals(currentCardCostFromRepository, currentCardCostFromCache);
    }

    @Test
    @DisplayName("Test cacheable BIN info method.")
    void testCacheableBinInfoMethod() {
        Integer expectedBin = 123456;
        BinInfo binInfo = new BinInfo(1, expectedBin, "US", 10.0);
        BinInfoDto expected = mapBinInfoToBinInfoDto(binInfo);

        // First call, result shouldn't be in the cache but should be in the repository
        when(cache.get(expectedBin, BinInfo.class)).thenReturn(null);
        when(cache.putIfAbsent(expectedBin, binInfo)).thenReturn(null);
        when(binInfoRepository.findByBin(anyInt())).thenReturn(Optional.of(binInfo));

        BinInfoDto currentFromRepository = binInfoService.findBinInfoByBin(expectedBin);
        assertNotNull(currentFromRepository);
        assertEquals(expected, currentFromRepository);

        // Second call, should return cached value
        when(cache.get(expectedBin, BinInfo.class)).thenReturn(binInfo);

        BinInfoDto currentFromCache = binInfoService.findBinInfoByBin(expectedBin);
        assertNotNull(currentFromCache);
        assertEquals(expected, currentFromCache);

        assertEquals(currentFromRepository, currentFromCache);
    }
}
