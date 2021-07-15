package com.example.demo.services;

import com.example.demo.domein.models.RecordByLine;

import java.util.Collection;
import java.util.List;

public interface LocalRepository {
    void save(RecordByLine record);

    void saveAll(Collection<RecordByLine> records);

    List<RecordByLine> getAllRecords();
}
