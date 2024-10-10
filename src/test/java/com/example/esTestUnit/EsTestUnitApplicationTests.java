package com.example.esTestUnit;

import com.example.esTestUnit.controller.UserController;
import com.example.esTestUnit.entities.User;
import com.example.esTestUnit.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

	private static Long changingId = 1L;

	@Autowired
	private UserController userController;

	@Autowired
	private UserServices userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void controllerLoads() {
		assertThat(userController).isNotNull();
	}

	@Test
	void serviceLoads() {
		assertThat(userService).isNotNull();
	}

	@Test
	void createTest() throws Exception {
		User user = new User(changingId, "Mario", "Rossi", 18, true);
		String userJSON = objectMapper.writeValueAsString(user);

		MvcResult result = mockMvc.perform(post("/user/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		changingId++;
	}

	@Test
	void readAllTest() throws Exception {
		createTest();

		MvcResult result = mockMvc.perform(get("/user/readAll"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<User> responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(responseUser.size()).isNotZero();
	}

	@Test
	void readTest() throws Exception {
		Long id = changingId;
		createTest();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/read/{id}", id))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
				.andReturn();

		User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(responseUser.getId()).isEqualTo(id);
	}

	@Test
	void updateTest() throws Exception {
		Long id = changingId;
		createTest();

		User updatedUser = new User(id, "Michele", "Angeletti", 25, false);
		String userJSON = objectMapper.writeValueAsString(updatedUser);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/change/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(responseUser.getId()).isEqualTo(id);
	}

	@Test
	void updateWorkingTest() throws Exception {
		Long id = changingId;
		createTest();

		boolean isActive = false;

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user/setActive/{id}",id)
						.param("active",String.valueOf(isActive)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(responseUser.getId()).isEqualTo(id);
	}

	@Test
	void deleteTest() throws Exception {
		Long id = changingId;
		createTest();

		mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/{id}",id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

}
