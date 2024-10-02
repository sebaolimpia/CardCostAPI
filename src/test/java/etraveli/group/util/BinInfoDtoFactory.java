package etraveli.group.util;

import etraveli.group.dto.BinInfoDto;
import etraveli.group.model.BinInfo;
import net.bytebuddy.utility.RandomString;
import java.util.Locale;
import java.util.Random;

public class BinInfoDtoFactory {

    public static BinInfoDto getBinInfoDtoWithIdBinInfo() {
        Random random = new Random();
        return BinInfoDto.builder()
                .id(random.nextInt(10) + 1)
                .bin(random.nextInt(100000) + 100000)
                .country(RandomString.make().toUpperCase(Locale.ROOT))
                .cost(random.nextDouble(10.00) + 1)
                .build();
    }

    public static BinInfoDto getBinInfoDtoWithoutId() {
        return BinInfoDto.builder()
                .bin(654321)
                .country("UY")
                .cost(5.0)
                .build();
    }

    public static BinInfoDto getBinInfoDtoIntegrationTest() {
        return BinInfoDto.builder()
                .id(1)
                .bin(421821)
                .country("UY")
                .cost(10.0)
                .build();
    }

    public static BinInfoDto getBinInfoDtoToUpdateIntegrationTest() {
        return BinInfoDto.builder()
                .id(1)
                .bin(123456)
                .country("US")
                .cost(5.25)
                .build();
    }

    public static BinInfoDto mapBinInfoToBinInfoDto(BinInfo binInfo) {
        return BinInfoDto.builder()
                .id(binInfo.getId())
                .bin(binInfo.getBin())
                .country(binInfo.getCountry())
                .cost(binInfo.getCost())
                .build();
    }
}
