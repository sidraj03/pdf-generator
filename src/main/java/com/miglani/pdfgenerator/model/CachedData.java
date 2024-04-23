package com.miglani.pdfgenerator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CachedData {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String identifier;
    private String path;
}
