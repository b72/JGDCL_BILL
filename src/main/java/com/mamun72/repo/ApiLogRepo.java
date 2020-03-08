package com.mamun72.repo;

import com.mamun72.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepo extends JpaRepository<ApiLog, String> {
}
