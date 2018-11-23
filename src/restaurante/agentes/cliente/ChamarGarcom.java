package restaurante.agentes.cliente;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import restaurante.Const;
import restaurante.cardapio.Cardapio;
import restaurante.cardapio.Pedido;

class ChamarGarcom extends TickerBehaviour 
{
	private final int QTD_DE_CHAMADAS = 3;
	
	private DFAgentDescription dfd;
	
	public ChamarGarcom(Agent a, long period) 
	{
		super(a, period);
		
		// Registrar na página amarela para chamar o garçom
		dfd = new DFAgentDescription();
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Const.CHAMAR_GARCOM);
		
		dfd.addServices(sd);
	}

	@Override
	protected void onTick() 
	{
		if(getTickCount() >= QTD_DE_CHAMADAS)
		{
			System.out.println(myAgent.getLocalName() + ": Esse restaurante não presta, vou embora!");
			myAgent.doDelete();
		} else
			try {
				chamarGarcons();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	private void chamarGarcons() throws InterruptedException
	{
		System.out.println(myAgent.getLocalName() + ": Garçom, por favor!");
		
		try
		{
			DFAgentDescription[] result = DFService.search(myAgent, dfd);
			
			for(int i = 0; i < result.length; i++)
			{
				AID garcom = result[i].getName();
				
				if(chamar(garcom))
				{
					Cliente c = (Cliente) myAgent;
					
					c.getComportamentos().addSubBehaviour(new FazerPedido(myAgent, garcom));
					this.stop();
					break;
				}
			}
		}
		catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	private boolean chamar(AID garcom) throws InterruptedException
	{
		if(garcom == null)
			return false;
		
		// envia sua identificacao para todos os vendedores
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		
		cfp.addReceiver(garcom);
		cfp.setConversationId(Const.CHAMAR_GARCOM);
		myAgent.send(cfp);
		
		Thread.sleep(5000);
		
		// Receber a mensagem do garçom
		ACLMessage reply = myAgent.receive();
		
		if(reply == null)
			return false;
		
		return reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL;
	}
}
