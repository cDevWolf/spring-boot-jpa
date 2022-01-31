package com.afelio.springbootjpa.models.service;

import com.afelio.springbootjpa.models.dao.ClienteDao;
import com.afelio.springbootjpa.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IClienteService implements ClienteService{
    @Autowired
    ClienteDao clienteDao;
    @Override
    @Transactional
    public List<Usuario> findAll() {
        return (List<Usuario>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public void save(Usuario usuario) {
        clienteDao.save(usuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findOne(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }


}
