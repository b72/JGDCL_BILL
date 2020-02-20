package com.mamun72.service;

import com.mamun72.billarApi.JgdlConfig;
import com.mamun72.billarApi.PayBillRequest;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.repo.BillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BillPayService {
    @Autowired
    private BillRepo billRepo;

    public Iterable<Bill> getAllBills() {
        return billRepo.findAll();
    }

    public Bill saveBill(Bill bill) {
        return billRepo.save(bill);
    }

    public int payBill(PayBillRequest payBillRequest, String apiTrxId, int status, User user) {
        return billRepo.payBill(
                apiTrxId,
                payBillRequest.getPaidAmount(),
                status,
                payBillRequest.getMobileNo(),
                payBillRequest.getTransactionId(),
                user.getUserId()
        );
    }


}
