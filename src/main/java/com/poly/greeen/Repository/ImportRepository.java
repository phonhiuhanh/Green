package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<Import, Integer> {
    @Query("SELECT COALESCE(MAX(i.importID), 0) + 1 FROM Import i")
    int getNextImportId();

    @Modifying
    @Query("UPDATE Import i SET i.isDeleted = true WHERE i.importID = ?1")
    void deleteImport(Integer id);

    @Modifying
    @Query("UPDATE Import i SET i.isDeleted = false WHERE i.importID = ?1")
    void restoreImport(Integer id);

    Page<Import> findAllByIsDeletedIsFalse(Pageable pageable);
}
