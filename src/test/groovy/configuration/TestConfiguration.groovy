package configuration

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

import com.github.tomakehurst.wiremock.WireMockServer

class TestConfiguration {


    @Value('${testing.server.wiremock.port:4181}')
    private int wireMockServerPort

    @Bean
    MockMvc testingMockMvc(WebApplicationContext webApplicationContext) {
        webAppContextSetup(webApplicationContext).build()
    }

   // @Bean(initMethod = 'start', destroyMethod = 'shutdown')
    WireMockServer wireMockServer() {
        new WireMockServer(wireMockConfig().port(wireMockServerPort))
    }
}
