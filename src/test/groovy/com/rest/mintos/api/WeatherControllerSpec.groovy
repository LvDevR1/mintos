package com.rest.mintos.api

import javax.servlet.http.HttpServletRequest

import com.rest.mintos.api.beans.ForecastBean
import com.rest.mintos.api.beans.WeatherResponseBean
import com.rest.mintos.service.WeatherService

import spock.lang.Specification

class WeatherControllerSpec extends Specification {

    static final String IP_ADDRESS = '127.0.0.1'

    WeatherService weatherService = Mock(WeatherService)

    WeatherController controller = new WeatherController(weatherService)

    def 'should get actual weather'() {
        given:
            HttpServletRequest request = Mock(HttpServletRequest)
            WeatherResponseBean bean = new WeatherResponseBean()
            weatherService.getWeatherByIp(request) >> bean
        when:
            WeatherResponseBean result = controller.getWeatherFromProvider(request)
        then:
            result == bean
    }

    def 'should get historical forecasts by ip'() {
        given:
            ForecastBean forecastBean1 = new ForecastBean()
            ForecastBean forecastBean2 = new ForecastBean()
            weatherService.getHistoricalDataByIpAdress(IP_ADDRESS) >> [forecastBean1, forecastBean2]
        when:
            List<ForecastBean> result = controller.getHistoricalDatabyIp(IP_ADDRESS)
        then:
            !result.empty
            result.containsAll([forecastBean1, forecastBean2])
    }

    def 'should get historical forecasts by coordinates'() {
        given:
            ForecastBean forecastBean1 = new ForecastBean()
            ForecastBean forecastBean2 = new ForecastBean()
            weatherService.getHistoricalDataByCoordinates(12.0 , 10.0 ) >> [forecastBean1, forecastBean2]
        when:
            List<ForecastBean> result = controller.getHistoricalDatabyCoordinates(12.0 , 10.0)
        then:
            !result.empty
            result.containsAll([forecastBean1, forecastBean2])
    }
}
