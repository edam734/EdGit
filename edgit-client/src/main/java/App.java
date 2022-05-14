import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.edgit.commons.network.NetworkFileUtils;

public class App {

	static final String UPLOAD_URL = "http://localhost:8082/ReceiveFile";
	static final String FILE_PATH = "/";
	// static final int BUFFER_SIZE = 2048;

	public static void main(String[] args) throws IOException {
		String filename = "rfc1867.txt";
		File uploadFile = new File(FILE_PATH + filename);

		System.out.println("File to upload: " + filename);

		// creates a HTTP connection
		URL url = new URL(UPLOAD_URL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true);
		httpConn.setRequestMethod("POST");
		// sets file name as a HTTP header
		httpConn.setRequestProperty("fileName", uploadFile.getName());

		// opens output stream of the HTTP connection for writing data
		OutputStream outputStream = httpConn.getOutputStream();

//		NetworkFileUtils.send(new ObjectOutputStream(outputStream), uploadFile);
	
		System.out.println("Data was written.");
		outputStream.close();

		// always check HTTP response code from server
		int responseCode = httpConn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			// reads server's response
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String response = reader.readLine();
			System.out.println("Server's response: " + response);
		} else {
			System.out.println("Server returned non-OK code: " + responseCode);
		}
	}
}
