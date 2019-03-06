/*
 * Project Name:demo
 * File Name:ControllerTest
 * Package Name:com.example.demo
 * Date:2019/3/616:06
 * Copyright (c) 2019, LY.com All Rights Reserved.
 */

package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ControllerTest {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "OK";
    }
}
