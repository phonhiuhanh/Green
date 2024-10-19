package com.poly.greeen.Repository;

import com.poly.greeen.Entity.ImportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInfoRepository extends JpaRepository<ImportInfo, Integer> {
}
