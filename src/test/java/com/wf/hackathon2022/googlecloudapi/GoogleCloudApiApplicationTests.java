package com.wf.hackathon2022.googlecloudapi;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

class GoogleCloudApiApplicationTests {
	
	@MockBean
	private SpringApplication sa;
	
	
	@Test
	void contextLoads() {
	}
	
//	@Test
//	public void main() {
//		Mockito.when(sa.run(Mockito.any(String.class))).thenReturn(null);
//		GoogleCloudApiApplication.main(new String[] {});
//	}

}
