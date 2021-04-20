package it.contrader.hardwaretracking.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.contrader.hardwaretracking.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	Item findByCode(String itemCode);

}
