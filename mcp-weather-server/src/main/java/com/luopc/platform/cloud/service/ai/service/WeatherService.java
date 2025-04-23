package com.luopc.platform.cloud.service.ai.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luopc.platform.cloud.service.ai.domain.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class WeatherService {

    private static final String API_URL = "https://cn.apihz.cn/api/tianqi/tqyb.php";
    private final OkHttpClient client = new OkHttpClient();

    @Tool(description = "根据省份+城市名称获取天气预报")
    public String getWeatherByCity(String province, String city) {
//        Map<String, String> mockData = Map.of(
//                "西安", "晴天",
//                "北京", "小雨",
//                "上海", "大雨"
//        );
        WeatherResponse weather = getWeather(province, city);
        return Objects.nonNull(weather) ? weather.toString() : "抱歉：未查询到对应城市！";
    }

    public WeatherResponse getWeather(String province, String city) {
        // 构建请求URL（注意URL编码）
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(API_URL)).newBuilder()
                .addQueryParameter("id", "88888888") // 替换实际ID
                .addQueryParameter("key", "88888888") // 替换实际KEY
                .addQueryParameter("sheng", URLEncoder.encode(province, StandardCharsets.UTF_8))
                .addQueryParameter("place", URLEncoder.encode(city, StandardCharsets.UTF_8))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Unexpected code: {}", response);
                return null;
            }
            String bodyResult = response.body().string();
            log.info("Response: {}", bodyResult);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            return mapper.readValue(bodyResult, WeatherResponse.class);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            return null;
        }
    }

    // 使用示例：
    public static void main(String[] args) {
        WeatherService service = new WeatherService();
        WeatherResponse weather = service.getWeather("四川", "绵阳");
        System.out.println("当前温度：" + weather.getTemperature() + "℃");
        System.out.println("风力等级：" + weather.getWindScale());
        System.out.println("风向：" + weather.toCnString());
    }
}
