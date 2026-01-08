package com.luopc.platform.cloud.service.ai.resource;

import com.luopc.platform.cloud.service.ai.domain.dto.WeatherResponse;
import com.luopc.platform.cloud.service.ai.service.WeatherService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class McpServiceResource {

    @Resource
    private WeatherService weatherService;

    @Tool(name = "getWeatherByCity" , description = "根据省份+城市名称获取天气预报")
    public String getWeatherByCity(String province, String city) {
        WeatherResponse weather = weatherService.getWeather(province, city);
        return Objects.nonNull(weather) ? weather.toString() : "抱歉：未查询到对应城市！";
    }

}
