package com.thiagogrego.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagogrego.cursomc.domain.Cliente;
import com.thiagogrego.cursomc.repositories.ClienteRepository;
import com.thiagogrego.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = repo.findById(id);
		
		if(cliente.isEmpty()) {
			return cliente.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		}
		
		return cliente.get();
		
	}
}
