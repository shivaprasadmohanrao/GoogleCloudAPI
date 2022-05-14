package com.wf.hackathon2022.googlecloudapi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import com.wf.hackathon2022.googlecloudapi.model.TTSRequest;
import com.wf.hackathon2022.googlecloudapi.model.TTSResponse;

/**
 * @author satpra
 * @apiNote
 * Controller class which communicates with GCP TTS API to generate speech for the given text string, in this case a name.
 * ENV_VAR in run configurations is set with Google Cloud Credentials json file with api key and other details
 * @version 1.0
 * @implSpec - this controller has one end point mapping as /getSpeech which uses application/json as the type in api communication.
 * Using ngrok exposing the localhost:<port> to public over the internet. 
 */
@RestController
public class GoogleCloudAPIController {

	/*
	 * @param empId --> user employee id.
	 * @param textStr --> text string to which speech needs to be generated.
	 * @param countryCode --> country code would help decide the ascent of tts generated.
	 * */
	@PostMapping(path="/getSpeech",produces="application/json", consumes="application/json")
	public ResponseEntity<TTSResponse> getSpeech(@RequestBody TTSRequest ttsRequest) throws Exception {
		//user employee ID
		String empId = ttsRequest.getEmpId();
		//text to which speech needs to be generated
		String textStr = ttsRequest.getTextStr();
		//country code ex: en-US, en-IN
		String countryCode = ttsRequest.getCountryCode();
		
		//System.out.println("empId " + empId);
		
		getTTS(empId,textStr,countryCode);
		
		//parse and formulate the response object
		TTSResponse response = new TTSResponse();
		response.setUrlAudio("http://db57-49-207-224-149.ngrok.io/" + empId + ".wav");
		return new ResponseEntity<TTSResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping(path="/getPhonetic")
	public ResponseEntity<String> getPhonetic(@RequestParam(name = "name") String name){
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject("https://pronouncenames.com/search?name="+name, String.class);
		
		//For the demo, we did not find any API out of the box to get Phonetic spelling, so using pronouncenames.com, intercepted api, to fetch the html response.
		//we would need to parse html and fetch exactly the required phonetic spelling, but for demo, just using vague string parsing to get value
		//idea here is, to go through html as a string, which is costly operation, and find for the word Phonetic Spelling, find the index of that word, scan to find next [ & ], 
		//extract the spelling as a string. We can definitely look later to make it lot better on this.
		int PhoneticSpelling = response.indexOf("Phonetic Spelling")+17;
		//System.out.println("PhoneticSpelling " + PhoneticSpelling);
		int first_Square = response.indexOf("[", PhoneticSpelling);
		//System.out.println("first_Square " + first_Square);
		int last_SquareIndexd = response.indexOf("]", first_Square);
		
		//System.out.println("last_SquareIndexd " + last_SquareIndexd);
		String r = response.substring(first_Square,last_SquareIndexd+1);
		//System.out.println("Phonetic for George is " + r);

		return new ResponseEntity<>(r,HttpStatus.OK);
		
	}
	
	
	
	/*
	 * utility method getTTS helps to connect to TTS API, and convert audio Bytes into wave file. 
	 * Audio file is then moved to classpath to generate http url for audio file.
	 * */
	public void getTTS(String empId, String textStr,String countryCode) throws Exception{

		//input String, which needs to be played out as wave/mp3
		String inputStr = textStr;//"Welcome to wellsfargo telephony servioces";
		//System.out.println("text string "+ countryCode);
		String ttsAudioFile = "/Users/satpra/Downloads/ttsdemo/" + empId + ".wav";
		//System.out.println("TTS Generation Service Started");
		
		//TTS client creation
		try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {
			// Setting the text input to be synthesized
			SynthesisInput in = SynthesisInput.newBuilder().setText(inputStr).build();

			//  the voice request; languageCode = "en_us" or "en_IN", also we can select the Gender of the synthesized voice using SsmlVoiceGender.FEMALE or SsmlVoiceGender.MALE
			VoiceSelectionParams voiceSelParams = VoiceSelectionParams.newBuilder().setLanguageCode(countryCode)
					.setSsmlGender(SsmlVoiceGender.FEMALE).build();

			// the type of audio file we want returned, LINEAR16 is for WAV, MP3 is for mp3 extension
			AudioConfig audioType = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.LINEAR16).build();

			// invoke TTS api, internally it uses gRPC to make a call to GCP TTS API.
			SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(in, voiceSelParams, audioType);

			// Get the audio contents from the response
			ByteString rawAudioOutput = response.getAudioContent();

			// Writing the response to the output file as wav or mp3 based on audioType in AudioConfiguration done in line #54
			try (OutputStream out = new FileOutputStream(ttsAudioFile)) {
				out.write(rawAudioOutput.toByteArray());
				File src = new File(ttsAudioFile);		
				File dest = new File(System.getProperty("user.dir") + "/target/classes/static/" + empId + ".wav");
				System.out.println("getTTS(), dest file: " + dest.getAbsolutePath());
				FileUtils.copyFile(src,dest);

			}
			//System.out.println("TTS Generation Service Completed");
		}


	}
}
