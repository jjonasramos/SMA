package restaurante.cardapio;

import java.util.Random;

public abstract class Cardapio 
{
	/**
	 * Podia ser um hashmap, mas por simplicidade deixei
	 * como lista mesmo.
	 */
	private static ItemCardapio[] itens = new ItemCardapio[] 
	{
			new ItemCardapio("Batata Frita", 9.90),
			new ItemCardapio("Milkshake", 12.99),
			new ItemCardapio("Hamburguer", 14.99),
			new ItemCardapio("Pizza", 29.99),
			new ItemCardapio("Pão de Queijo", 59.99)
	};
	
	public static ItemCardapio selecionarItem()
	{
		int random = new Random().nextInt(itens.length);
		
		return itens[random];
	}
}
