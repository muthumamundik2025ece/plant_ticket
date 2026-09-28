package com.cropadvisor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.datasource.url=jdbc:h2:mem:cropadvisor;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
class CropAdvisorApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void servesLandingPage() throws Exception {
		var response = restTemplate.getForEntity("/", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType())
				.isNotNull()
				.matches(contentType -> contentType.isCompatibleWith(MediaType.TEXT_HTML));
		assertThat(response.getBody()).contains("CropAdvisor");
	}
}