package com.thiagogrego.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.thiagogrego.cursomc.domain.Categoria;
import com.thiagogrego.cursomc.domain.Cliente;
import com.thiagogrego.cursomc.domain.Cliente;
import com.thiagogrego.cursomc.dto.ClienteDTO;
import com.thiagogrego.cursomc.repositories.ClienteRepository;
import com.thiagogrego.cursomc.services.exceptions.DataIntegrityException;
import com.thiagogrego.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = repo.findById(id);
		
		if(cliente.isEmpty()) {
			return cliente.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		}
		return cliente.get();	
	}
	
	public Cliente update(Cliente cliente) {
		
		Cliente newCliente = findById(cliente.getId());
		upadateData(newCliente, cliente);
		return repo.save(newCliente);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há entidades relacionadas");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	private void upadateData(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());
	}
}
