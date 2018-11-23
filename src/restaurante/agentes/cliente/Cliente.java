package restaurante.agentes.cliente;

import jade.core.Agent;

public class Cliente extends Agent
{
	@Override
	protected void setup()
	{
		super.setup();
		
		System.out.println("Cliente " + getLocalName() + " entrando no restaurante!");
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		System.out.println("Cliente " + getLocalName() + " saindo do restaurante!");
	}
}
