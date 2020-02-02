package com.mamun72.service;

import com.mamun72.entity.Test;
import com.mamun72.repo.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestTableService {

    @Autowired
    private TestRepo testRepository;

    public Iterable<Test> getAll()
    {

        return  testRepository.findAll();
       /* List<Test> testList = testRepository.findAll();

        if(testList.size() > 0) {
            return testList;
        } else {
            return new ArrayList<Test>();
        }*/
    }

}
