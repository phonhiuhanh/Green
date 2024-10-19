package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<Import, Integer> {
}
