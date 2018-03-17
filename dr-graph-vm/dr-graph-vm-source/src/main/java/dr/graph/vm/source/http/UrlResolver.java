package dr.graph.vm.source.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import dr.common.logger.LazyLogger;
import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;

public class UrlResolver implements Resolver<String, String> , LazyLogger {
    
	private final String USER_AGENT = "Mozilla/5.0";

	private final Configuration config;

	public UrlResolver(Configuration config) {
		this.config = config;
	}

	public String resolve(String id) throws GenericResolverException {

		log("resolving "+id);
		
		try {
			return send(new URL(id));
		} catch (MalformedURLException e) {
			throw new GenericResolverException("Could not form valid url from provided id=" + id);
		}

	}

	private String send(URL url) throws GenericResolverException {
		HttpURLConnection connection = null;
		try {
			log("get http(s) connection ...");
			connection = getConnection(url, config.connType);
			log("set request method ...");
			connection.setRequestMethod(config.reqType.name());
			log("add HEADER ...");
			connection.setRequestProperty("User-Agent", USER_AGENT);

			// do some extra stuff for POST ...
			doPost(connection, config.parameters);

			int responseCode = connection.getResponseCode();
			log("\nSending 'GET' request to URL : " + url);
			log("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();

		} catch (Exception e) {
			throw new GenericResolverException("Could not complete http request to '"+url+"' with configuration=" + config, e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private HttpURLConnection getConnection(URL url, ConnectionType type) throws IOException {
		switch (type) {
		case HTTPS:
			return (HttpsURLConnection) url.openConnection();
		case HTTP:
			return (HttpURLConnection) url.openConnection();
		default:
			throw new IOException("Cannot create connection of type " + type);
		}
	}

	private void doPost(HttpURLConnection connection, String urlParameters) throws IOException {
		if(config.reqType == RequestType.GET) {
			return;
		}
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
	}

	public static class Configuration {

		private ConnectionType connType;
		private RequestType reqType;
		private String parameters;

		public ConnectionType getConnType() {
			return connType;
		}

		public Configuration withConnType(ConnectionType connType) {
			this.connType = connType;
			return this;
		}

		public Configuration withReqType(RequestType reqType) {
			this.reqType = reqType;
			return this;
		}
		
		@Override
		public String toString() {
			return this.getClass().getSimpleName()+"{"
					+ "connType="+connType +","
					+ "reqType="+reqType +","
					+ "parameters="+parameters
					+ "}";
		}

	}

	public enum ConnectionType {
		HTTP, HTTPS;
	}

	public enum RequestType {
		GET, POST;
	}

}
