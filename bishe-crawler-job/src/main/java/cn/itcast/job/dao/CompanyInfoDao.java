package cn.itcast.job.dao;

import cn.itcast.job.pojo.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoDao extends JpaRepository<CompanyInfo,String> {

}
