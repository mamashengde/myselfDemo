package cn.itcast.job.service;

import cn.itcast.job.pojo.JobInfo;

import java.util.List;

public interface JobInfoService {

    /**
     * 保存工作信息
     * @param jobInfo
     */
    public void save(JobInfo jobInfo);

}
