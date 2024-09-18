package etraveli.group.unit.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import etraveli.group.dto.CardCostDto;
import etraveli.group.model.CardCost;
import etraveli.group.repository.ICardCostRepository;
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

    // Necessary for modelMapper to call the real methods of the ModelMapper class
    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private CardCostService cardCostService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(cacheManager.getCache("card_cost")).thenReturn(cache);
    }

    @Test
    @DisplayName("Test cacheable method.")
    void testCacheableMethod() {
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
}
