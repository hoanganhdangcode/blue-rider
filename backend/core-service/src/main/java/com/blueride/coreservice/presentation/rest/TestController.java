package com.blueride.coreservice.presentation.rest;

import com.blueride.coreservice.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public BaseResponse<String> test() {
        return BaseResponse.success("Core Service is running");
    }
}
