package integration

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import com.fasterxml.jackson.databind.ObjectMapper
import com.rest.mintos.MintosApplication
import com.rest.mintos.api.beans.WeatherResponseBean

import configuration.TestConfiguration
import spock.lang.Specification

@SpringBootTest(classes = MintosApplication.class, webEnvironment = RANDOM_PORT)
@Import(TestConfiguration)
class WeatherIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper mapper

    def 'should request weather and location from real providers'() {
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

}
