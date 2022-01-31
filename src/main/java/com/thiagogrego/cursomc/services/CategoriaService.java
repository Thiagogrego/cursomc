package com.thiagogrego.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagogrego.cursomc.domain.Categoria;
import com.thiagogrego.cursomc.repositories.CategoriaRepository;
import com.thiagogrego.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria findById(Integer id) {
		Optional<Categoria> categoria = repo.findById(id);
		
		if(categoria.isEmpty()) {
			return categoria.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		}
		
		return categoria.get();
		
	}
	
	public Categoria save(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
	
	public Categoria update(Categoria categoria) {
		findById(categoria.getId());
		return repo.save(categoria);
	}
}
