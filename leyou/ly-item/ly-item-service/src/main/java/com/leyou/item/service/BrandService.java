package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author longhai
 * @date 2019/3/7 - 10:11
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example=new Example(Brand.class);
        if(StringUtils.isNoneBlank(key)){
            //过滤条件
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());

        }

        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }

        //查询

        List<Brand> list= brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)){
            throw  new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页的结果
        PageInfo<Brand> info=new PageInfo<>(list);

        return new PageResult<>(info.getTotal(),list);
    }
}
