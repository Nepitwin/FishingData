FishingData
================

Simple generic java request handler to obtain data. For example API calls to get JSON files. FishingData used Apache HttpClient.

========
### Example

An example implementation exists under example folder. This example shows how to obtain an json array.

```
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

try {
   data = fishData.fish(url);
   for(int i = 0 ; i < data.length(); i++) {
       System.out.println(data.getJSONObject(i));
   }
} catch (IOException e) {
   e.printStackTrace();
}
 ```

### License

```
Copyright 2016 Andreas Sekulski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
