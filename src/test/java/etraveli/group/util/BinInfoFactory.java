package etraveli.group.util;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.model.BinInfo;
import net.bytebuddy.utility.RandomString;
import java.util.Locale;
import java.util.Random;

public class BinInfoFactory {

    public static BinInfo getBinInfoWithIdBinInfo() {
        Random random = new Random();
        return BinInfo.builder()
                .id(random.nextInt(10) + 1)
                .bin(random.nextInt(100000) + 100000)
                .country(RandomString.make().toUpperCase(Locale.ROOT))
                .cost(random.nextDouble(10.00) + 1)
                .build();
    }

    public static BinInfo mapBinInfoDtoToBinInfo(BinInfoDto binInfoDto) {
        return BinInfo.builder()
                .id(binInfoDto.getId())
                .bin(binInfoDto.getBin())
                .country(binInfoDto.getCountry())
                .cost(binInfoDto.getCost())
                .build();
    }
}
