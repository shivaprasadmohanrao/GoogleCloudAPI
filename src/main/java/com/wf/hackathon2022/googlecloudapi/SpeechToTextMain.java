package com.wf.hackathon2022.googlecloudapi;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

//Google Speech to text API is consumed to get audio transcribed
public class SpeechToTextMain {

	public static void main(String... args) throws Exception {
		//send fileName of the audio as string url of local storage
		generateText("/Users/satpra/Downloads/ttsdemo/2012682.wav");
		
	}
	
	/**
	 *
	 * @param fileName the path to a audio file to transcribe.
	 */
	public static void generateText(String fileName) throws Exception {
	  // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS, need ENV VAr set up with google API credentials
	  try (SpeechClient speech = SpeechClient.create()) {

	    Path path = Paths.get(fileName);
	    byte[] data = Files.readAllBytes(path);
	    ByteString audioBytes = ByteString.copyFrom(data);

	    RecognitionConfig config =
	        RecognitionConfig.newBuilder()
	            .setEncoding(AudioEncoding.LINEAR16)
	            .setLanguageCode("en-IN")
	            .setSampleRateHertz(24000) // some times we can try 16000 also 
	            .build();
	    RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

	    // Use non-blocking call for getting file transcription
	    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
	        speech.longRunningRecognizeAsync(config, audio);

	    while (!response.isDone()) {
	      Thread.sleep(10000); // we can reduce , depends on api response and speed of network some times
	    }

	    List<SpeechRecognitionResult> results = response.get().getResultsList();

	    for (SpeechRecognitionResult result : results) {
	      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	      System.out.println("===========================***********==================");
	      System.out.println("      Transcription: "+ alternative.getTranscript());
	      System.out.println("===========================***********==================");
	    }
	  }
	}
}

