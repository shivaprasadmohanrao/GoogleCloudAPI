package com.wf.hackathon2022.googlecloudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author satpra
 * This class helps to get wave/mp3 file generated using GCP TextToSpeech cloud API.
 * We have to use GCP Credentials as env variable in Run configurations
 * Enable the TTS api in GCP console and download the API credentials
 * 
 *
 */

@SpringBootApplication
public class TtsdemoApplication{ //implements CommandLineRunner {
 
    public static void main(String[] args) {
        //SpringApplication app = new SpringApplication(TtsdemoApplication.class);
        SpringApplication.run(TtsdemoApplication.class, args);
    }
 
}
