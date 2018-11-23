package restaurante.agentes.garcom;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import restaurante.Const;

class ComportamentoGarcom extends Behaviour 
{
	private static final int ESPERANDO = 0;
	
	private static final int ATENDER_CLIENTE = 1;
	private static final int ANOTANDO_PEDIDO = 2;
	private static final int LEVANDO_PEDIDO_COZINHA = 3;
	
	private static final int RECEBENDO_PRATO = 4;
	private static final int ENTREGANDO_PRATO_CLIENTE = 5;
	
	private int estado = ESPERANDO;
	
	public ComportamentoGarcom(Agent a)
	{
		super(a);
		
		// Registrar o garçom nas páginas amarelas de cliente e cozinha
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(a.getAID());
		
		ServiceDescription sdCliente = new ServiceDescription();
		sdCliente.setType(Const.CHAMAR_GARCOM);
		sdCliente.setName("Chamar " + myAgent.getLocalName());
		
		ServiceDescription sdCozinha = new ServiceDescription();
		sdCozinha.setType(Const.CHAMAR_GARCOM_COZINHA);
		sdCozinha.setName("Chamar " + myAgent.getLocalName() + " p/ cozinha");
		
		dfd.addServices(sdCliente);
		dfd.addServices(sdCozinha);
		
		try {
			DFService.register(a, dfd);
		}
		catch (FIPAException fe) {
			 fe.printStackTrace();
		}
	}
	
	@Override
	public void action() 
	{
		switch(estado)
		{
		case ESPERANDO:
			ACLMessage msg = myAgent.receive();
			
			if(msg != null)
				switch(msg.getPerformative())
				{
				case ACLMessage.CFP:
					responderChamadoCliente(msg);
					estado = ATENDER_CLIENTE;
					break;
				}
			else
				block();
			
			break;
			
		case ATENDER_CLIENTE:
			break;
		}
	}
	
	private void responderChamadoCliente(ACLMessage msg)
	{
		System.out.println("Walt msg " + msg.getSender().getLocalName());
		
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		myAgent.send(reply);
		
		System.out.println(myAgent.getLocalName() + ": Olá " + msg.getSender().getLocalName() + ". O que vai querer?");
	}

	@Override
	public boolean done() {
		return false;
	}
}
