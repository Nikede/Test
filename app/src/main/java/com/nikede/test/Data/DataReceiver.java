package com.nikede.test.Data;

import com.nikede.test.Models.CreditByDate;
import com.nikede.test.Models.CreditByPartner;
import com.nikede.test.Models.DebitByDate;
import com.nikede.test.Models.DebitByPartner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class DataReceiver {
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void parseAllItems(DebitLab debitLab, CreditLab creditLab, JSONObject jsonBody)
            throws IOException, JSONException {

        parseCreditItems(creditLab, jsonBody);
        parseDebitItems(debitLab, jsonBody);

    }

    public void parseDebitItems(DebitLab debitLab, JSONObject jsonBody)
            throws IOException, JSONException  {

        if (!jsonBody.getBoolean("success")) {
            return;
        }

        debitLab.clear();

        JSONObject dataJsonObject = jsonBody.getJSONObject("data");
        JSONObject contentJsonObject = dataJsonObject.getJSONObject("content");

        JSONObject debitJsonObject = contentJsonObject.getJSONObject("debit");

        JSONArray debitByPartnerJsonArray = debitJsonObject.getJSONArray("byPartner");
        for (int i = 0; i < debitByPartnerJsonArray.length(); i++) {
            JSONObject debitByPartnerJsonObject = debitByPartnerJsonArray.getJSONObject(i);
            DebitByPartner item = new DebitByPartner();
            item.setSum(debitByPartnerJsonObject.getDouble("sum"));
            item.setPartner(debitByPartnerJsonObject.getString("partner"));
            debitLab.addDebitByPartner(item);
        }

        JSONArray debitByDateJsonArray = debitJsonObject.getJSONArray("byDate");
        for (int i = 0; i < debitByDateJsonArray.length(); i++) {
            JSONObject debitByDateJsonObject = debitByDateJsonArray.getJSONObject(i);
            DebitByDate item = new DebitByDate();
            item.setSum(debitByDateJsonObject.getDouble("sum"));
            item.setDate(new Date(debitByDateJsonObject.getLong("date")));
            debitLab.addDebitByDate(item);
        }
    }

    public void parseCreditItems(CreditLab creditLab, JSONObject jsonBody)
            throws IOException, JSONException  {

        if (!jsonBody.getBoolean("success")) {
            return;
        }

        creditLab.clear();

        JSONObject dataJsonObject = jsonBody.getJSONObject("data");
        JSONObject contentJsonObject = dataJsonObject.getJSONObject("content");

        JSONObject creditJsonObject = contentJsonObject.getJSONObject("credit");

        JSONArray creditByPartnerJsonArray = creditJsonObject.getJSONArray("byPartner");
        for (int i = 0; i < creditByPartnerJsonArray.length(); i++) {
            JSONObject creditByPartnerJsonObject = creditByPartnerJsonArray.getJSONObject(i);
            CreditByPartner item = new CreditByPartner();
            item.setSum(creditByPartnerJsonObject.getDouble("sum"));
            item.setPartner(creditByPartnerJsonObject.getString("partner"));
            creditLab.addCreditByPartner(item);
        }

        JSONArray creditByDateJsonArray = creditJsonObject.getJSONArray("byDate");
        for (int i = 0; i < creditByDateJsonArray.length(); i++) {
            JSONObject creditByDateJsonObject = creditByDateJsonArray.getJSONObject(i);
            CreditByDate item = new CreditByDate();
            item.setSum(creditByDateJsonObject.getDouble("sum"));
            item.setDate(new Date(creditByDateJsonObject.getLong("date")));
            creditLab.addCreditByDate(item);
        }
    }
}
