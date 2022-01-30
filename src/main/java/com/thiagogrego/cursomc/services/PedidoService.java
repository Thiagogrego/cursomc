package com.thiagogrego.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagogrego.cursomc.domain.Pedido;
import com.thiagogrego.cursomc.repositories.PedidoRepository;
import com.thiagogrego.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	public Pedido findById(Integer id) {
		Optional<Pedido> pedido = repo.findById(id);
		
		if(pedido.isEmpty()) {
			return pedido.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		}
		
		return pedido.get();
		
	}
}
