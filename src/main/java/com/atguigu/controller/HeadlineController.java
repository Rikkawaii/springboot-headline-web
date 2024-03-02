package com.atguigu.controller;

import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {
    @Autowired
    private HeadlineService headlineService;
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline,@RequestHeader String token){
        Result result = headlineService.publish(headline,token);
        return result;
    }
    //修改头条时回显
    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid){
        Result result = headlineService.findHeadlineByHid(hid);
        return result;
    }
    @PostMapping("update")
    public Result update(@RequestBody Headline headline){
        Result result = headlineService.updateData(headline);
        return result;
    }
    @PostMapping("removeByHid")
    public Result removeByHid(@RequestParam Integer hid){
        headlineService.removeById(hid);
        return Result.ok(null);
    }
}
