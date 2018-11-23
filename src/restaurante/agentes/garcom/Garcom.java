package restaurante.agentes.garcom;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import restaurante.agentes.Funcionario;

public class Garcom extends Funcionario
{
	@Override
	protected void setup()
	{
		super.setup();
		
		addBehaviour(new ComportamentoGarcom(this));
	}
	
	@Override
	protected void takeDown() 
	{
		super.takeDown();
		
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
