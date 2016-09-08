package kr.yerina.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * The type Http client util.
 */
public class HttpClientUtil {

	RestTemplate restTemplate = new RestTemplate();
	ObjectMapper objectMapper = new ObjectMapper();

	private String getServerUrl(String domain, int port) {
		return "http://" + domain + ":" + port;
	}

	/**
	 * Gets parms.
	 *
	 * @param paramLt the param lt
	 *
	 * @return the parms
	 */
	public static String getParms(String[][] paramLt) {
		StringBuffer sb = new StringBuffer();
		if(paramLt!=null){
			for(int i=0; i<paramLt.length; i++) {
				if(i==0)sb.append("?");
				else	sb.append("&");
				sb.append(paramLt[i][0]);
				sb.append("=");
				sb.append(paramLt[i][1]);
			}
		}

		return sb.toString();
	}

	/**
	 * Gets parms.
	 *
	 * @param param the param
	 *
	 * @return the parms
	 */
	public static String getParms(HashMap<String, String> param) {
		StringBuffer sb = new StringBuffer();
		
		Iterator itr = param.keySet().iterator();
		int i=0;
		while (itr.hasNext()) {  
			String key = (String)itr.next();
			if(i==0)sb.append("?");
			else	sb.append("&");
			sb.append(key);
			sb.append("=");
			sb.append(param.get(key));			
  
		}		

		return sb.toString();
	}

	/**
	 * Gets post parms.
	 *
	 * @param paramLt the param lt
	 *
	 * @return the post parms
	 */
	public static List<NameValuePair> getPostParms(String[][] paramLt) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		for(int i=0; i<paramLt.length; i++) {
			params.add(new BasicNameValuePair(paramLt[i][0], paramLt[i][1]));
		}

		return params;
	}

	/**
	 * Gets post parms.
	 *
	 * @param param the param
	 *
	 * @return the post parms
	 */
	public static List<NameValuePair> getPostParms(HashMap<String, Object> param) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		Iterator itr = param.keySet().iterator();
		while (itr.hasNext()) {  
			String key = (String)itr.next();
			params.add(new BasicNameValuePair(key, (String)param.get(key)));
		}

		return params;
	}

	/**
	 * Get string.
	 *
	 * @param URL   the url
	 * @param param the param
	 *
	 * @return the string
	 *
	 * @throws Exception the exception
	 */
// Get Method 를 통한 XML 데이터 조회
//	public static String GET(String URL, String[][] paramLt) throws Exception {
	public static String GET(String URL, HashMap<String, String> param) throws Exception {
		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(URL + getParms(param));

		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			public String handleResponse(final HttpResponse response) throws IOException {
				String result = "";
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					result = entity != null ? EntityUtils.toString(entity) : null;
				}
				return result;
			}
		};

		return client.execute(get, responseHandler);
	}

	/**
	 * Post string.
	 *
	 * @param URL   the url
	 * @param param the param
	 *
	 * @return the string
	 */
// Post Method 를 통한 XML 데이터 조회
	public static String POST(String URL, HashMap<String, Object> param) {
		try{
			HttpClient client = HttpClients.createDefault();
			HttpPost	post	= new HttpPost(URL);
	
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws IOException {
					String result = "";
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						result = entity != null ? EntityUtils.toString(entity) : null;
					}
					return result;
				}
			};
	
			List<NameValuePair> params = getPostParms(param);
	
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);
	
			return client.execute(post, responseHandler);
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Gets json parser.
	 *
	 * @param info the info
	 *
	 * @return the json parser
	 *
	 * @throws ParseException the parse exception
	 */
	public static JSONObject getJSONParser(String info) throws ParseException{
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(info);
		
		return jsonObject;
		
	}

//TODO 작업 해야함
	public void sendByPost(String domain, int port) {
		String serverUrl = getServerUrl(domain, port);

		Map<String, Object> params = new HashMap<>();

		params.put("data", "test");

		String body = null;
		try {
			body = objectMapper.writeValueAsString(params);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if(body != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            org.springframework.http.HttpEntity entity = new org.springframework.http.HttpEntity(body, headers);

			restTemplate.postForEntity(
					serverUrl,
					entity,
					String.class
					);
		}
	}

    //TODO 작업 해야함
	public void sendByGet(String domain, int port, String appId, String authKey, String channelName, String event, String message) {
		String serverUrl = getServerUrl(domain, port);

		Map<String, Object> params = new HashMap<>();
		params.put("channels", channelName);
		params.put("event", event);
		params.put("auth_key", authKey);
		params.put("data", message);
		params.put("app_id", appId);

		restTemplate.getForObject(
				serverUrl + "/channel/{app_id}?channels={channels}&event={event}&auth_key={auth_key}&data={data}",
				String.class,
				params);
	}




	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			map.put("key", "value");

			final String url = "";
			System.out.println(HttpClientUtil.POST(url, map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
