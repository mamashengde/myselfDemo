package cn.itcast.job.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "job_info")
@Data
public class JobInfo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 工作名称
    private String jobName;

    // 工作地址
    private String jobAddr;

    // 工作类型（不限0，全职1，兼职/临时2，实习3，校园4，其他5）
    private int jobType;

    // 薪水范围-最低，单位元
    private String salaryMin;

    // 薪水范围-最高，单位元
    private String salaryMax;

    // 每年发多少个月工资
    private String salaryMonth;

    // 学历需求（不限0，初中及以下1，高中2，中专/中技3，大专4，本科5，硕士6，MBA/EMBA7，博士8）
    private Integer requiredDegree;

    // 招聘人数
    private Integer requiredNumber;

    // 工作经验（经验不限0，无经验1，1年以下2，1-3年3，3-5年4，5-10年5，10年以上6，未知7)
    private Integer requiredExperience;

    // 详情url
    @TableField("job_url")
    private String jobUrl;

    // 发布时间
    private String jobTime;

    // 工作内容详细
    private String jobContent;

    // 公司ID
    private String companyId;

}
