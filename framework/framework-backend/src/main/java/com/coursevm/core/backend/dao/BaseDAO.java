package com.coursevm.core.backend.dao;

import com.coursevm.core.backend.repository.CommandRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDAO<T, ID extends Serializable> extends CommandRepository<T, ID> {

}
