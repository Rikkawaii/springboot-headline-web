package com.atguigu.service.impl;

import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.mapper.HeadlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author xwzy
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-02-27 22:46:49
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{
    @Autowired
    private HeadlineMapper headlineMapper;
    @Autowired
    private JwtHelper jwtHelper;
    /**
     *
     * @param portalVo
     * @return
     */
    @Override
    public Result findNewsPage(PortalVo portalVo) {
        IPage<Map> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        headlineMapper.selectPageMap(page,portalVo);
        //把封装到page里的数据取出来
        List<Map> records = page.getRecords();
        Map pageInfo = new HashMap<>();
        pageInfo.put("pageData",records);
        pageInfo.put("pageNum",page.getCurrent());//当前页码
        pageInfo.put("pageSize",page.getSize());//每页记录数
        pageInfo.put("totalPage",page.getPages());//总页数
        pageInfo.put("totalSize",page.getTotal());//总记录数
        Map<String, Object> data = new HashMap<>();
        data.put("pageInfo",pageInfo);
        return Result.ok(data);
    }

    /**
     * 查询头条详情
     * @param hid 头条id
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {
        //查询头条详情,并把结果放入data中
        Map headlineValue = headlineMapper.selectDetailMap(hid);
        Map<String,Map> data = new HashMap<>();
        data.put("headline",headlineValue);
        //更新头条状态存入数据库（浏览量+1）
        Headline headline = new Headline();
        headline.setHid((Integer) headlineValue.get("hid"));
        headline.setVersion((Integer) headlineValue.get("version"));
        headline.setPageViews((Integer) headlineValue.get("pageViews")+1);
        headlineMapper.updateById(headline);

        return Result.ok(data);
    }

    @Override
    public Result publish(Headline headline, String token) {
        //根据token获得发布者
        int uid = jwtHelper.getUserId(token).intValue();
        //把headline信息存入数据库
        headline.setPublisher(uid);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());
        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    @Override
    public Result findHeadlineByHid(Integer hid) {
        Headline headline = new Headline();
        headline = getById(hid);
        headline.setUpdateTime(null);
        headline.setPublisher(null);
        headline.setCreateTime(null);
        Map data = new HashMap<>();
        data.put("headline",headline);
        return Result.ok(data);
    }
    @Override
    public Result updateData(Headline headline) {
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        headline.setVersion(version);
        headline.setUpdateTime(new Date());
        headlineMapper.updateById(headline);
        return Result.ok(null);
    }
}




