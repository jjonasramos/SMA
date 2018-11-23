package restaurante.cardapio;

import java.io.Serializable;

public class ItemCardapio implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3689332462065050463L;
	private String nome;
	private double preco;
	
	public ItemCardapio(String nome, double preco) {
		super();
		this.nome = nome;
		this.preco = preco;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemCardapio [nome=");
		builder.append(nome);
		builder.append(", preco=");
		builder.append(preco);
		builder.append("]");
		return builder.toString();
	}	
}
