package com.luopc.platform.cloud.service.ai;

import dev.langchain4j.http.client.HttpClientBuilder;
import dev.langchain4j.http.client.HttpClientBuilderLoader;
import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.internal.OpenAiClient;

import javax.net.ssl.SSLContext;
import java.lang.reflect.Proxy;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;

import static java.net.http.HttpClient.Version.HTTP_1_1;

/**
 * Hello world!
 *
 */
public class OpenAiChatModelTest {
    public static void main(String[] args) {

//        HttpClientBuilder customHttpClient = HttpClientBuilderLoader.loadHttpClientBuilder()
//                .connectTimeout(Duration.ofSeconds(10)) // Set connection timeout
//                .readTimeout(Duration.ofSeconds(30)) // Set read timeout
//                ;


        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(System.getenv("deepseek-token"))
                .modelName("deepseek-chat")
                .build();
        String answer = model.chat("how to install jDK in windows?");
        System.out.println(answer); // Hello World
    }



    public void testHttpClient() throws NoSuchAlgorithmException {
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
                .sslContext(SSLContext.getDefault())
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("postman", "password".toCharArray());
                    }
                });
        HttpRequest outRequest = HttpRequest.newBuilder()
                .version(HTTP_1_1)
                .GET()
                .uri(URI.create("https://httpwg.org/asset/http.svg")) // no matter which URI to request
                .setHeader("Proxy-Authorization", "Basic token123")
                .build();

//        HttpClientBuilder httpClientBuilder2 = HttpClientBuilderLoader.loadHttpClientBuilder().
//
        JdkHttpClientBuilder jdkHttpClientBuilder = JdkHttpClient.builder()
                .httpClientBuilder(httpClientBuilder);
//
//
//        HttpClientBuilder customHttpClientBuilder = new CustomHttpClientBuilder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-host", 8080)))
//                .sslContext(sslContext);
//
//        HttpClientBuilder httpClientBuilder = HttpClientBuilderLoader.loadHttpClientBuilder()
//                .addInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(HttpRequest request) {
//                        request.addHeader("X-Custom-Header", "value");
//                    }
//                });
//
        OpenAiClient client = OpenAiClient.builder()
                .httpClientBuilder(jdkHttpClientBuilder)
                .apiKey("your-api-key")
                .customHeaders(Map.of("X-Custom-Header", "value", "Token", "token 1234"))
                .build();




        OpenAiChatModel model = OpenAiChatModel.builder()
                .httpClientBuilder(jdkHttpClientBuilder)
                .baseUrl("")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .customHeaders(Map.of("X-Custom-Header", "value", "Token", "token 1234"))
                .build();
    }
}
