package week2;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;

/**
 * HttpClient工具类
 *
 * @author shirui
 * @date 2022/1/16
 */
public class HttpClientUtils {

    /** 从连接池中获取连接的超时时间（单位：ms） */
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;

    /** 与服务器连接的超时时间（单位：ms） */
    private static final int CONNECTION_TIMEOUT = 5000;

    /** 从服务器获取响应数据的超时时间（单位：ms） */
    private static final int SOCKET_TIMEOUT = 10000;

    /** 连接池的最大连接数 */
    private static final int MAX_CONN_TOTAL = 100;

    /** 每个路由上的最大连接数 */
    private static final int MAX_CONN_PER_ROUTE = 50;

    /** Http客户端对象 */
    private static CloseableHttpClient httpClient = null;

    /** Connection配置对象 */
    private static ConnectionConfig connectionConfig = null;

    /** Socket配置对象 */
    private static SocketConfig socketConfig = null;

    /** 配置信息 */
    private static RequestConfig requestConfig = null;

    /** Cookie存储对象 */
    private static BasicCookieStore cookieStore = null;

    static{
        init();
    }

    /**
     * 全局对象初始化
     *
     */
    private static void init(){
        // 创建Connection配置对象
        connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();

        // 创建Request配置对象
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        // 创建Cookie存储对象（服务端返回的Cookie保存在CookieStore中，下次再访问时才会将CookieStore中的Cookie发送给服务端）
        cookieStore = new BasicCookieStore();

        // 创建HttpClient对象
        httpClient = HttpClients.custom()
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                //.setUserAgent(USER_AGENT)
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();
    }

    /**
     * get方法
     *
     */
    public static HttpResult get(String url){
        HttpResult httpResult = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            // 获得响应实体
            HttpEntity responseEntity = response.getEntity();
            // 发送Post请求并得到响应结果
            httpResult = httpClient.execute(httpGet, getResponseHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResult;
    }

    /**
     * 获取响应结果处理器（把响应结果封装成HttpResult对象）
     */
    private static ResponseHandler<HttpResult> getResponseHandler() {
        ResponseHandler<HttpResult> responseHandler = new ResponseHandler<HttpResult>() {
            @Override
            public HttpResult handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                if (httpResponse == null) {
                    throw new ClientProtocolException("HttpResponse is null");
                }

                StatusLine statusLine = httpResponse.getStatusLine();
                HttpEntity httpEntity = httpResponse.getEntity();
                if (statusLine == null) {
                    throw new ClientProtocolException("HttpResponse contains no StatusLine");
                }
                if (statusLine.getStatusCode() != 200) {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
                if (httpEntity == null) {
                    throw new ClientProtocolException("HttpResponse contains no HttpEntity");
                }

                HttpResult httpResult = new HttpResult();
                httpResult.setStatusCode(statusLine.getStatusCode());
                ContentType contentType = ContentType.getOrDefault(httpEntity);
                httpResult.setContentType(contentType.toString());
                boolean isTextType = isTextType(contentType);
                httpResult.setTextType(isTextType);
                if (isTextType) {
                    httpResult.setStringContent(EntityUtils.toString(httpEntity));
                } else {
                    httpResult.setByteArrayContent(EntityUtils.toByteArray(httpEntity));
                }
                httpResult.setCookies(cookieStore.getCookies());
                httpResult.setHeaders(httpResponse.getAllHeaders());

                return httpResult;
            }
        };
        return responseHandler;
    }

    /**
     * 判断响应的内容是否是文本类型
     * @param contentType 响应内容的类型
     */
    private static boolean isTextType(ContentType contentType) {
        if (contentType == null) {
            throw new RuntimeException("ContentType is null");
        }
        if (contentType.getMimeType().startsWith("text")) {
            return true;
        } else if (contentType.getMimeType().startsWith("image")) {
            return false;
        } else if (contentType.getMimeType().startsWith("application")) {
            if (contentType.getMimeType().contains("json") || contentType.getMimeType().contains("xml")) {
                return true;
            } else {
                return false;
            }
        } else if (contentType.getMimeType().startsWith("multipart")) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        // 测试get请求
        HttpResult httpResult = get("https://www.baidu.com");
        System.out.println(httpResult.getStringContent());
    }
}
