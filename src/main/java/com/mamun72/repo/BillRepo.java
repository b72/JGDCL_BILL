package com.mamun72.repo;


import com.mamun72.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BillRepo extends JpaRepository<Bill,Long> {


    @Transactional
    @Modifying
    @Query("update Bill b set b.status = :status, b.jsdclTrxId = :jsdclTrxId" +
            ", b.paidAmount = :paidAmount, b.mobileNo = :mobileNo, " +
            "b.paidAt = current_timestamp, b.paidBy = :paidBy, b.branchCode = :branchCode where b.transactionId = :id")
    int payBill(
            @Param("jsdclTrxId") String apiTrxId,
            @Param("paidAmount") Double paidAmount,
            @Param("status") Integer status,
            @Param("mobileNo") String mobileNo,
            @Param("id") String id,
            @Param("paidBy") String paidBay,
            @Param("branchCode") String branchCode
            );
}
