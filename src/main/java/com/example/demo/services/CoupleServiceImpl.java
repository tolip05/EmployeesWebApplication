package com.example.demo.services;

import com.example.demo.domein.models.Couple;
import com.example.demo.domein.models.Project;
import com.example.demo.domein.models.RecordByLine;
import com.example.demo.factories.CoupleFactory;
import com.example.demo.factories.RecordByLineFactory;
import com.example.demo.io.FileIO;
import com.example.demo.io.FileIOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.example.demo.constants.ApplicationConstants.DEFAULT_OVERLAP_ZERO_DAYS;
import static com.example.demo.constants.ApplicationConstants.INDEX_ZERO;
import static com.example.demo.constants.ApplicationConstants.ONE;

@Service
public class CoupleServiceImpl implements CoupleService {
    private final LocalRepository localRepository;
    private FileIO fileIO;
    private Couple result;

    @Autowired
    public CoupleServiceImpl(LocalRepository localRepository) {
        this.localRepository = localRepository;
        this.fileIO = new FileIOImpl();
    }

    @Override
    public void addEmployeeRecords(List<RecordByLine> records) {
        this.localRepository.saveAll(records);
    }

    @Override
    public List<Couple> findAllTeamsWithOverlap() {
        List<RecordByLine> allRecords = this.localRepository.getAllRecords();
        List<Couple> teams = new ArrayList<>();
        for (int i = INDEX_ZERO; i < allRecords.size() - ONE; i++) {
            for (int j = i + ONE; j < allRecords.size(); j++) {
                RecordByLine firstEmpl = allRecords.get(i);
                RecordByLine secondEmpl = allRecords.get(j);

                if (firstEmpl.getProjectId() == secondEmpl.getProjectId()
                        && hasOverlap(firstEmpl, secondEmpl)) {

                    long overlapDays = calculateOverlap(firstEmpl, secondEmpl);
                    if (overlapDays > DEFAULT_OVERLAP_ZERO_DAYS) {
                        updateTeamCollection(teams, firstEmpl, secondEmpl, overlapDays);
                    }
                }
            }
        }
        return teams;
    }

    @Override
    public void readFile(String name) {
        String pathFile = null;
        String path = "D:\\Ac";
        File file = new File(path);
        ArrayDeque<File> deque = new ArrayDeque<>();
        deque.offer(file);
        while (!deque.isEmpty()) {
            File file1 = deque.poll();
            if (file1.getName().equals(name)) {
                pathFile = file1.getPath();
                break;
            }
            if (file1.isDirectory()) {
                File[] directories = file1.listFiles();
                for (File directory : directories) {
                    deque.offer(directory);
                }
            }

        }
        List<RecordByLine> records = this.fileIO.read(pathFile)
                .stream().map(RecordByLineFactory::execute)
                .collect(Collectors.toList());
        addEmployeeRecords(records);
        List<Couple> couples = findAllTeamsWithOverlap();
        this.result = null;
        if (couples.size() != 0) {
            couples.sort((firstCouple, secondCouple) ->
                    (int) (secondCouple.getTotalDuration() - firstCouple.getTotalDuration()));
            this.result = couples.get(0);
        }
    }

    @Override
    public Couple show() {
        if (this.result == null) {
            throw new IllegalArgumentException("Not result!");
        }
        this.localRepository.getAllRecords().clear();
        return this.result;
    }

    private void updateTeamCollection(List<Couple> teams, RecordByLine firstEmpl, RecordByLine secondEmpl, long overlapDays) {
        Project project = new Project(firstEmpl.getProjectId(), overlapDays);
        AtomicBoolean isPresent = new AtomicBoolean(false);
        teams.forEach(team -> {
            if (isTeamPresent(team, firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId())) {
                team.addOverlapDuration(overlapDays);
                team.getProjects().add(project);
                isPresent.set(true);
            }
        });
        if (!isPresent.get()) {
            Couple newTeam = CoupleFactory.execute(
                    firstEmpl.getEmployeeId(),
                    secondEmpl.getEmployeeId(),
                    overlapDays);
            newTeam.getProjects().add(project);
            teams.add(newTeam);
        }
    }

    private boolean isTeamPresent(Couple team, long firstEmplId, long secondEmplId) {
        return (team.getFirstEmplId() == firstEmplId
                && team.getSecondEmplId() == secondEmplId)
                || (team.getFirstEmplId() == secondEmplId
                && team.getSecondEmplId() == firstEmplId);
    }

    private long calculateOverlap(RecordByLine firstEmpl, RecordByLine secondEmpl) {
        LocalDate periodStartDate =
                firstEmpl.getDateFrom().isBefore(secondEmpl.getDateFrom()) ?
                        secondEmpl.getDateFrom() : firstEmpl.getDateFrom();

        LocalDate periodEndDate =
                firstEmpl.getDateTo().isBefore(secondEmpl.getDateTo()) ?
                        firstEmpl.getDateTo() : secondEmpl.getDateTo();
        return Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate));
    }

    private boolean hasOverlap(RecordByLine firstEmpl, RecordByLine secondEmpl) {
        return (firstEmpl.getDateFrom().isBefore(secondEmpl.getDateTo())
                || firstEmpl.getDateFrom().isEqual(secondEmpl.getDateTo()))
                && (firstEmpl.getDateTo().isAfter(secondEmpl.getDateFrom())
                || firstEmpl.getDateTo().isEqual(secondEmpl.getDateFrom()));
    }
}
