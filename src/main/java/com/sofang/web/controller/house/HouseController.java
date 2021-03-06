package com.sofang.web.controller.house;

import com.sofang.base.ResponseEntity;
import com.sofang.base.ServiceMultiResult;
import com.sofang.base.StatusCode;
import com.sofang.service.AddressService;
import com.sofang.web.dto.SubwayDTO;
import com.sofang.web.dto.SubwayStationDTO;
import com.sofang.web.dto.SupportAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by gegf
 */
@Controller
public class HouseController {

    @Autowired
    private AddressService addressService;

    /**
     * 获取所有支持的城市
     * @return
     */
    @ResponseBody
    @GetMapping("address/support/cities")
    public ResponseEntity getSupportCities(){
        ServiceMultiResult<SupportAddressDTO> result = addressService.findAllCities();
        if(result.getResultSize() == 0){
            return ResponseEntity.createByErrorCodeMessage(StatusCode.NOT_FOUND);
        }
        return ResponseEntity.createBySuccess(result.getResult());
    }

    /**
     * 获取指定城市下的区/县
     * @param cityEnName
     * @return
     */
    @ResponseBody
    @RequestMapping("address/support/regions")
    public ResponseEntity getSupportRegions(@RequestParam(name="city_name")String cityEnName){
        ServiceMultiResult<SupportAddressDTO> addressResult = addressService.findAllRegionByCityName(cityEnName);
        if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
            return ResponseEntity.createByErrorCodeMessage(StatusCode.NOT_FOUND);
        }
        return ResponseEntity.createBySuccess(addressResult.getResult());
    }

    /**
     * 获取具体城市所支持的地铁线路
     * @param cityEnName
     * @return
     */
    @GetMapping("address/support/subway/line")
    @ResponseBody
    public ResponseEntity getSupportSubwayLine(@RequestParam(name = "city_name") String cityEnName) {
        List<SubwayDTO> subways = addressService.findAllSubwayByCity(cityEnName);
        return ResponseEntity.createBySuccess(subways);
    }

    /**
     * 获取对应地铁线路所支持的地铁站点
     * @param subwayId
     * @return
     */
    @GetMapping("address/support/subway/station")
    @ResponseBody
    public ResponseEntity getSupportSubwayStation(@RequestParam(name = "subway_id") Long subwayId) {
        List<SubwayStationDTO> stationDTOS = addressService.findAllStationBySubway(subwayId);
        if (stationDTOS.isEmpty()) {
            return ResponseEntity.createByErrorCodeMessage(StatusCode.NOT_FOUND);
        }

        return ResponseEntity.createBySuccess(stationDTOS);
    }

}
