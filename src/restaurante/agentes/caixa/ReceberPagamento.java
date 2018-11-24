package restaurante.agentes.caixa;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import restaurante.agentes.cliente.Cliente;

public class ReceberPagamento extends TickerBehaviour 
{
	private static int TEMPO_OLHAR_CAIXA = 1000;
	private static int TEMPO_RECEBER_PAGAMENTO = 2000;

	public ReceberPagamento(Agent a) {
		super(a, TEMPO_OLHAR_CAIXA);
	}

	@Override
	protected void onTick() 
	{
		try
		{
			if(FilaCaixa.temCliente())
			{
				chamarCliente();
				Thread.sleep(TEMPO_RECEBER_PAGAMENTO);
				receberPagamento();
			}
			else
				block();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void chamarCliente()
	{
		Cliente cliente = FilaCaixa.chamarCliente();
		System.out.println(myAgent.getLocalName() + ": Próximo!");
		
		ACLMessage proximo = new ACLMessage(ACLMessage.INFORM);
		proximo.addReceiver(cliente.getAID());
		myAgent.send(proximo);
	}
	
	private void receberPagamento() throws UnreadableException
	{
		ACLMessage pagamento = myAgent.receive();
		
		double dinheiro = (double) pagamento.getContentObject();
		
		System.out.println(myAgent.getLocalName() + ": Recebi R$ " + dinheiro + "! Pode ir!");
		
		ACLMessage reply = pagamento.createReply();
		myAgent.send(reply);
	}

}
