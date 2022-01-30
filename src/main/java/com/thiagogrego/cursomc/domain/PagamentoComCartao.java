package com.thiagogrego.cursomc.domain;

import javax.persistence.Entity;

import com.thiagogrego.cursomc.domain.enums.EstadoPagamento;

@SuppressWarnings("serial")
@Entity
public class PagamentoComCartao extends Pagamento {

	private Integer numeroDeParcelas; 
	
	public PagamentoComCartao() {
		
	}

	public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
		super(id, estado, pedido);
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

}
