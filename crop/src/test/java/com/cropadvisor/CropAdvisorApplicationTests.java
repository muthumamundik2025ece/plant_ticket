package com.cropadvisor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import com.cropadvisor.entity.Crop;
import com.cropadvisor.entity.Farmer;
import com.cropadvisor.entity.Officer;
import com.cropadvisor.entity.Region;
import com.cropadvisor.entity.Ticket;
import com.cropadvisor.entity.TicketResponse;
import com.cropadvisor.entity.TicketStatus;
import com.cropadvisor.entity.TicketStatusHistory;
import com.cropadvisor.repository.CropRepository;
import com.cropadvisor.repository.FarmerRepository;
import com.cropadvisor.repository.OfficerRepository;
import com.cropadvisor.repository.RegionRepository;
import com.cropadvisor.repository.TicketRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.datasource.url=jdbc:h2:mem:cropadvisor;MODE=MySQL;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
class CropAdvisorApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private FarmerRepository farmerRepository;

	@Autowired
	private OfficerRepository officerRepository;

	@Autowired
	private CropRepository cropRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Test
	void servesLandingPage() throws Exception {
		var response = restTemplate.getForEntity("/", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType())
				.isNotNull()
				.matches(contentType -> contentType.isCompatibleWith(MediaType.TEXT_HTML));
		assertThat(response.getBody()).contains("CropAdvisor");
	}

	@Test
	@Transactional
	void persistsTicketRelationshipsResponsesAndStatusHistory() {
		Region region = regionRepository.save(new Region("North Valley", "NV", "Northern farms"));
		Farmer farmer = farmerRepository.save(new Farmer("Asha Farmer", "asha@example.test", "555-0101", region));
		Officer officer = officerRepository.save(new Officer("OFF-001", "Sam Officer", "sam@example.test", null, region));
		Crop crop = cropRepository.save(new Crop("Maize", "Sweet", "Sweet corn"));

		Ticket ticket = new Ticket(farmer, crop, region, "Yellowing lower leaves", "Leaves yellowed after heavy rain.");
		ticket.setAssignedOfficer(officer);
		ticket.setPhotoReference("uploads/ticket-photo.jpg");
		ticket.setStatus(TicketStatus.ASSIGNED);
		ticket.setEscalationLevel(1);
		ticket.addResponse(new TicketResponse(null, officer, "Please check field drainage."));
		ticket.addStatusHistory(new TicketStatusHistory(TicketStatus.NEW, TicketStatus.ASSIGNED, null, officer, "Assigned to regional officer"));

		Ticket savedTicket = ticketRepository.saveAndFlush(ticket);

		assertThat(savedTicket.getId()).isNotNull();
		assertThat(savedTicket.getTicketNumber()).isNotBlank();
		assertThat(savedTicket.getFarmer().getId()).isEqualTo(farmer.getId());
		assertThat(savedTicket.getCrop().getId()).isEqualTo(crop.getId());
		assertThat(savedTicket.getRegion().getId()).isEqualTo(region.getId());
		assertThat(savedTicket.getAssignedOfficer().getId()).isEqualTo(officer.getId());
		assertThat(savedTicket.getResponses()).hasSize(1);
		assertThat(savedTicket.getStatusHistory()).hasSize(1);
	}
}