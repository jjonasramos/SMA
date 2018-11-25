package restaurante.cardapio;

import java.io.Serializable;

import jade.core.AID;
import jade.core.Agent;
import restaurante.agentes.cliente.Cliente;

public class Pedido implements Serializable
{
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 3395022724889221398L;
	
	private AID cliente;
	private ItemCardapio item;
	
	public Pedido(AID cliente, ItemCardapio item) {
		super();
		this.cliente = cliente;
		this.item = item;
	}

	public AID getCliente() {
		return cliente;
	}

	public void setCliente(AID cliente) {
		this.cliente = cliente;
	}

	public ItemCardapio getItem() {
		return item;
	}

	public void setItem(ItemCardapio item) {
		this.item = item;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
