package com.wave.payroll.dao;

import org.springframework.data.repository.CrudRepository;

import com.wave.payroll.domain.JobGroup;

public interface IJobGroupRepository extends CrudRepository<JobGroup, Long>{
	public JobGroup findOneByName(String name);
}
