import java.io.*;
import java.util.*;
import java.net.*;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.scene.*;
import javafx.scene.web.*;
import javafx.application.*;
import javafx.scene.layout.BorderPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
//import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

// This class will connect to the Google Maps API and create a JFrame containing the map contents.

@SuppressWarnings("restriction")
public class GoogleMaps {
	int width; // The width of the frame.
	int height; // The height of the frame.
	String apiURL; // The partial URL used to connect to the API.
	String apiKey; // Required API key to access.
	double latitude; // Latitude of location.
	double longitude; // Longitude of location.
	ArrayList<String> location; // Name and address of the actual location.

	public GoogleMaps(int width, int height, double latitude, double longitude) { // Creates Google Maps object.
		this.apiKey = "";
		this.width = width;
		this.height = height;
		this.latitude = latitude;
		this.longitude = longitude;
		this.apiURL = "https://www.google.com/maps/embed/v1/streetview?key=";
		this.location = new ArrayList<String>();
	}
	
	private void createGoogleMapsURL() {
		apiURL = apiURL + apiKey + "&location=" + latitude + "," + longitude;
	}
	
	private void getAndSaveGoogleMap() { // Gets map information from API and saves embed map into an HTML file.
		String htmlFileName; // Filename of HTML file containing the interactive map.
		String html; // The actual HTML contents.
		File f; // HTML File Object
		PrintWriter pw; // Used to save the HTML.
				
		createGoogleMapsURL(); // Create location-specific Google Maps URL.

		htmlFileName = "google-maps.html";
				
		f = new File(htmlFileName);
		
		html = "<html><iframe "
			+ "width='" + width + "'"
			+ " height='" + height + "'"
			+ " src='" + apiURL + "'"
			+ " allowfullscreen>Your browser does not support iframes.</iframe>"
			+ "<script>document.getElementsByTagName('iframe')[0].document.getElementsByClassName('gm-iv-address')[0].style.display = 'none';</script>"
			+ "</html>";
		
		try {
			pw = new PrintWriter(f);
			pw.println(html);
			pw.close();	
		}
		catch (Exception e) {
			System.out.println("Could not connect to and/or save the Google map.");
					
			e.printStackTrace();
		}
	}
	
	public JPanel createGoogleMapsPanel(Browser browser, BrowserView bv) { // Panel containing map contents.					
		getAndSaveGoogleMap();
								
		JPanel panel = new JPanel(new BorderLayout());		
		panel.add(bv, BorderLayout.CENTER); 

		try {
			File f = new File("google-maps.html"); // File should have been previously created or overwritten.

        	browser.loadURL(f.toURI().toURL().toString()); // This will open the file in the WebView browser.
		}
		catch (Exception e) {
			System.out.println("Could not display the Google map.");
			
			e.printStackTrace();
		}
					
		return panel;	
	}
	
	private String getGoogleMapsPlaceID() {
		String placeIDAPIURL; // Google Places Search Place API URL
		String placeID = ""; // Place ID needed for API.
		int responseCode; // HTTP Request Response Code
		String json; // HTTP Request Response in JSON
				
		placeIDAPIURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" 
		+ latitude + "," + longitude + "&key=" + apiKey + "&radius=100";
			
		// GET Request for Getting Place ID
		
		try {
			URL placeURLObj = new URL(placeIDAPIURL);
			HttpURLConnection connection = (HttpURLConnection)placeURLObj.openConnection();
			
			//System.out.println("Place ID API URL: " + placeIDAPIURL);
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Language", "en-us");
			connection.setRequestProperty("Content-Type", "application/json");
			
			responseCode = connection.getResponseCode();
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Raw input stream from returned response.
			String line; // Line used to read input stream.
			
			StringBuffer response = new StringBuffer(); 
			
			while ((line = inputStream.readLine()) != null) { // Reading in the response from the HTTP request.
				response.append(line);
			}	
					
			json = response.toString(); // Full JSON converted to a string.
			responseCode = connection.getResponseCode();
			
			System.out.println("\nResponse Code (Google Place ID): " + responseCode);
			System.out.println("JSON (Google Place ID): " + json + "\n");
			
			// Parse Place ID JSON
			
			JSONParser parser = new JSONParser();	
			JSONObject jsonObj = (JSONObject)parser.parse(json);
			
			JSONArray resultJSONArr = (JSONArray)jsonObj.get("results"); // Nested JSON object containing the actual results.
			placeID = ((JSONObject)resultJSONArr.get(0)).get("place_id").toString(); 
			
			System.out.println("Place ID: " + placeID);
			
			inputStream.close(); // Close the input stream from the request.
			connection.disconnect(); // Close the URL connection.
		}
		catch (Exception e) {
			System.out.println("HTTP request to get Google Place ID failed.");
			e.printStackTrace();
		}
		
		return placeID;
	}
	
	
	public ArrayList<String> getGoogleMapsPlace() {
		String placeDetailsAPIURL = "https://maps.googleapis.com/maps/api/place/details/"; // Google Places Details API URL	
		String placeID; // Place ID needed for API.
		int responseCode; // HTTP Request Response Code
		String json; // HTTP Request Response in JSON
		
		placeID = getGoogleMapsPlaceID();	
		
		placeDetailsAPIURL = placeDetailsAPIURL + "json?placeid=" + placeID + "&fields=name,formatted_address&key=" + apiKey;
		
		//System.out.println("Place Details API URL: " + placeDetailsAPIURL);
		
		// GET Request for Getting Place Details using Place ID
		
		try {
			URL placeURLObj = new URL(placeDetailsAPIURL);
			HttpURLConnection connection = (HttpURLConnection)placeURLObj.openConnection();
						
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Language", "en-us");
			connection.setRequestProperty("Content-Type", "application/json");
			
			responseCode = connection.getResponseCode();
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Raw input stream from returned response.
			String line; // Line used to read input stream.
			
			StringBuffer response = new StringBuffer(); 
			
			while ((line = inputStream.readLine()) != null) { // Reading in the response from the HTTP request.
				response.append(line);
			}	
					
			json = response.toString(); // Full JSON converted to a string.
			responseCode = connection.getResponseCode();
			
			System.out.println("\nResponse Code (Google Place Details): " + responseCode);
			System.out.println("JSON (Google Place Details): " + json + "\n");
			
			// Parse Place Details JSON
			
			JSONParser parser = new JSONParser();	
			JSONObject jsonObj = (JSONObject)parser.parse(json);
			
			JSONObject resultJSONObj = (JSONObject)jsonObj.get("result"); // Nested JSON object containing the actual results.
			String name = resultJSONObj.get("name").toString(); // Name of the place.
			String address = resultJSONObj.get("formatted_address").toString(); // Address of the place.
			
			location.add(name);
			location.add(address);
			
			inputStream.close(); // Close the input stream from the request.
			connection.disconnect(); // Close the URL connection.
		}
		catch (Exception e) {
			System.out.println("HTTP request to get Google Place Details failed.");
			e.printStackTrace();
		}
		
		return location;
	}
}
