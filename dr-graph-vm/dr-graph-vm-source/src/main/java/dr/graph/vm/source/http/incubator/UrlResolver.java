package dr.graph.vm.source.http.incubator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscription;

import dr.graph.vm.source.GenericResolverException;
import dr.graph.vm.source.Resolver;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpHeaders;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import jdk.incubator.http.HttpResponse.BodyHandler;
import jdk.incubator.http.HttpResponse.BodyProcessor;

public class UrlResolver implements Resolver<String,String>{
	
	public String resolve(String id) throws GenericResolverException {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
			    .uri(new URI(id))
			    .build();
		} catch (URISyntaxException e) {
			throw new GenericResolverException("Could not build uri=",e);
		}

		HttpResponse<String> response;
		try {
			response = client.send(request, new HttpResponse.BodyHandler<String>() {

				@SuppressWarnings("unchecked")
				public BodyProcessor<String> apply(int arg0, HttpHeaders arg1) {
					return new HttpResponse.BodyProcessor() {

						public void onComplete() {
							// TODO Auto-generated method stub
							
						}

						public void onError(Throwable arg0) {
							// TODO Auto-generated method stub
							
						}

						public void onNext(Object arg0) {
							// TODO Auto-generated method stub
							
						}

						public void onSubscribe(Subscription arg0) {
							// TODO Auto-generated method stub
							
						}

						public CompletionStage getBody() {
							// TODO Auto-generated method stub
							return null;
						}};
				}});
		} catch (Exception e) {
			throw new GenericResolverException("Could not resolve uri=",e);
		}

		return response.body();
	}

	
	
}
