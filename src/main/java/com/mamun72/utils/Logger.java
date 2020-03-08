package com.mamun72.utils;

import com.mamun72.entity.ApiLog;
import com.mamun72.repo.ApiLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logger {

    @Autowired
    ApiLogRepo apiLogRepo;

    public void keepApiLog(ApiLog apiLog) {
        apiLogRepo.save(apiLog);
    }
}
