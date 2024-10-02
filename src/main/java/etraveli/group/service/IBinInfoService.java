package etraveli.group.service;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.response.BinInfoListDto;

public interface IBinInfoService {

    BinInfoDto addBinInfo(BinInfoDto binInfoDto);

    BinInfoListDto findBinInfo();

    BinInfoDto findBinInfoByBin(Integer bin);

    BinInfoDto updateBinInfo(Integer idBinInfo, BinInfoDto binInfoDto);

    void deleteBinInfo(Integer idBinInfo);
}
