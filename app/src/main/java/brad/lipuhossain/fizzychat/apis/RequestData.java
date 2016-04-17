package brad.lipuhossain.fizzychat.apis;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import brad.lipuhossain.fizzychat.constants.ConstantURLS;
import brad.lipuhossain.fizzychat.constants.Constants;

/**
 * Request and get data from API
 *
 * @author PhanTri
 */
public class RequestData {
    private JsonParser mJsonParser = null;
    private String REQUEST_DATA_URL = null;
    private int mRestType = 0;
    private Context mContex = null;

    public RequestData(Context context) {
        mJsonParser = new JsonParser();
        mContex = context;
    }

    /**
     * TODO <br>
     * Function to get data
     *
     * @return json in string
     * @author Phan Tri
     * @date Oct 15, 2014
     */
    @SuppressWarnings("unchecked")
    public String getData(int typeOfRequest, final HashMap<String, Object> parameters) {
        ArrayList<Object> listParams = new ArrayList<Object>();
        ArrayList<NameValuePair> nameValueParams = new ArrayList<NameValuePair>();
        ArrayList<Map.Entry<String, Bitmap>> bitmapParams = new ArrayList<Map.Entry<String, Bitmap>>();
        JSONObject returnJson = null;

        switch (typeOfRequest) {
            case Constants.REQUEST_GET_USER_COUNTRY:
                mRestType = Constants.REST_GET;
                REQUEST_DATA_URL = ConstantURLS.URL_GET_USER_COUNTRY;
                break;



            default:
                break;
        }

        listParams.add(nameValueParams);
        listParams.add(bitmapParams);
        // Building Parameters
        Log.e("Request URL:", REQUEST_DATA_URL);
        returnJson = mJsonParser.getJSONFromUrl(REQUEST_DATA_URL, mRestType, listParams);

        return (returnJson != null) ? returnValues(returnJson) : null;
    }

    /**
     * TODO <br>
     * Function to return values from server after check <br>
     *
     * @param returnObj
     * @return
     * @author Munir
     * @date Oct 15, 2016
     */
    public String returnValues(JSONObject returnObj) {
        return returnObj.toString();
    }
}
