package me.personal.integration.gitlab.flows;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhongyi on 2018/10/25.
 */
@Controller
public class WebAppController {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${oauth2.server.url}")
    private String gitlabServerUrl;
    @Value("${oauth2.server.url.authorize.path:/oauth/authorize}")
    private String authorizePath;
    @Value("${oauth2.server.url.token.path:/oauth/token}")
    private String tokenPath;

    @Value("${oauth2.client.id}")
    private String clientId;
    @Value("${oauth2.client.secret}")
    private String clientSecret;
    @Value("${oauth2.client.callback.url}")
    private String callbackUrl;

    OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    String accessToken = null;

    @PreDestroy
    public void cleanUp() {
        accessToken = null;
        oAuthClient.shutdown();
    }

    @RequestMapping("/displayresult")
    @ResponseBody
    public String display() throws Throwable {
        logger.info("query user information with access token...");
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(gitlabServerUrl + "/api/v3/user").setAccessToken(accessToken).buildQueryMessage();
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        logger.info("had authorized, just query for user information...");
        logger.info("user information: " + resourceResponse.getBody());
        return resourceResponse.getBody();
    }

    @RequestMapping("/getToken")
    @ResponseBody
    public String token() {
        return accessToken;
    }

    @RequestMapping("/webappauth")
    public String index(HttpServletRequest req, HttpServletResponse response) throws Throwable {
        /**
         * Step 1
         *
         * */
        logger.info("first login, build oauth request >..");
        OAuthClientRequest request = OAuthClientRequest
                .authorizationLocation(gitlabServerUrl + authorizePath)
                .setClientId(clientId)
                .setRedirectURI(callbackUrl)
                .setResponseType("code")
                .buildQueryMessage();

        String gitlabAuthUrl = request.getLocationUri();

        logger.info("redirect to : " + gitlabAuthUrl);
        return "redirect:" + gitlabAuthUrl;
    }


    @RequestMapping("/callback")
    @ResponseBody
    public String callback(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "error_description", required = false) String errorDescription) throws Throwable {

        if (StringUtils.hasLength(error)) {
            logger.error("authorization fails with error={} and error description={}", error, errorDescription);
        } else {
            logger.info("callback request receives with code={}", code);

            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(gitlabServerUrl + tokenPath)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectURI(callbackUrl)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setCode(code)
                    .buildQueryMessage();
            logger.info("build authorize request with code:{} and client secret", code);

            OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);
            accessToken = response.getAccessToken();
            logger.info("access token got: {}", accessToken);

        }
        return accessToken;
    }


}



