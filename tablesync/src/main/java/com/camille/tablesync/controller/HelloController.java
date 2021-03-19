

package com.camille.tablesync.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @FileName: HelloController.java
 * @Description: HelloController.java类说明
 * @Author: kang.peng
 * @Date: 2021/3/19 15:09
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
