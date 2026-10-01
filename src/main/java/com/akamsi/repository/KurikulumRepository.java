package com.akamsi.repository;

import com.akamsi.entity.Kurikulum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KurikulumRepository extends JpaRepository<Kurikulum, Long> {
}
