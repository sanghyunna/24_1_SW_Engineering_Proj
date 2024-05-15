package cau.se.issuemanagespring.service;

import cau.se.issuemanagespring.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;
}
