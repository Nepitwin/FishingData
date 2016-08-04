/*
 *
 *  * Copyright 2016 Andreas Sekulski
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package example;

import com.client.fish.FishData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Example for generic fish data class.
 *
 * @author Andreas Sekulski
 */
public class Main {
    public final static void main(String[] args) throws Exception {
        // Example to obtain magic cards from API call.
        storeMagicCards(1);
     }

    /**
     * Example implementation to obtain JSON data.
     * @param page - PageNr to obtain
     */
     public static void storeMagicCards(int page) {

         String url = String.format("https://api.magicthegathering.io/v1/cards?page=%1$d", page);

         // Create a custom response handler
         ResponseHandler<JSONArray> responseHandler = new ResponseHandler<JSONArray>() {
             @Override
             public JSONArray handleResponse(HttpResponse response) throws IOException {
                 int status = response.getStatusLine().getStatusCode();
                 if (status == 200) {
                     HttpEntity entity = response.getEntity();
                     String jsonString = EntityUtils.toString(entity);
                     JSONObject object = new JSONObject(jsonString);
                     return object.getJSONArray("cards");
                 } else {
                     throw new ClientProtocolException("Unexpected response status: " + status);
                 }
             }
         };

         FishData<JSONArray> fishData = new FishData<>(responseHandler);
         JSONArray data;
         JSONObject card;

         try {
             data = fishData.fish(url);
             for(int i = 0 ; i < data.length(); i++) {
                 card = data.getJSONObject(i);
                 try (FileWriter file = new FileWriter("D:\\Magic_Cards\\" + card.getString("id") + ".json")) {
                    file.write(card.toString());
                    file.close();
                 }
             }

             if(data.length() > 0) {
                 // Get next cards
                 storeMagicCards(page + 1);
             }

         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}
