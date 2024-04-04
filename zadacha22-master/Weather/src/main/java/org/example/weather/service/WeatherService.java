package org.example.weather.service;

import org.example.weather.model.Main;
import org.example.weather.model.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@CacheConfig(cacheNames = "weatherCache", cacheManager = "cacheManager")
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;



    @Value("${url.weather}")
    private String urlWeather;

    @Value("${app_id}")
    private String appId;
    @Cacheable(value = "weatherCache", key = "{#lat, #lon}", unless = "#result == null")
    public Main getWeather(Double lat, Double lon){
        String request = String.format("%s?lat=%s&lon=%s&units=metric&appid=%s",
                urlWeather, lat, lon, appId);
        return restTemplate.getForObject(request, Root.class).getMain();
    }
}

