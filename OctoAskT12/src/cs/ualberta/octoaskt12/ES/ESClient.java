package cs.ualberta.octoaskt12.ES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs.ualberta.octoaskt12.Question;
import cs.ualberta.octoaskt12.QuestionArrayList;

public class ESClient {
	private HttpClient httpClient = new DefaultHttpClient();
	
	private Gson gson = new Gson();
	
	public QuestionArrayList getQuestions() throws ClientProtocolException, IOException {
		HttpPost searchRequest = new HttpPost("http://cmput301.softwareprocess.es:8080/cmput301f14t12/_search?size=150");
		
		String SEARCH_USER_FAV = 
				"{\n" +
						   "\"query\":{\n" +
						        "\"match_all\":{}\n" +
						   "}\n" +
				"}";
//				"{\n" +
//					   "\"query\":{\n" +
//					        "\"term\":{\"favoritedUsers\":\"23\"}\n" +
//					   "}\n" +
//				"}";
		
		StringEntity stringEntity = new StringEntity(SEARCH_USER_FAV);
		
		searchRequest.setHeader("Accept", "application/json");
		searchRequest.setEntity(stringEntity);
		
		HttpResponse response = httpClient.execute(searchRequest);
		
		String status = response.getStatusLine().toString();
		System.out.println("Status: " + status);
		
		String json = getEntityContent(response);
		
		Type esResponseType = new TypeToken<ESSearchSearchResponse<Question>>(){}.getType();
		ESSearchSearchResponse<Question> esResponse = gson.fromJson(json, esResponseType);
		
		System.out.println("Response: " + esResponse);
		QuestionArrayList qal = new QuestionArrayList();
		for (ESResponse<Question> r : esResponse.getHits()) {
			Question question = r.getSource();
			System.out.println(question.getTitle());
			qal.addQuestion(question);
		}
		return qal;
//		searchRequest.releaseConnection();
	}
	
	public String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.out.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.out.println("JSON:"+json);
		return json;
	}
}