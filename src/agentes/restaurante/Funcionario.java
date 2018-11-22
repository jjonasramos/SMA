package agentes.restaurante;

import jade.core.Agent;

/**
 * Classe só para padronizar os prints do setup e takeDown para funcionários do restaurante
 * 
 * @author Junior
 *
 */
public abstract class Funcionario extends Agent 
{
	@Override
	protected void setup()
	{
		super.setup();
		
		System.out.println(this.getClass().getSimpleName() + " " + getLocalName() + " contratado!");
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		System.out.println(this.getClass().getSimpleName() + " " + getLocalName() + " demitido!");
	}
}
