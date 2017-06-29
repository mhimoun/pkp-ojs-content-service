package com.ojs.service.content.v1.service;


import com.ojs.service.content.v1.domain.JournalRepository;
import com.ojs.service.content.v1.domain.Journals;
import com.ojs.service.content.v1.dto.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBJournalService implements JournalService {


    private JournalRepository journalRepository;

    @Autowired
    public DBJournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }


    @Override
    public List<Journal> getEnabledJournals() {

        List<Journal> dtoJournals = new ArrayList<>();

        List<Journals> domainJournals = journalRepository.findByEnabled(1);

        for (Journals dj : domainJournals) {
            Journal dotJournal = new Journal(dj.getJournalId(), dj.getPath());
            dotJournal.setPrimaryLocale(dj.getPrimaryLocale());
            dtoJournals.add(dotJournal);
            if (!CollectionUtils.isEmpty(dj.getJournalSettings())){
                dotJournal.setDescription(dj.getJournalSettings().get(0).getSettingValue());
            }
        }

        return dtoJournals;
    }
}