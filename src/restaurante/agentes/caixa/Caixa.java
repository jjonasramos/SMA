package restaurante.agentes.caixa;

import jade.core.Agent;
import restaurante.agentes.Funcionario;

public class Caixa extends Funcionario
{
	@Override
	protected void setup()
	{
		super.setup();
		
		addBehaviour(new ReceberPagamento(this));
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
	}
}
