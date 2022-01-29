package com.thiagogrego.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagogrego.cursomc.domain.Categoria;
import com.thiagogrego.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Optional<Categoria> findById(Integer id) {
		Optional<Categoria> categoria = repo.findById(id);
		return categoria;
	}
}
