package com.mamun72.repo;


import com.mamun72.entity.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepo extends CrudRepository<Bill,Long> {
}
