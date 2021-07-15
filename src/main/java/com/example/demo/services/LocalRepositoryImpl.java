package com.example.demo.services;

import com.example.demo.domein.models.RecordByLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class LocalRepositoryImpl implements LocalRepository {
    private List<RecordByLine> database;

    @Autowired
    public LocalRepositoryImpl() {
        this.database = new ArrayList<>();
    }

    @Override
    public void save(RecordByLine record) {
        this.database.add(record);
    }

    @Override
    public void saveAll(Collection<RecordByLine> records) {
        this.database.addAll(records);
    }

    @Override
    public List<RecordByLine> getAllRecords() {
        return this.database;
    }
}
