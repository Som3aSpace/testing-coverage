package com.som3a.springboottesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.som3a.springboottesting.controller.CourseController;
import com.som3a.springboottesting.entity.Course;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpringBootTestingApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(CourseController.class)
				.build();
	}


	@Test
	public void addNewCourseTest() throws Exception {
		//build request body
		Course course = Course.builder()
				.name("test-course")
				.price(100)
				.duration("1 month")
				.build();
		//call controller endpoints
		mockMvc.perform(MockMvcRequestBuilders
						.post("/courses")
						.contentType("application/json")
						.content(asJsonString(course))
						.accept("application/json"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}


	@Test
	public void getAllTheCoursesTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get("/courses")
						.accept("application/json")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(10));
	}

	private String asJsonString(Object object) {
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
