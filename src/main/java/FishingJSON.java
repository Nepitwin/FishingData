
import java.io.FileWriter;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andy
 */
public class FishingJSON {

    private static DefaultHttpClient httpClient;

    public FishingJSON() {
    }

    public void fishJSON() throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpGet httpget = new HttpGet("https://api.magicthegathering.io/v1/cards");

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                }
            };

            String responseBody = httpclient.execute(httpget, responseHandler);
            
            JSONObject object = new JSONObject(responseBody);
            JSONArray array = object.getJSONArray("cards");
            
            for(int i = 0 ; i < array.length(); i++) {
                
                // try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter(i + ".json")) {
                    file.write(array.getJSONObject(i).toString());
                    file.close();
		}
                
                System.out.println(array.getJSONObject(i).getString("imageUrl"));
            }
        } finally {
            httpclient.close();
        }
    }
}
