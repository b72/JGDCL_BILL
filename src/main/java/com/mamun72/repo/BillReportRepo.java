package com.mamun72.repo;

import com.mamun72.dto.BranchWiseCollectionReport;
import com.mamun72.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BillReportRepo extends JpaRepository<Bill, String> {


    @Transactional
    @Modifying
    @Query("select new com.mamun72.dto.BranchWiseCollectionReport(b.customerId, b.customerName, " +
            "b.jsdclTrxId, b.paybleAmount, b.billAmount, b.surcharge, " +
            "b.stampCharge, b.paidAmount, b.paidAt, b.paidBy, u.brName, u.userName, b.status) from Bill b join b.user u")
    List<BranchWiseCollectionReport> getAll();
}
