package com.mamun72.repo;


import com.mamun72.entity.Bill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Repository
public interface BillRepo extends CrudRepository<Bill,Long> {


    @Transactional
    @Modifying
    @Query("update Bill b set b.status = :status, b.jsdclTrxId = :jsdclTrxId" +
            ", b.paidAmount = :paidAmount, b.mobileNo = :mobileNo  where b.transactionId = :id")
    int updateBill(
            @Param("jsdclTrxId") String apiTrxId,
            @Param("paidAmount") Double paidAmount,
            @Param("status") Integer status,
            @Param("mobileNo") String mobileNo,
            @Param("id") String id);
}
