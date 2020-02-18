package com.mamun72.repo;

import com.mamun72.entity.ApiLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepo extends CrudRepository<ApiLog, String> {
}
