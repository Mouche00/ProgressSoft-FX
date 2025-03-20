package org.yc.assignments.progresssoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yc.assignments.progresssoft.models.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal, String> {
}
