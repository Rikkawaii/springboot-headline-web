package com.atguigu.controller;

import com.atguigu.mapper.TypeMapper;
import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.service.HeadlineService;
import com.atguigu.service.TypeService;
import com.atguigu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("portal")
@CrossOrigin
public class PortalController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private HeadlineService headlineService;

    /**
     * 在首页左上方显示出新闻类别以供选择
     * @return
     */
    @GetMapping("findAllTypes")
    public Result findAllTypes(){
        Result result = typeService.findAllTypes();
        return result;
    }
    //url地址：portal/findNewsPage
    //
    //请求方式：post
    //
    //请求参数:
    //
    //```JSON
    //{
    //    "keyWords":"马斯克", // 搜索标题关键字
    //    "type":0,           // 新闻类型
    //    "pageNum":1,        // 页码数
    //    "pageSize":10     // 页大小
    //}
    //```
    @PostMapping("findNewsPage")
    public Result findNewsPage(@RequestBody PortalVo portalVo){
        Result result = headlineService.findNewsPage(portalVo);
        return result;
    }
    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(@RequestParam Integer hid){
        Result result = headlineService.showHeadlineDetail(hid);
        return result;
    }
}
