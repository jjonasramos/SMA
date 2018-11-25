package restaurante.agentes.cozinheiro;

import java.util.LinkedList;
import java.util.Queue;

import restaurante.cardapio.Pedido;

public class Cozinha 
{
	private static Queue<Pedido> filaPedidos = new LinkedList<Pedido>();
	
	public static void colocarPedido(Pedido p){
		filaPedidos.add(p);
	}
	
	public static Pedido retirarPedido() {
		return filaPedidos.remove();
	}
	
	public static boolean temPedido() {
		return !filaPedidos.isEmpty();
	}
}
