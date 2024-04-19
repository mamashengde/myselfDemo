package cn.itcast.job.mapper;

import cn.itcast.job.pojo.JobInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobInfoMapper extends BaseMapper<JobInfo> {
}
