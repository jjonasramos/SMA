package restaurante.cardapio;

import jade.core.Agent;

public class Pedido 
{
	private Agent agente;
	private ItemCardapio item;
	
	public Pedido(Agent agente, ItemCardapio item) {
		super();
		this.agente = agente;
		this.item = item;
	}
	
	public Agent getAgente() {
		return agente;
	}
	public void setAgente(Agent agente) {
		this.agente = agente;
	}
	public ItemCardapio getItem() {
		return item;
	}
	public void setItem(ItemCardapio item) {
		this.item = item;
	}
}
