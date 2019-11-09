package com.rest.mintos.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static org.springframework.beans.BeanUtils.copyProperties;
import com.rest.mintos.api.beans.ForecastBean;
import com.rest.mintos.domain.Forecast;

@Component
public class ForecastConverter implements Converter<Forecast, ForecastBean> {

    @Override
    public ForecastBean convert(Forecast forecast) {
        ForecastBean forecastBean = new ForecastBean();
        copyProperties(forecast, forecastBean);
        return forecastBean;
    }
}

