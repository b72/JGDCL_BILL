package com.mamun72.service;

import com.mamun72.entity.Bill;
import com.mamun72.repo.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillPayService {
    @Autowired
    private BillRepo billRepo;
    public Iterable<Bill> getAllBills(){
        return billRepo.findAll();
    }

}
