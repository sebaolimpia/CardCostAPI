package etraveli.group.service.impl;

import static etraveli.group.common.Constants.EXCEPTION_ALREADY_EXIST_BIN;
import static etraveli.group.common.Constants.EXCEPTION_NOT_FOUND_BIN_INFO_WITH_ID;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.response.BinInfoListDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.exception.BadRequestException;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.BinInfo;
import etraveli.group.model.CardCost;
import etraveli.group.repository.IBinInfoRepository;
import etraveli.group.service.IBinInfoService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class BinInfoService implements IBinInfoService {

    private final IBinInfoRepository binInfoRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public BinInfoService(IBinInfoRepository binInfoRepository, ModelMapper modelMapper) {
        this.binInfoRepository = binInfoRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Add new BIN info.
     * @param binInfoDto BIN info.
     * @return BIN info added.
     */
    @Override
    @CacheEvict(value = "bin_info", allEntries = true)
    public BinInfoDto addBinInfo(BinInfoDto binInfoDto) {
        if (binInfoRepository.findByBin(binInfoDto.getBin()).isPresent()) {
            throw new BadRequestException(String.format(EXCEPTION_ALREADY_EXIST_BIN, binInfoDto.getBin()));
        }
        BinInfoDto newBinInfoDto = BinInfoDto.builder()
                .bin(binInfoDto.getBin())
                .country(binInfoDto.getCountry().toUpperCase(Locale.ROOT))
                .cost(binInfoDto.getCost())
                .build();
        binInfoRepository.save(modelMapper.map(newBinInfoDto, BinInfo.class));
        return newBinInfoDto;
    }

    /**
     * Find all BIN info.
     * @return List of BIN info.
     */
    @Override
    @Cacheable("bin_info")
    public BinInfoListDto findBinInfo() {
        List<BinInfo> binInfoList = binInfoRepository.findAll();
        BinInfoListDto binInfoListDto = BinInfoListDto.builder().build();
        if (binInfoList.isEmpty()) {
            binInfoListDto.setBinInfo(List.of());
            return binInfoListDto;
        }
        binInfoListDto.setBinInfo(binInfoList.stream().map(binInfo ->
                modelMapper.map(binInfo, BinInfoDto.class)).toList());
        return binInfoListDto;
    }

    /**
     * Find BIN info by BIN.
     * @param bin BIN.
     * @return BIN info.
     */
    @Override
    @Cacheable("bin_info")
    public BinInfoDto findBinInfoByBin(Integer bin) {
        BinInfo binInfo = binInfoRepository.findByBin(bin).orElse(null);
        return binInfo != null ? modelMapper.map(binInfo, BinInfoDto.class) : null;
    }

    /**
     * Update bin info.
     * @param idBinInfo BIN info id.
     * @param binInfoDto BIN info update.
     * @return BIN info updated.
     */
    @Override
    @CacheEvict(value = "bin_info", allEntries = true)
    public BinInfoDto updateBinInfo(Integer idBinInfo, BinInfoDto binInfoDto) {
        BinInfo binInfo = findBinInfo(idBinInfo);
        binInfoDto.setId(binInfo.getId());
        binInfoDto.setCountry(binInfoDto.getCountry().toUpperCase(Locale.ROOT));
        binInfoRepository.save(modelMapper.map(binInfoDto, BinInfo.class));
        return binInfoDto;
    }

    /**
     * Delete BIN info.
     * @param idBinInfo BIN info id.
     */
    @Override
    @CacheEvict(value = "bin_info", allEntries = true)
    public void deleteBinInfo(Integer idBinInfo) {
        BinInfo binInfo = findBinInfo(idBinInfo);
        binInfoRepository.delete(binInfo);
    }

    private BinInfo findBinInfo(Integer idBinInfo) {
        BinInfo binInfo = binInfoRepository.findById(idBinInfo).orElse(null);
        if (binInfo == null) {
            throw new NotFoundException(String.format(EXCEPTION_NOT_FOUND_BIN_INFO_WITH_ID, idBinInfo));
        }
        return binInfo;
    }
}
