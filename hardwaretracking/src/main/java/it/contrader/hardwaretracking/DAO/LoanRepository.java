package it.contrader.hardwaretracking.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.contrader.hardwaretracking.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

}