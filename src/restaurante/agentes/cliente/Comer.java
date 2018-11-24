package restaurante.agentes.cliente;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import restaurante.cardapio.ItemCardapio;

class Comer extends WakerBehaviour
{
	private final static long TEMPO_COMER = 1000;

	private Cliente cliente;
	private ItemCardapio comida;
	
	public Comer(Agent a, ItemCardapio ic) {
		super(a, TEMPO_COMER);
		
		this.comida = ic;
		this.cliente = (Cliente) a;
	}
	
	protected void onWake() 
	{
        System.out.println(myAgent.getLocalName() + ": terminei de comer " + comida.getNome() + "!\n");
        
        cliente.getComportamentos().addSubBehaviour(new PagarNoCaixa(cliente));
    }
}
