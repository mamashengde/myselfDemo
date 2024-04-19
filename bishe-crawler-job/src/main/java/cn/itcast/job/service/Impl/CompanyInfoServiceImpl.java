package cn.itcast.job.service.Impl;

import cn.itcast.job.dao.CompanyInfoDao;
import cn.itcast.job.mapper.CompanyInfoMapper;
import cn.itcast.job.pojo.CompanyInfo;
import cn.itcast.job.service.CompanyInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Override
    @Transactional
    public void save(CompanyInfo companyInfo) {
        //根据公司名称去查找公司信息是否存在
//        System.out.println("执行查询公司是否存在:" + companyInfo.getCompanyName());
        QueryWrapper<CompanyInfo> queryWrapper = new QueryWrapper<>();
        if(companyInfo.getCompanyName()!= null){
            queryWrapper.lambda().eq(CompanyInfo::getCompanyName, companyInfo.getCompanyName());
        }
        //执行查询
        List<CompanyInfo> list = companyInfoMapper.selectList(queryWrapper);

        //判断查询结果是否为空
        if(list.size() == 0){
            //如果查询结果为空，表示公司信息数据不存在，或者已经更新了，需要新增或者更改数据库
            this.companyInfoMapper.insert(companyInfo);
//            System.out.println("公司不存在，插入成功" + companyInfo.getCompanyName());
        }
    }
}
