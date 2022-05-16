package com.wf.hackathon2022.googlecloudapi;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wf.hackathon2022.googlecloudapi.controller.GoogleCloudAPIController;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GoogleCloudAPIController.class)
public class GoogleCloudAPIControllerTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testGetPhoneticOkStatus() throws Exception {
		
		
		mockMvc.perform(get("/getPhonetic?name=Shiva")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetPhoneticHello() throws Exception {
		
		
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetPhoneticStatus() throws Exception {
		
		//content().string("[ sh ih v aa ]")
		MvcResult result =  mockMvc.perform(get("/getPhonetic?name=Shiva")).andReturn();
		String resultVal = result.getResponse().getContentAsString();
		System.out.println("resultVal" + resultVal);
		assertEquals("[ sh  ih v aa ]", resultVal);
		
	}
	@Test
	public void testGetPhoneticFailure() throws Exception {
		
		//content().string("[ sh ih v aa ]")
		MvcResult result =  mockMvc.perform(get("/getPhonetic?name=Shiva")).andReturn();
		String resultVal = result.getResponse().getContentAsString();
		System.out.println("resultVal" + resultVal);
		assertNotEquals("[ sh  ih v a ]", resultVal);
		
	}
	@Test
	public void testGetPhoneticException() throws Exception {
		
		//content().string("[ sh ih v aa ]")
		MvcResult result =  mockMvc.perform(get("/getPhonetic?name=ShivaPrasad%20Mohanrao")).andReturn();
		String resultVal = result.getResponse().getContentAsString();
		System.out.println("resultVal" + resultVal);
		assertNotEquals("[ sh  ih v a ]", resultVal);
		
	}
	
	@Test
	public void testGetPhoneticBadGAteway() throws Exception {
		mockMvc.perform((get("/getPhonetic?name=Sunil"))).andExpect(status().isOk()).andExpect(content().contentType("text/plain;charset=UTF-8"));
	}
	
	@Test
	public void testGetSpeech() throws Exception {
		mockMvc.perform((MockMvcRequestBuilders.post("/getSpeech"))).andExpect(status().isUnsupportedMediaType());
	}
	
	@Test
	public void testGetSpeechSuc() throws Exception {
		
		mockMvc.perform((MockMvcRequestBuilders.post("/getSpeech").contentType(MediaType.APPLICATION_JSON).
				content("{\n"
						+ "    \"empId\":\"2012682\",\n"
						+ "    \"textStr\":\"Padmaja Mohanrao\",\n"
						+ "    \"countryCode\" : \"en-IN\"\n"
						+ "}"))).
		andExpect(status().isOk());
	}
	

}
