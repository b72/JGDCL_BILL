package com.mamun72.service;

import com.mamun72.billarApi.Jgdl.POJO.PayBillRequest;
import com.mamun72.dto.BranchWiseCollectionReport;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.repo.BillRepo;
import com.mamun72.repo.BillReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.util.List;

@Service
public class BillPayService {
    @Autowired
    private BillRepo billRepo;

    @Autowired
    BillReportRepo billReportRepo;

    @Autowired
    EntityManager entityManager;

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
                user.getUserId(),
                user.getBrCode()
        );
    }

    public List getBillReportRepo() {
        return billReportRepo.getAll();
    }


    public List getBillByBranchStatus(String fromDate, String toDate, String branchCode, int status) throws ParseException {
        String queryStr =
                "select new com.mamun72.dto.BranchWiseCollectionReport(b.customerId, b.customerName, " +
                        "b.jsdclTrxId, b.paybleAmount, " +
                        "b.billAmount, b.surcharge, " +
                        "b.stampCharge, b.paidAmount, " +
                        "b.paidAt, b.paidBy, " +
                        "u.brName, u.userName, b.status) " +
                        "from Bill as b " +
                        "join b.user as u "+
                        "where u.brCode = :branchCode " +
                        "and b.status = :status " +
                        "and TRUNC(b.paidAt) between TO_DATE(:fromDate, 'DD-MM-YY') and  TO_DATE(:toDate, 'DD-MM-YY')";
        TypedQuery<BranchWiseCollectionReport> query =
                entityManager.createQuery(queryStr, BranchWiseCollectionReport.class);
        List <BranchWiseCollectionReport> results = query
                .setParameter("branchCode", branchCode)
                .setParameter("status", status)
                .setParameter("fromDate",fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
        return results;
    }

    public List getBillByBranchStatus(String fromDate, String toDate, String branchCode) throws ParseException {

        String queryStr =
                "select new com.mamun72.dto.BranchWiseCollectionReport(b.customerId, b.customerName, " +
                        "b.jsdclTrxId, b.paybleAmount, " +
                        "b.billAmount, b.surcharge, " +
                        "b.stampCharge, b.paidAmount, " +
                        "b.paidAt, b.paidBy, " +
                        "u.brName, u.userName, b.status) " +
                        "from Bill as b " +
                        "join b.user as u "+
                        "where u.brCode = :branchCode " +
                        "and TRUNC(b.paidAt) between TO_DATE(:fromDate, 'DD-MM-YY') and  TO_DATE(:toDate, 'DD-MM-YY')";
        TypedQuery<BranchWiseCollectionReport> query =
                entityManager.createQuery(queryStr, BranchWiseCollectionReport.class);
        List <BranchWiseCollectionReport> results = query
                .setParameter("branchCode", branchCode)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
        return results;
    }
}
