package restaurante.agentes.cliente;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class Cliente extends Agent
{
	private SequentialBehaviour comportamentos;
	private double valorAPagar = 10.0;
	
	@Override
	protected void setup()
	{
		super.setup();
		
		System.out.println("Cliente " + getLocalName() + " entrando no restaurante!");
		
		comportamentos = new SequentialBehaviour(this)
		{
			public int onEnd()
			{
                myAgent.doDelete();
                return 0;
            }
		};
		
		comportamentos.addSubBehaviour(new ChamarGarcom(this, 5000));
		
		addBehaviour(comportamentos);
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		System.out.println("Cliente " + getLocalName() + " saindo do restaurante!");
	}

	public SequentialBehaviour getComportamentos() {
		return comportamentos;
	}

	public void setComportamentos(SequentialBehaviour comportamentos) {
		this.comportamentos = comportamentos;
	}

	public double getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(double valorAPagar) {
		this.valorAPagar = valorAPagar;
	}
}
