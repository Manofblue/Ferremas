package com.ferremas.serviceImpl;

import com.ferremas.model.Transferencia;
import com.ferremas.repository.TransferenciaRepository;
import com.ferremas.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Override
    public List<Transferencia> findAll() {
        return transferenciaRepository.findAll();
    }

    @Override
    public Optional<Transferencia> findById(Integer id) {
        return transferenciaRepository.findById(id);
    }

    @Override
    public Transferencia save(Transferencia transferencia) {
        return transferenciaRepository.save(transferencia);
    }

    @Override
    public void deleteById(Integer id) {
        transferenciaRepository.deleteById(id);
    }

    @Override
    public List<Transferencia> findByPedidoId(Integer idPedido) {
        return transferenciaRepository.findByPedidoIdPedido(idPedido);
    }
}
