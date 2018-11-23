package restaurante.agentes.cozinheiro;

import jade.core.Agent;
import restaurante.agentes.Funcionario;

public class Cozinheiro extends Funcionario
{
	@Override
	protected void setup()
	{
		super.setup();
		
		addBehaviour(new PrepararComida(this));
	}
	
	@Override
	protected void takeDown() 
	{
		super.takeDown();
	}
}
