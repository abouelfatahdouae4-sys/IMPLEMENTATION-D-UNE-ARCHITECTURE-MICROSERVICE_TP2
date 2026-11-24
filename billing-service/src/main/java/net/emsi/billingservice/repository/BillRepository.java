package net.emsi.billingservice.repository;

import net.emsi.billingservice.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BillRepository extends JpaRepository<Bill, Long> {
    // GET /api/bills/search/findByCustomerId?customerId=1
    List<Bill> findByCustomerId(@Param("customerId") long customerId);
}
