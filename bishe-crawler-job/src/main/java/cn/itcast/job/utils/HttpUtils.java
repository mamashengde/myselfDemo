package cn.itcast.job.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HttpUtils {

    private static PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();

        //设置最大连接数
        this.cm.setMaxTotal(100);

        //设置每个主机的最大连接数
        this.cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return
     */
    public  String doGetHtml(String url){
        //获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        //创建httpGet请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0");
        httpGet.setHeader("Cookie","\n" +
                "x-zp-client-id=e6c4d24c-800e-426d-b742-d1f8869d8ffb; sensorsdata2015jssdkchannel=%7B%22prop%22%3A%7B%22_sa_channel_landing_url%22%3A%22https%3A%2F%2Flanding.zhaopin.com%2Fresume-templates%3Fchannel_name%3D360_sem_track%26callback_id%3Dh5tU0XX3%26_data_version%3D0.5.0%26channel_utm_content%3Djl%26project%3Dzlclient%26channel_utm_medium%3Docpc%26tid%3Ds%26channel_link_type%3Dweb%26channel_utm_source%3D360PC%26hash_key%3DChLMjfS16C5zMRgPkydK%26sat_cf%3D2%26channel_utm_campaign%3Dxyjkay999%26channel_utm_term%3D771693%26_channel_track_key%3D7wuQAmYh%26link_version%3D1%26channel_keyword_id%3D17464499152%26channel_ad_id%3D10361649370%26channel_account_id%3D243985234%26device%3Dpc%26channel_adgroup_id%3D3721939131%26channel_campaign_id%3D2327961415%26qhclickid%3De0590e7b5d33de86%22%7D%7D; Hm_lvt_7fa4effa4233f03d11c7e2c710749600=1713277509; Hm_lpvt_7fa4effa4233f03d11c7e2c710749600=1713277509; LastCity=%E6%AD%A6%E6%B1%89; LastCity%5Fid=736; FSSBBIl1UgzbN7NO=538O1Q8LDJk42W538yyle1MoK_dEzv.o01dhJxxh7y.2D1aOs9y7X763CqfdbG3eKvxe7tVJ04RhqadpSynud4G; _uab_collina=171327752712256554580059; locationInfo_search={%22code%22:%222367%22%2C%22name%22:%22%E6%AD%A6%E6%B1%89%E5%90%B4%E5%AE%B6%E5%B1%B1%E7%BB%8F%E6%B5%8E%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91%E5%8C%BA%22%2C%22message%22:%22%E5%8C%B9%E9%85%8D%E5%88%B0%E5%B8%82%E7%BA%A7%E7%BC%96%E7%A0%81%22}; Hm_lvt_21a348fada873bdc2f7f75015beeefeb=1713277528; zp_passport_deepknow_sessionId=4a2f296fs5ceae4b108d68337c16d15476e6; at=d87a59c497af44b18779ffa442210b12; rt=cf6d41d3d74a413b99a9e1d522b2517f; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221180016066%22%2C%22first_id%22%3A%22189536dab0df0b-0282fc231dd95f2-7e56547f-1821369-189536dab0e1584%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg5NTM2ZGFiMGRmMGItMDI4MmZjMjMxZGQ5NWYyLTdlNTY1NDdmLTE4MjEzNjktMTg5NTM2ZGFiMGUxNTg0IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTE4MDAxNjA2NiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221180016066%22%7D%2C%22%24device_id%22%3A%22189536dab0df0b-0282fc231dd95f2-7e56547f-1821369-189536dab0e1584%22%7D; acw_tc=276082a117132808614197777ef06776623b3e019fd92c7253f1c0c1999842; Hm_lpvt_21a348fada873bdc2f7f75015beeefeb=1713280864; ssxmod_itna=YqUhDKYvxjooiQDXDnQhr0QqDt5tqr9wnDW+eeDsiDcYxA5D8D6DQeGTbnWzp5q8ZhG3hO/cwavqjtB+pdIQErYpeDHxY=DU6oPqoD445GwD0eG+DD4DWYqB6yDtqDkD3qDdfx7PGSAq08De9FfxxDUm2h5nKDTx01OxGODiHwRYYDmO5DREKDeO5D9pPDwOvwZxG70ngkDmbk5DDz57Mx9C0vAZSLSxgmF+gIzIovOo8vXf4Cu1v8DmnFpz7k=Hv1XF4PfQDPbCioCYrdjmxeCmDP5mxqsuD5UiDq=CnD5nb4iDhT0kaBDeDi2XoQGWYD==; ssxmod_itna2=YqUhDKYvxjooiQDXDnQhr0QqDt5tqr9wnDW4ikA=5DlrODjbIaQeqqadidi2q2DWTLq2AewDNsD5Uq7KmlzK15WIltzmhWk29Sl5as/b9dIgxEg8/LN0pxFe4ep2mvEUlxCd99zQtRuKHQLAG83tfGRjgBudzFmjSDMLekR629L05d+6FRBG2joqKAwqwjhwD13dPM4X14SXX432tWb9/WSn+3q4OZjvT=uyYPbo4tuveW8rG3dOu8+xp=+5fqtSv2fSX+9vX0KCz//27LU2zbmg1BclwQn2IGvfeTbtuPZRU94Vh054BEYHxGB252MGrhzkOh6YHN3OS+8S3xVooUkdNCYrl7al=tCF6ik3lNqrCNSYeqmbMPadoiozndo=lhOlNxShohwQw8bbFHrF/jdqgYb43/GA1OIlLpwdrQDPD7jQmxYB4L8mftwq=wfzaRih+i32Rx3DGcDG75iDD===; tfstk=fRlnEQcsBvyQuEbJxfVBsitpdLvtRMN7k0C827EyQlrsykp7vAvrA0qL2v3-IumZjklBY0Na7zahUM5PLOuZtrrPLkre_NzLyy5rzbUrZWGtp_EpA70rD7xvDIdxR2w7anKYVwGfUSzVTyS8Y15wklWRnIdxR27L8E3wMUMd2mru47yUL17afzbUaJyySlzulTSE478i7laRaMPPalzayzCrp97Ucbkr_E_tdoV_Yrhg-Rvt8lyUKUUUKNu3b_RIsyVz02rGa_EQcChjS2KhGVFI7m4xvC5nmV3xORGkmB2I_qc_Kb_lCVebx5rIpHf_-D3oiRleyZEE14lbISv5DlGS7xebgMWUoWq33XoOzEPUUzPUAvKh1ymKrj2ZTUBY5YosZWF2GMr-BqcurbtWsm0tkXwsZnAN4XWN3edANP8-_T67LPagcsgZYw1XlemJSFXAFJz_ooLMST65c_5RCFYGhyyU5yqF.; ZL_REPORT_GLOBAL={%22jobs%22:{%22funczoneShare%22:%22dtl_best_for_you%22%2C%22recommandActionidShare%22:%22f728ebb2-c11d-47a7-ac77-0fbd604251a2-job%22}}; FSSBBIl1UgzbN7NP=5Rf_hZC38PdAqqqDiyg5geaiySKRawkfy2l90uag9VpAB_A9JCFqmA7uc3xCdwC3rCvBwzc33ucfTMZpYiWIt5wCU0eouEELw72RlgMBJ_VZK3IMOz5I5iq7vPq.9DzM45XlMP1ZlnhZIH_bK2z83oF0M5Bkhg4JtSfn_JgYLmlcuIkzKFm23QkuHXyVm2XhQHcMXSzdKqwv7hZeO7UcPYeQ1tXqDgv7eVFkqqRRAuvCUjQIFSJMq_y6aLsezpWYnNoFTSRWrbWB8sOwPOmMd76TecjRyT7u0rSE197.GDMwak2rOtitAoLJGxkeiMxyn42XEEGlv8YxCjSI9ovv10v");
        CloseableHttpResponse response = null;
        try{
            //HttpClient发起请求，获取响应
            response = httpClient.execute(httpGet);

            //解析相应，返回结果
            if(response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空，那就可以使用EntityUtils
                if(response.getEntity() != null){
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    return content;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            //关闭response
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回空串
        return "";
    }


    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url){

        //获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //创建httpGet请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(this.getConfig());

        CloseableHttpResponse response = null;
        try{
            //HttpClient发起请求，获取响应
            response = httpClient.execute(httpGet);

            //解析相应，返回结果
            if(response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空，那就可以使用EntityUtils
                if(response.getEntity() != null){
                    //下载图片
                    //创建图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));

                    //创建图片名，进行重命名
                    String picName = UUID.randomUUID().toString()+extName;

                    //下载图片
                    //声明OutPutStream
                    OutputStream outputStream = new FileOutputStream(new File("G:\\DaSiXia\\picture\\"+picName));
                    response.getEntity().writeTo(outputStream);

                    //返回图片名称
                    return picName;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            //关闭response
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回空串
        return "";
    }


    /**
     * 设置请求信息
     * @return
     */
    private  RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)       //创建连接的最长时间
                .setConnectionRequestTimeout(5000)   //获取连接的最长时间
                .setSocketTimeout(100000)            //数据传输的最长时间
                .build();
        return config;
    }




}

