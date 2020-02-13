package com.mamun72.service;

import com.mamun72.entity.ApiLog;
import com.mamun72.repo.ApiLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiLogService {
    @Autowired
    private ApiLogRepo apiLogRepo;



    public ApiLog saveLog(ApiLog apiLog){
          return apiLogRepo.save(apiLog);
    }

}
