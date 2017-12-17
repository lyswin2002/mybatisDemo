package com.demo.service;/**
 * Created by Administrator on 2017/12/17.
 */

import com.demo.dao.CityMapper;
import com.demo.model.City;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 廖永生
 * @create 2017-12-17 16:47
 * @decription
 */
@Service
public class CityService {

    @Resource
    private CityMapper cityMapper;

    public City getCity(Integer id){
        return cityMapper.selectByPrimaryKey(id);
    }

    public void update(City city){
        cityMapper.insertSelective(city);
    }
}
