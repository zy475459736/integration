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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhongyi on 2018/10/25.
 */
@RestController
public class ResourceOwnerPasswordCredentialsController {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${oauth2.server.url}")
    private String gitlabServerUrl;
    @Value("${oauth2.server.url.token.path:/oauth/token}")
    private String tokenPath;

    @Value("${oauth2.credentials.username}")
    private String username;
    @Value("${oauth2.credentials.password}")
    private String password;

    OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

    String accessToken = null;
    String refreshToken = null;

    @PreDestroy
    public void cleanUp() {
        accessToken = null;
        refreshToken = null;
        oAuthClient.shutdown();
    }

    @RequestMapping("/display2")
    @ResponseBody
    public String display() throws Throwable {
        logger.info("query user information with access token...");
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(gitlabServerUrl + "/api/v3/user").setAccessToken(accessToken).buildQueryMessage();
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        logger.info("had authorized, just query for user information...");
        logger.info("user information: " + resourceResponse.getBody());
        return resourceResponse.getBody();
    }

    @RequestMapping("/getToken2")
    @ResponseBody
    public String token() {
        return accessToken;
    }

    @RequestMapping("/credentialauth")
    public String index(HttpServletRequest req, HttpServletResponse response) throws Throwable {
        logger.info("first login, build oauth request >..");
        /**
         * Step 1
         * */
        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(gitlabServerUrl + tokenPath)
                .setGrantType(GrantType.PASSWORD)
                .setUsername(username)
                .setPassword(password)
                .buildQueryMessage();

        String gitlabAuthUrl = request.getLocationUri();

        logger.info("redirect to : " + gitlabAuthUrl);
        OAuthJSONAccessTokenResponse oAuthJSONAccessTokenResponseresponse = oAuthClient.accessToken(request);
        /**
         * Step 2
         * */
        accessToken = oAuthJSONAccessTokenResponseresponse.getAccessToken();
        refreshToken = oAuthJSONAccessTokenResponseresponse.getRefreshToken();
        logger.info("access token got: {};refresh token got:{};", accessToken, refreshToken);
        return "redirect:/display2";
    }

}



