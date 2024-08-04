package com.beingAbroad.eduManage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample")
public class sample {

    @PostMapping("/api/v1/{str}")
    public  String sampleController (@PathVariable String str) {
        System.out.println(str);
        return  str;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/a")
    public  String sampleResponse(){
        return "alkdfj";
    }

}
