package com.afelio.springbootjpa.models.service;
import com.afelio.springbootjpa.models.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    public List<Usuario> findAll();
    public void save(Usuario usuario);
    public void deleteById(Long id);
    public Usuario findOne(Long id);
    public Page<Usuario> findAll(Pageable pageable);
}
