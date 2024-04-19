package cn.itcast.job.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "company_industry")
public class companyIndustry {
    //公司行业ID
    @Id
    @TableId(value = "industry_id")
    private int industryId;

    //公司行业名称
    @TableField(value = "industry_name")
    private String industryName;
}
