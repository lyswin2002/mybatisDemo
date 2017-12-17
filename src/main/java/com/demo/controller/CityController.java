package com.demo.controller;/**
 * Created by Administrator on 2017/12/17.
 */

import com.demo.model.City;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.service.CityService;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author 廖永生
 * @create 2017-12-17 16:50
 * @decription
 */
@Controller
public class CityController {

    @Resource
    private CityService cityService;

    @RequestMapping("/getCity")
    @ResponseBody
    public City getCity(@RequestParam int cityId){
        return cityService.getCity(Integer.valueOf(cityId));
    }

    @RequestMapping("/update")
    public ModelAndView updateCity(@RequestParam String name,@RequestParam String  code,@RequestParam String district,@RequestParam int popu){
        City city = new City();
        city.setName(name);
        city.setCountrycode(code);
        city.setDistrict(district);
        city.setPopulation(popu);
        cityService.update(city);
        ModelAndView mv = new ModelAndView();
        mv.addObject("city",city);
        mv.setViewName("index");
        return mv;
    }


}
