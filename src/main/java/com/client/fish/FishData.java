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

package com.client.fish;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;


/**
 * Fishing data class to get data from an request.
 *
 * @author Andreas Sekulski
 */
public class FishData<T> {

    /**
     * Response handler to handle incoming data.
     */
    private ResponseHandler<T> handler;

    /**
     * Constructor to create an fish data.
     * @param handler Event handler to response incoming data.
     */
    public FishData(ResponseHandler<T> handler) {
        this.handler = handler;
    }

    /**
     * Fish data from url.
     * @param url URL to obtain data.
     * @return Data from generic object T which is obtained from handler request.
     * @throws IOException If an error occured.
     */
    public T fish(String url) throws IOException {

        T data = null;
        HttpGet httpget;
        CloseableHttpClient httpClient = null;

        try {
            httpClient = HttpClients.createDefault();
            httpget = new HttpGet(url);
            System.out.println("Executing request " + httpget.getRequestLine());
            data = httpClient.execute(httpget, handler);
        } finally {
            if(httpClient != null) {
                httpClient.close();
            }
        }

        return data;
    }
}
