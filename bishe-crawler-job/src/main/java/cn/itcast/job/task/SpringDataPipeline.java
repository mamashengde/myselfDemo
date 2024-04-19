package cn.itcast.job.task;

import cn.itcast.job.pojo.CompanyInfo;
import cn.itcast.job.pojo.JobInfo;
import cn.itcast.job.service.CompanyInfoService;
import cn.itcast.job.service.JobInfoService;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private JobInfoService jobInfoService;

    @Autowired
    private CompanyInfoService companyInfoService;

    private long id = 1;
    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的招聘详情对象

        JobInfo jobInfo = resultItems.get("jobInfo-" + id);
        CompanyInfo companyInfo = resultItems.get("companyInfo-"+id);
        System.out.println("提取id了resultItem-" + id + "的内容");

        //判断数据是否不为空
        System.out.println("jobInfo-" + id + ":" + jobInfo.getJobName());
        System.out.println("companyInfo-:" + id + ":" + companyInfo.getCompanyName());
        if (jobInfo!= null) {
            //保存数据到数据库中
            this.jobInfoService.save(jobInfo);
            id++;
        }
        if(companyInfo!= null){
            this.companyInfoService.save(companyInfo);
        }
        System.out.println("\u001B[32m[SUCCESS] " + "数据保存完毕: " + (id-1) + "次" + "\u001B[0m");
    }
}
