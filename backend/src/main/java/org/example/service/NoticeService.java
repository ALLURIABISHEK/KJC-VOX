package org.example.service;

import org.example.model.Notice;
import org.example.repository.NoticeRepository;

import java.util.List;

public class NoticeService {
    private final NoticeRepository repository;

    public NoticeService(NoticeRepository repository) {
        this.repository = repository;
    }

    public void createNotice(Notice notice) {
        repository.saveNotice(notice);
    }


    public List<Notice> getNoticesFor(String noticeFor) {
        return repository.getNoticesFor(noticeFor);
    }

}
