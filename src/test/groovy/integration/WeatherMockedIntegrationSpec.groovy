package integration

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.client.RestTemplate

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.rest.mintos.MintosApplication
import com.rest.mintos.api.beans.ForecastBean
import com.rest.mintos.api.beans.WeatherResponseBean

import configuration.TestConfiguration
import spock.lang.Specification

@SpringBootTest(classes = MintosApplication.class, webEnvironment = RANDOM_PORT)
@Import(TestConfiguration)
@DirtiesContext
class WeatherMockedIntegrationSpec extends Specification {

    MockRestServiceServer mockServer

    @Autowired
    RestTemplate restTemplate

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper mapper

    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    def 'should request weather and location from mocked providers'() {
        given:
            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.ipapi.com/195.12.122.23?access_key=87b4dcb1ba89d678c8741400da06a552')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['latitude': 51.16667175292969, 'longitude': 71.44999694824219]))
                )

            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.openweathermap.org/data/2.5/weather?lat=51.17&lon=71.45&appid=82a8c56f34d2d0125dbb349d361fb31c')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['name': 'Astana', 'weather': [['main': 'Clouds']] as List]))
                )
        when:
            WeatherResponseBean weatherResponseBean = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get('/weather')
                .contentType(APPLICATION_JSON_VALUE).header('X-FORWARDED-FOR', '195.12.122.23'))

                .andExpect(status().isOk()).andReturn()
                .response.contentAsString, WeatherResponseBean)

        then:
            weatherResponseBean.success
            weatherResponseBean.status == 'Actual Response from provider'
            weatherResponseBean.getForecast().get('name') == 'Astana'
            !((ArrayList) weatherResponseBean.getForecast().get('weather')).isEmpty()
    }

    def 'should get historical data by ip'() {
        given:
            String clientIp = '195.12.122.24'
            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.ipapi.com/' + clientIp + '?access_key=87b4dcb1ba89d678c8741400da06a552')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['latitude': 50.40, 'longitude': 71.22]))
                )

            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.openweathermap.org/data/2.5/weather?lat=50.40&lon=71.22&appid=82a8c56f34d2d0125dbb349d361fb31c')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['name': 'Astana', 'weather': [['main': 'Clouds']] as List]))
                )
        when:
            mockMvc.perform(MockMvcRequestBuilders.get('/weather')
                .contentType(APPLICATION_JSON_VALUE).header('X-FORWARDED-FOR', clientIp))

            List<ForecastBean> forecastBeans = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get('/historical/weather/' + clientIp)
                .contentType(APPLICATION_JSON_VALUE))

                .andExpect(status().isOk()).andReturn()
                .response.contentAsString, new TypeReference<List<ForecastBean>>() {})

        then:
            forecastBeans.size() == 1
            ForecastBean result = forecastBeans.get(0)
            result.clientIp == clientIp
            result.latitude == 50.40
            result.longitude == 71.22
    }

    def 'should get historical data by coordinates'() {
        given:
            String clientIp = '195.12.122.25'
            BigDecimal latitude = 30.40
            BigDecimal longitude = 40.40
            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.ipapi.com/' + clientIp + '?access_key=87b4dcb1ba89d678c8741400da06a552')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['latitude': latitude, 'longitude': longitude]))
                )

            mockServer.expect(ExpectedCount.once(),
                requestTo(new URI('http://api.openweathermap.org/data/2.5/weather?lat=' + latitude + '&lon=' + longitude + '&appid=82a8c56f34d2d0125dbb349d361fb31c')))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(['name': 'Astana', 'weather': [['main': 'Clouds']] as List]))
                )
        when:
            mockMvc.perform(MockMvcRequestBuilders.get('/weather')
                .contentType(APPLICATION_JSON_VALUE).header('X-FORWARDED-FOR', clientIp))

            List<ForecastBean> forecastBeans = mapper.readValue(mockMvc.perform(MockMvcRequestBuilders.get('/historical/weather/' + latitude + '/' + longitude)
                .contentType(APPLICATION_JSON_VALUE))

                .andExpect(status().isOk()).andReturn()
                .response.contentAsString, new TypeReference<List<ForecastBean>>() {})

        then:
            forecastBeans.size() == 1
            ForecastBean result = forecastBeans.get(0)
            result.clientIp == clientIp
            result.latitude == latitude
            result.longitude == longitude
    }

}
