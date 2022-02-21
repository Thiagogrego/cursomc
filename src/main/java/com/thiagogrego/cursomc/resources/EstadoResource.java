package com.thiagogrego.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thiagogrego.cursomc.domain.Cidade;
import com.thiagogrego.cursomc.domain.Estado;
import com.thiagogrego.cursomc.dto.CidadeDTO;
import com.thiagogrego.cursomc.dto.EstadoDTO;
import com.thiagogrego.cursomc.services.CidadeService;
import com.thiagogrego.cursomc.services.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	ResponseEntity<List<EstadoDTO>> findAllByOrderByNome(){
		List<Estado> estados = estadoService.findAllByOrderByNome();
		List<EstadoDTO>estadoDTO = estados.stream().map(dto -> new EstadoDTO(dto)).collect(Collectors.toList());
		return ResponseEntity.ok().body(estadoDTO);
	}
	
	@GetMapping("/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId){
	List<Cidade> cidades = cidadeService.findByEstado(estadoId);
	List<CidadeDTO>cidadeDTO = cidades.stream().map(dto -> new CidadeDTO(dto)).collect(Collectors.toList());
	return ResponseEntity.ok().body(cidadeDTO);
	}
}
