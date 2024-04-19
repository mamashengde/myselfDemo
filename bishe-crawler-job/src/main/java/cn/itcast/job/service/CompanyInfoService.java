package cn.itcast.job.service;

import cn.itcast.job.pojo.CompanyInfo;


import java.util.List;

public interface CompanyInfoService {

    /**
     * 保存公司信息
     * @param companyInfo
     */
    public void save(CompanyInfo companyInfo);

}
