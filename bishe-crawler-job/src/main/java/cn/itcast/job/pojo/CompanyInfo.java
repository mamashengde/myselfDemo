package cn.itcast.job.pojo;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "company_info")
@Data
public class CompanyInfo {
    @Id
    private String companyId;

    //公司名称
    private String companyName;

    //公司地址
    private String companyAddr;

    //公司规模（20人以下0，20-99人1，100-299人2，300-499人3，500-999人4，1000-9999人5，10000人以上6）
    private int companySize;

    //公司行业（加入之前先去查找，从表里查找）
    private String companyIndustry;

    //公司性质（不限0，国企1，外企2，合资3，民营4，上市公司5，股份制企业6，事业单位7，其他8）
    private int companyType;

    //公司详情url
    private String companyUrl;
}
