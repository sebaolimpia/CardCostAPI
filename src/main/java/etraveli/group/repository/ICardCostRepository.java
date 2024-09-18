package etraveli.group.repository;

import etraveli.group.model.CardCost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ICardCostRepository extends JpaRepository<CardCost, Integer> {

    Optional<CardCost> findByCountry(String country);

    Optional<List<CardCost>> findByCost(Double cost);
}
