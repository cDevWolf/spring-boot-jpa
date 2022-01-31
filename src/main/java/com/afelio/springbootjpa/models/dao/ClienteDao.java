package com.afelio.springbootjpa.models.dao;

import com.afelio.springbootjpa.models.entity.Usuario;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClienteDao extends PagingAndSortingRepository<Usuario,Long> {

}
