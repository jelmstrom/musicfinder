package se.jelmstrom.musicfinder.http;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class ClientWrapper {

    private final static Log log = LogFactory.getLog(ClientWrapper.class);
    private static final CacheConfig cacheConfig = CacheConfig.custom()
            .setMaxCacheEntries(1000)
            .setMaxObjectSize(500000)
            .setAllow303Caching(true)
            .setHeuristicCachingEnabled(true)
            .setHeuristicDefaultLifetime(3600)
            .setSharedCache(true)
            .build();
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(30000)
            .setSocketTimeout(30000)
            .build();
    private static final CloseableHttpClient cachingClient = CachingHttpClients.custom()
            .setCacheConfig(cacheConfig)
            .setUserAgent("MusicFinder/1.0 (johan.elmmstrom@gmail.com)")
            .setDefaultRequestConfig(requestConfig)
            .setDefaultHeaders(Arrays.asList(
                    new BasicHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString()),
                    new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())))
            .build();
    private static final HttpCacheContext context = HttpCacheContext.create();

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public <T> Optional<T> makeRequest(String url, Class t) {
        HttpGet httpget = new HttpGet(url);
        log.trace(String.format("Getting resource %s", url));
        try (CloseableHttpResponse response = cachingClient.execute(httpget, context)) {
            if(context.getCacheResponseStatus().equals(CacheResponseStatus.CACHE_MISS)) {
                log.debug(String.format("%s for url %s", context.getCacheResponseStatus(), url));
            }
            if (response.getStatusLine().getStatusCode() < 300) {
                return Optional.of((T) mapper.readValue(response.getEntity().getContent(), t));
            }
        } catch (IOException e) {
            log.warn(String.format("Could not complete request to %s [ %s %s ]", url, e.getCause(), e.getMessage()));
        } catch (IndexOutOfBoundsException ioex){
            log.warn(String.format("Failed to build entity  %s [ %s %s ]", url, ioex.getCause(), ioex.getMessage()));
        }
        return Optional.empty();
    }
}
