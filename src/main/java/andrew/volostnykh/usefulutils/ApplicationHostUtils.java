package andrew.volostnykh.usefulutils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationHostUtils {

    @Value("${app.back-end-url}")
    private final String appBackEndUrl;

    @Value("${app.base-url}")
    private final String appBaseUrl;

    public String getAppBackEndUrl() {
        return appBackEndUrl;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public URI buildBackEndBasedUrl(Map<String, String> params, String pathFormat, String... pathVars) {
        return buildUrl(appBackEndUrl, params, pathFormat, pathVars);
    }

    public URI buildFrontEndBasedUrl(Map<String, String> params, String pathFormat, String... pathVars) {
        return buildUrl(appBaseUrl, params, pathFormat, pathVars);
    }

    public URI buildUrl(String baseUrl, Map<String, String> params, String pathFormat, String... pathVars) {
        String path;
        if (pathVars != null && pathVars.length > 0) {
            path = String.format(pathFormat, pathVars);
        } else {
            path = pathFormat;
        }

        if (params == null) {
            params = Collections.emptyMap();
        }

        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath(getPath(builder, path))
                    .addParameters(params
                            .entrySet()
                            .stream()
                            .map(pair -> new BasicNameValuePair(pair.getKey(), pair.getValue()))
                            .collect(Collectors.toList()));
            return builder.build();
        } catch (URISyntaxException e) {
            log.error(
                    "Can't build URI, baseurl: {}, path format: {}, path vars: {}, params: {}",
                    baseUrl,
                    pathFormat,
                    pathVars,
                    params,
                    e
            );
            return null;
        }
    }

    private String getPath(URIBuilder builder, String path) {
        return Objects.isNull(builder.getPath()) ? path : builder.getPath() + path;
    }
}
