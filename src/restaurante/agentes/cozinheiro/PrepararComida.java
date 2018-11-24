package restaurante.agentes.cozinheiro;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import restaurante.Const;
import restaurante.agentes.cliente.Cliente;
import restaurante.agentes.cliente.FazerPedido;
import restaurante.cardapio.Pedido;

class PrepararComida extends TickerBehaviour 
{
	private static int TEMPO_OLHAR_PEDIDOS = 1000;
	private static int TEMPO_PREPARO = 5000;
	
	DFAgentDescription dfd;
	
	public PrepararComida(Agent a) 
	{
		super(a, TEMPO_OLHAR_PEDIDOS);
		
		// Registrar na página amarela para chamar o garçom
		dfd = new DFAgentDescription();
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Const.CHAMAR_GARCOM_COZINHA);
		
		dfd.addServices(sd);
	}

	@Override
	protected void onTick() 
	{
		if(Cozinha.temPedido())
		{
			Pedido p = Cozinha.retirarPedido();
			
			System.out.println(myAgent.getLocalName() + ": Fazendo " + p.getItem().getNome() + " para " + p.getCliente().getLocalName() + "!");
			
			try 
			{
				Thread.sleep(TEMPO_PREPARO);
				chamarGarcons(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			block();
	}

	private void chamarGarcons(Pedido p) throws IOException 
	{
		try
		{
			DFAgentDescription[] result = DFService.search(myAgent, dfd);
			
			for(int i = 0; i < result.length; i++)
			{
				AID garcom = result[i].getName();
				
				if(entregarPedido(garcom, p))
					break;
			}
		}
		catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	private boolean entregarPedido(AID garcom, Pedido p) throws IOException
	{
		if(garcom == null)
			return false;
		
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		
		msg.addReceiver(garcom);
		msg.setConversationId(Const.CHAMAR_GARCOM_COZINHA);
		msg.setContentObject(p);
		myAgent.send(msg);
		
		System.out.println(myAgent.getLocalName() + ": entregando para " + garcom.getLocalName());
		
		return true;
	}
}
