package com.miglani.pdfgenerator.repositories;

import com.miglani.pdfgenerator.model.CachedData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CacheDataRepository extends JpaRepository<CachedData, Long> {
    CachedData findByIdentifier(String key);
}
