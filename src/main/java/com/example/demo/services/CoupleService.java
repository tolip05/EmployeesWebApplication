package com.example.demo.services;

import com.example.demo.domein.models.Couple;
import com.example.demo.domein.models.RecordByLine;

import java.util.List;

public interface CoupleService {
    void addEmployeeRecords(List<RecordByLine> records);

    List<Couple> findAllTeamsWithOverlap();

    void readFile(String path);

    Couple show();
}
