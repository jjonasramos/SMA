package restaurante.agentes.caixa;

import java.util.LinkedList;
import java.util.Queue;

import restaurante.agentes.cliente.Cliente;

public class FilaCaixa 
{
	private static Queue<Cliente> filaCaixa = new LinkedList<Cliente>();
	
	public static void entrar(Cliente c){
		filaCaixa.add(c);
	}
	
	public static Cliente chamarCliente() {
		return filaCaixa.remove();
	}
	
	public static boolean temCliente() {
		return !filaCaixa.isEmpty();
	}
}