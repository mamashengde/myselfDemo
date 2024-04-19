package cn.itcast.job.task;

import cn.itcast.job.pojo.CompanyInfo;
import cn.itcast.job.pojo.JobInfo;
import cn.itcast.job.utils.ChromeDownloader;
import cn.itcast.job.utils.InfoHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class JobProcessor implements PageProcessor {

    private String zpUrl = "https://sou.zhaopin.com/?jl=765&kw=python&p=";
    private long PageIndex = 1;
    private InfoHandler infoHandler = new InfoHandler();
    private final String SubUrl = "&preactionid";
    private long InsertId = 1;

    @Autowired
    private SpringDataPipeline pipeline;

    public void process(Page page) {

        Document document = Jsoup.parse(page.getHtml().toString());
        List<Selectable> list = page.getHtml().css("div.positionlist > div.joblist-box__item").nodes();

        if (list.size() == 0) {
            //如果为空，表示这是招聘详情页，解析页面，获取详情信息，保存数据
            this.saveInfo(page);
        } else {
            //如果不为空，表示这是列表页
            for (Selectable selectable : list) {
                //获取url地址
                String jobInfoUrl = selectable.links().toString();
                //添加详情页url到任务队列
                page.addTargetRequest(jobInfoUrl);
                System.out.println("a:" + jobInfoUrl);
            }

            //获取下一页的url
            String nextUrl = zpUrl + PageIndex+ "&" + UUID.randomUUID().toString();
            System.out.println("b:" + nextUrl);
            page.addTargetRequest(nextUrl);
            PageIndex++;
        }
    }


    /**
     * 解析页面，获取扎品详情信息，保存数据
     * @param page
     */
    private void saveInfo(Page page) {
        //保存数据分为保存job信息和company信息两部分
            //保存job信息
            JobInfo jobInfo = new JobInfo();
            CompanyInfo companyInfo = new CompanyInfo();
            Document doc = Jsoup.parse(page.getHtml().toString());

            //获取job信息
            String jobName = doc.select("h3.summary-plane__title").text();  //java开发工程师
            String jobTime = doc.select("div.summary-plane__other").text(); //更新于 4月12日/今天
            String[] salary = infoHandler.parseSalary(doc.select("span.summary-plane__salary").text());//15k-30k，面议，*15
            String[] jobElements = infoHandler.parseJobInfoString(doc.select("ul.summary-plane__info").text());
            String jobAddr = jobElements[0];
            String requiredExperience = jobElements[1];
            String requiredDegree = jobElements[2];
            String jobType = jobElements[3];
            String requiredNumber = jobElements[4];
            String jobContent = doc.select("div.describtion__detail-content").toString();
            String jobUrl = page.getUrl().toString();

            //设置job信息
            jobInfo.setJobName(jobName);
            jobInfo.setJobTime(infoHandler.parseUpdateDate(jobTime));
            jobInfo.setSalaryMin(salary[0]);
            jobInfo.setSalaryMax(salary[1]);
            jobInfo.setSalaryMonth(salary[2]);
            jobInfo.setJobAddr(jobAddr);
            jobInfo.setRequiredExperience(infoHandler.parseExperience(requiredExperience));
            jobInfo.setRequiredDegree(infoHandler.parseDegree(requiredDegree));
            jobInfo.setJobType(infoHandler.parseJobType(jobType));
            jobInfo.setRequiredNumber(infoHandler.parseRequiredNumber(requiredNumber));
            jobInfo.setJobContent(jobContent);
            jobInfo.setJobUrl(infoHandler.extractUrlBefore(jobUrl,SubUrl));

            //获取company信息
            String companyId = UUID.randomUUID().toString().replaceAll("-", "");
            String companyName = doc.select("a.company__title").text();
            String companyIndustry = doc.select("button.company__industry").text();
            String companySize = doc.select("button.company__size").text();
            String companyAddr = jobAddr;
            String companyUrl = doc.select("div.company > a").attr("href").toString();

            //设置company信息
            companyInfo.setCompanyId(companyId);
            companyInfo.setCompanyName(companyName);
            //TODO:公司行业和公司规模解析
            companyInfo.setCompanyIndustry(companyIndustry);
            companyInfo.setCompanySize(infoHandler.parseCompanySize(companySize));
            companyInfo.setCompanyAddr(companyAddr);
            companyInfo.setCompanyUrl(infoHandler.parseCompanyUrl(companyUrl));

            //保存数据到数据库
            page.putField("jobInfo-"+InsertId, jobInfo);
            page.putField("companyInfo-"+InsertId, companyInfo);
            if(jobInfo.getJobName()!=null && companyInfo.getCompanyName()!=null){
                System.out.println("page.putField到了第"+InsertId+"次");
                InsertId++;
            }
    }


    private Site site = Site.me()
            .setCharset("utf8")          //设置编码
            .setTimeOut(10 * 1000)       //设置超时时间
            .setRetrySleepTime(1000)     //设置重试间隔时间
            .setRetryTimes(3)            //设置重试次数
            .setSleepTime(1000)          //设置爬取间隔时间
            .addHeader("Referer", "https://www.zhaopin.com/")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0")
            .addCookie("cookies", "x-zp-client-id=e6c4d24c-800e-426d-b742-d1f8869d8ffb; sensorsdata2015jssdkchannel=%7B%22prop%22%3A%7B%22_sa_channel_landing_url%22%3A%22https%3A%2F%2Flanding.zhaopin.com%2Fresume-templates%3Fchannel_name%3D360_sem_track%26callback_id%3Dh5tU0XX3%26_data_version%3D0.5.0%26channel_utm_content%3Djl%26project%3Dzlclient%26channel_utm_medium%3Docpc%26tid%3Ds%26channel_link_type%3Dweb%26channel_utm_source%3D360PC%26hash_key%3DChLMjfS16C5zMRgPkydK%26sat_cf%3D2%26channel_utm_campaign%3Dxyjkay999%26channel_utm_term%3D771693%26_channel_track_key%3D7wuQAmYh%26link_version%3D1%26channel_keyword_id%3D17464499152%26channel_ad_id%3D10361649370%26channel_account_id%3D243985234%26device%3Dpc%26channel_adgroup_id%3D3721939131%26channel_campaign_id%3D2327961415%26qhclickid%3De0590e7b5d33de86%22%7D%7D; Hm_lvt_7fa4effa4233f03d11c7e2c710749600=1713277509; locationInfo_search={%22code%22:%222367%22%2C%22name%22:%22%E6%AD%A6%E6%B1%89%E5%90%B4%E5%AE%B6%E5%B1%B1%E7%BB%8F%E6%B5%8E%E6%8A%80%E6%9C%AF%E5%BC%80%E5%8F%91%E5%8C%BA%22%2C%22message%22:%22%E5%8C%B9%E9%85%8D%E5%88%B0%E5%B8%82%E7%BA%A7%E7%BC%96%E7%A0%81%22}; zp_passport_deepknow_sessionId=4a2f296fs5ceae4b108d68337c16d15476e6; at=d87a59c497af44b18779ffa442210b12; rt=cf6d41d3d74a413b99a9e1d522b2517f; _uab_collina=171327824182276414030784; Hm_lvt_ec66c70e779981d9449c691e22a09d17=1713278242; LastCity=%E5%8C%97%E4%BA%AC; LastCity%5Fid=530; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221180016066%22%2C%22first_id%22%3A%22189536dab0df0b-0282fc231dd95f2-7e56547f-1821369-189536dab0e1584%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg5NTM2ZGFiMGRmMGItMDI4MmZjMjMxZGQ5NWYyLTdlNTY1NDdmLTE4MjEzNjktMTg5NTM2ZGFiMGUxNTg0IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTE4MDAxNjA2NiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%221180016066%22%7D%2C%22%24device_id%22%3A%22189536dab0df0b-0282fc231dd95f2-7e56547f-1821369-189536dab0e1584%22%7D; selectCity_search=538; Hm_lpvt_7fa4effa4233f03d11c7e2c710749600=1713454867; acw_tc=276077db17134979770421876e2ecc1e45d9a28344a42a805009d36a2d9b63; acw_sc__v2=6621e77968df453cbadc3a605494d99af06e84d2; ssxmod_itna=QqUxcD2D9QQGqGKGHimeriqYY5zqiKP4iEh+dD/iQmDnqD=GFDK40ooOY5D7mDgxRDN5eb=C0NzMFxfH3GiB6+pwWTZOx0aDbqGkq36i4GGjxBYDQxAYDGDDpuDzTtGuD0KGRD0bkDFxi1yx08DeIH6hDDUQG+lD5DTx01CxGODi5m0WYDmp5DRcKDep5D92PDwpHmD4G7UDfCDm4ClDDzlmdNn0wALoLjZ=EpOUEvhRgvCa3ARIRC9EHkDmmHAfYCiwHzR/qei=hNtohxLQD4t0hW=Qh4t04x3YbbDOD5YGDNdB0PbmpVDDflOx2YD=; ssxmod_itna2=QqUxcD2D9QQGqGKGHimeriqYY5zqiKP4iEh4G9iMDBqEqx7PiD7DvxgG7=+x8xbQPj4SLvx+VS=n2XIqolThiFrZP1BWwWrYu5v7ef3p8uLWxMMEd8VDMGcGsR50RlW28HRToag/mqE+4bjVmkAV48AklrK27liF8L20Cg5O92LkeTI9Kfu7i8bOFQI9BlbUbPoK1mpk8T0s9QUOZuniTR58Om9+mxoqEfAkGudGL1ykaUW3kKgzE8n3kaSa6nOdNpPpkq4iBFtNgUv2x3KbgtYuvSIhWqZ7GkdmhX7cws5sIRcZvoy6BtUcU31UYrNDKmq3581UKR07AXGn0oF0kCqmDABZtPbP=9NicP6Pwxiw8=t2=QCoIz0QM7LhmIDg4YaEUn5mAhg7uVQpN=YU0YsS+M0P6netcRcl+sGPHBPvfTVpuVGG6OPPh4DQIa=Go68Q/iWKGxSrHVfQFD=SrqIbPKDDFqD+rqxD; acw_sc__v3=6621ece3238ea6933a6f2476dbe9a36a6d6021c5; tfstk=fw--F8vzLjcoko3HVLgD8uiMWrDmj3pPhQJ_x6fuRIdvwIkUqpa3pMdXdLbHdbRpvste4T17AisfC9e5Oe5Bhs6OIyw5OT9bcsd7xaxpLkBBdBkPtYuDULSFAfcGvcvyUXljKtK8PsNX39z7PB0dvujFAfc0ok_rXMRieGDqyKMAK9U7OB_ScxBhKMZ5RuabGsWCA6OCRK_fL9EQA66CGvnEys25HkKqextbJDBzAktSYaCtesWVnne5z1pW2kZQ5TQR1L1qsYakrZ9FJHyUv_9W7BW6N7GVtFvDVZIKSW5p-Ltk2MyUkKQpqe7p4ANN6U9vvgA-6y56rKsfaUG3P_tyIiLCkYiWHgCJ_3O-XjO1W3YFJ3k__1LWHHIk2v4PUEOPDt-ngu1pletlzMPSg6YMn3_Afg82jhpOr5fOKzMxHyzFPtxZkTfhdQuXUtCiFYaU8w6VH1DxHyzFPtWAsYd88y7C3; Hm_lpvt_ec66c70e779981d9449c691e22a09d17=1713499634; ZL_REPORT_GLOBAL={%22jobs%22:{%22funczoneShare%22:%22dtl_best_for_you%22%2C%22recommandActionidShare%22:%22b3e19752-f770-4ddb-8eb0-4526a3071816-job%22}%2C%22company%22:{%22actionid%22:%2281825803-8bd0-47ae-9639-40041b3402fd-company%22%2C%22funczone%22:%22hiring_jd%22}%2C%22//www%22:{%22seid%22:%22d87a59c497af44b18779ffa442210b12%22%2C%22actionid%22:%225835515c-386b-461a-b8f0-a4b2da1eef0b-cityPage%22}}");


    @Override
    public Site getSite() {
        return site;
    }

    //initialDelay：第一次执行任务的延迟时间，单位为毫秒。
    //fixedDelay：任务执行间隔时间，单位为毫秒。
    int scheduledIndex = 1;
    @Scheduled(initialDelay = 1000,fixedDelay = 1*1000)
    public void process() {
        System.out.println("定时任务什么玩意？----" + scheduledIndex);
        Spider.create(new JobProcessor())
                .addUrl(zpUrl + PageIndex)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000)))
                .setDownloader(new ChromeDownloader())
                .thread(1)
                .addPipeline(pipeline)
                .run();
    }
}
