package spiderUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class HttpClientUtils {
    /**
     * 跳过ssl安全验证403错误的CloseableHttpClient
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }).build();
//            System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");

//            sslContext.
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static CloseableHttpClient Creat() throws NoSuchAlgorithmException, KeyManagementException {


        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);


        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = sc;

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
//        new SSLClient().
        return client;
    }


    /**
     * 获取简单的httpclient
     *
     * @return
     */
    public static CloseableHttpClient creteHttpclient() {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        return httpCilent;
    }

    /**
     * 获取HttpGet请求
     * headerMap：请求头
     *
     * @param url
     * @param headerMap
     * @return
     */
    public static HttpGet createHttpGet(String url, Map<String, String> headerMap) {
        /**
         * 设置请求参数（代理等
         */
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost("61.175.225.162", 80, "http");
//        HttpHost proxy = new HttpHost("183.129.207.73", 14823, "HTTPS");
//
///////////////////
//        HttpHost proxy = new HttpHost("218.60.8.98", 3129, "http");
//        HttpHost proxy = new HttpHost("47.100.41.53", 8888, "http");
//        HttpHost proxy = new HttpHost("113.128.148.119", 8118, "HTTPS");
//        HttpHost proxy = new HttpHost("61.135.217.7", 80, "https");
////////////////////
        Stack<Proxy> proxyStack = new Stack();

        Proxy Proxy = new Proxy("122.235.172.230", 8118, "http");
        proxyStack.push(Proxy);
        proxyStack.push(new Proxy("163.125.158.12", 9999, "http"));
        proxyStack.push(new Proxy("27.208.28.236", 8060, "http"));
        proxyStack.push(new Proxy("120.35.33.3", 8908, "http"));
        proxyStack.push(new Proxy("218.60.8.98", 3129, "http"));//11
        proxyStack.push(new Proxy("47.106.120.36", 8118, "http"));
        proxyStack.push(new Proxy("47.105.151.97", 80, "http"));
        proxyStack.push(new Proxy("58.58.213.55", 8888, "http"));
        proxyStack.push(new Proxy("47.105.163.176", 80, "http"));
        proxyStack.push(new Proxy("47.105.144.51", 80, "http"));
        proxyStack.push(new Proxy("218.60.8.98", 3129, "http"));//11

        /*不删除栈顶值*/
        Proxy proxyIP = proxyStack.peek();
        HttpHost proxy = new HttpHost(proxyIP.getIp(), proxyIP.getPort(), proxyIP.getType());
        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(5000)   //设置连接超时时间
//                .setConnectionRequestTimeout(5000) // 设置请求超时时间
//                .setSocketTimeout(5000)
//                .setRedirectsEnabled(true)//默认允许自动重定向
                .setProxy(proxy)
                .build();
        HttpGet httpGet = new HttpGet(url);
        //设置请求配置
        httpGet.setConfig(requestConfig);
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
//                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpGet;
    }

    /**
     * 创建post请求
     *
     * @param url
     * @param headerMap      请求头
     * @param paramsMap：表单参数
     * @return
     */
    public static HttpPost createHttpPost(String url, Map<String, String> headerMap, Map<String, String> paramsMap) {
        /**
         * 设置请求参数（代理等
         */
        String encoding = "UTF-8";
        // 依次是代理地址，代理端口号，协议类型
//        HttpHost proxy = new HttpHost("47.100.41.53", 8888, "http");
//        HttpHost proxy = new HttpHost("61.135.217.7", 80, "http");
//        HttpHost proxy = new HttpHost("61.175.225.162", 80, "http");
//        HttpHost proxy = new HttpHost("122.235.172.230", 8118, "http");
        Stack<Proxy> proxyStack = new Stack();
        Proxy Proxy = new Proxy("122.235.172.230", 8118, "http");
        proxyStack.push(Proxy);
//        proxyStack.push(new Proxy("163.125.158.12", 9999, "http"));
//        proxyStack.push(new Proxy("27.208.28.236", 8060, "http"));
//        proxyStack.push(new Proxy("120.35.33.3", 8908, "http"));
//        proxyStack.push(new Proxy("218.60.8.98", 3129, "http"));//11
//        proxyStack.push(new Proxy("47.106.120.36", 8118, "http"));
//        proxyStack.push(new Proxy("47.105.151.97", 80, "http"));
//        proxyStack.push(new Proxy("58.58.213.55", 8888, "http"));
//        proxyStack.push(new Proxy("47.105.163.176", 80, "http"));
//        proxyStack.push(new Proxy("47.105.144.51", 80, "http"));
        proxyStack.push(new Proxy("218.60.8.98", 3129, "http"));//11
        proxyStack.push(new Proxy("47.105.144.51", 80, "http"));

        /*不删除栈顶值*/
        Proxy proxyIP = proxyStack.peek();
        HttpHost proxy = new HttpHost(proxyIP.getIp(), proxyIP.getPort(), proxyIP.getType());
        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(5000)   //设置连接超时时间
//                .setConnectionRequestTimeout(5000) // 设置请求超时时间
//                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .setProxy(proxy)
                .build();
        HttpPost httpPost = new HttpPost(url);
        //设置请求配置
        httpPost.setConfig(requestConfig);
        //装填表单参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //设置参数到请求对象中
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpPost;
    }

    /**
     * 创建post请求
     *
     * @param url
     * @param headerMap      请求头
     * @param paramsMap：表单参数
     * @return
     */
    public static HttpPost createHttpPostV2(String url, Map<String, String> headerMap, Map<String, Object> paramsMap) {
        /**
         * 设置请求参数（代理等
         */
        String encoding = "UTF-8";
        // 依次是代理地址，代理端口号，协议类型
///
        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(5000)   //设置连接超时时间
//                .setConnectionRequestTimeout(5000) // 设置请求超时时间
//                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
//                .setProxy(proxy)
                .build();
        HttpPost httpPost = new HttpPost(url);
        //设置请求配置
        httpPost.setConfig(requestConfig);
        //装填表单参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }
        //设置参数到请求对象中
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpPost;
    }

    /**
     * 创建post请求:JSON方式提交
     *
     * @param url
     * @param headerMap     请求头
     * @param json：JSON表单参数
     * @return
     */
    public static HttpPost createHttpPostByJSON(String url, Map<String, String> headerMap, String json) {
        /**
         * 设置请求参数（代理等
         */
        String encoding = "UTF-8";
        // 依次是代理地址，代理端口号，协议类型
///
        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(5000)   //设置连接超时时间
//                .setConnectionRequestTimeout(5000) // 设置请求超时时间
//                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
//                .setProxy(proxy)
                .build();
        HttpPost httpPost = new HttpPost(url);
        //设置请求配置
        httpPost.setConfig(requestConfig);
        //装填Json参数
        StringEntity entity = new StringEntity(json, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        //设置参数到请求对象中
        try {
            httpPost.setEntity(entity);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpPost;
    }

    /**
     * 执行请求入口
     *
     * @param httpClient
     * @param requestBase
     * @return
     */
    public static String excuteRequest(HttpClient httpClient, HttpRequestBase requestBase) {
        HttpEntity httpEntity = null;
        String encoding = "UTF-8";
//        String encoding = "GBK";
        String response = null;
        try {
            httpEntity = httpClient.execute(requestBase)
//            httpEntity = new SSLClient().execute(requestBase)
                    .getEntity();
            if (null != httpEntity) {
                //按指定编码转换结果实体为String类型
                response = EntityUtils.toString(httpEntity, encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 上传文件
     *
     * @param httpPost
     * @param fileKey
     * @param file
     * @param textMap
     * @return
     */
    public static HttpPost createByUploadFile(HttpPost httpPost, String fileKey, File file, Map<String, String> textMap) {
        String fileName = file.getName();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            builder.addBinaryBody(fileKey, new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //设置一般表单参数
        // 类似浏览器表单提交，对应input的name和value
        if (textMap != null && !textMap.isEmpty()) {
            for (Map.Entry<String, String> entry : textMap.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        return httpPost;
    }

    /*代理IP类*/
    static class Proxy {
        String ip;
        int port;
        String type;

        public Proxy() {
        }

        public Proxy(String ip, int port, String type) {
            this.ip = ip;
            this.port = port;
            this.type = type;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
