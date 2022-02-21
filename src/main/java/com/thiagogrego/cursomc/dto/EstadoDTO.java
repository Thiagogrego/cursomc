package com.thiagogrego.cursomc.dto;

import java.io.Serializable;

import com.thiagogrego.cursomc.domain.Estado;

@SuppressWarnings("serial")
public class EstadoDTO implements Serializable{

	private Integer id;
	private String nome;
	
	public EstadoDTO() {
	
	}

	public EstadoDTO(Estado estado) {
		id = estado.getId();
		nome = estado.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}

