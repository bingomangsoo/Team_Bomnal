package com.jobfinder.persistence;

import com.jobfinder.domain.Recruit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecruitMapper {
    public List<Recruit> recruitlist();
}
