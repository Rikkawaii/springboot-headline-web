package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xwzy
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-02-27 22:46:49
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewsPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result publish(Headline headline, String token);

    Result findHeadlineByHid(Integer hid);

    Result updateData(Headline headline);
}
