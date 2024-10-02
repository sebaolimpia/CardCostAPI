package etraveli.group.repository;

import etraveli.group.model.BinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IBinInfoRepository extends JpaRepository<BinInfo, Integer> {

        Optional<BinInfo> findByBin(Integer bin);

        Optional<BinInfo> findByCountry(String country);
}
