package cn.itcast.job.service.Impl;

import cn.itcast.job.mapper.JobInfoMapper;
import cn.itcast.job.pojo.JobInfo;
import cn.itcast.job.service.JobInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {


    @Autowired
    private JobInfoMapper jobInfoMapper;

    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        // 创建 QueryWrapper 对象
        QueryWrapper<JobInfo> queryWrapper = new QueryWrapper<>();

        // 根据JobInfo中的url和time字段构建查询条件
        if (jobInfo.getJobName() == null || jobInfo.getJobName().equals("")) {
            return;
        }
        queryWrapper.lambda().eq(JobInfo::getJobUrl, jobInfo.getJobUrl());
        if (jobInfo.getJobTime() != null) {
            queryWrapper.lambda().eq(JobInfo::getJobTime, jobInfo.getJobTime());
        }

        //执行查询
        System.out.println("查询工作信息是否存在" + jobInfo.getJobUrl());
        List<JobInfo> list = this.jobInfoMapper.selectList(queryWrapper);

        //判断查询结果是否为空
        if(list.size() == 0){
            //如果查询结果为空，表示招聘信息数据不存在，或者已经更新了，需要新增或者更改数据库
            System.out.println("\u001B[32m[SUCCESS] " + "新增工作信息" + jobInfo.getJobUrl() + "工作名称" + jobInfo.getJobName() + "\u001B[0m");
            this.jobInfoMapper.insert(jobInfo);
        }
    }

}
