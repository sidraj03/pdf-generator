package com.miglani.pdfgenerator.services;

import com.miglani.pdfgenerator.model.CachedData;
import com.miglani.pdfgenerator.repositories.CacheDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheDataRepository cacheDataRepository;

    @Transactional
    public void put(String key, String value) {
        CachedData cachedData = new CachedData();
        cachedData.setIdentifier(key);
        cachedData.setPath(value);
        cacheDataRepository.save(cachedData);
    }
    @Transactional
    public String get(String key) {
        CachedData cachedData = cacheDataRepository.findByIdentifier(key);
        return cachedData != null ? cachedData.getPath() : null;
    }
}
