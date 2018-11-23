package restaurante.agentes.garcom;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import restaurante.Const;
import restaurante.agentes.cliente.Cliente;
import restaurante.agentes.cozinheiro.Cozinha;
import restaurante.cardapio.ItemCardapio;
import restaurante.cardapio.Pedido;

class ComportamentoGarcom extends Behaviour 
{
	private static final int ESPERANDO = 0;
	
	private static final int ATENDER_CLIENTE = 1;
	
	private static final int ENTREGANDO_PRATO_CLIENTE = 2;
	
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
				switch(msg.getConversationId())
				{
				// Cliente chamando
				case Const.CHAMAR_GARCOM:
					responderChamadoCliente(msg);
					estado = ATENDER_CLIENTE;
					break;
					
				// Cozinha chamando
				case Const.CHAMAR_GARCOM_COZINHA:
					try {
						responderPedidoCliente(msg);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnreadableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					estado = ENTREGANDO_PRATO_CLIENTE;
					break;
				}
			else
				block();
			
			break;
			
		case ATENDER_CLIENTE:
			ACLMessage pedido = myAgent.receive();
			
			if(pedido != null)
			{
				System.out.println(myAgent.getLocalName() + ": Só um momento...");
				
				try {
					Pedido p = (Pedido) pedido.getContentObject();
					colocarPedidoNaCozinha(p);
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				
				estado = ESPERANDO;
			}
			else
				block();
			
			break;
			
		case ENTREGANDO_PRATO_CLIENTE:
			estado = ESPERANDO;
			break;
		}
	}
	
	private void responderChamadoCliente(ACLMessage msg)
	{
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		myAgent.send(reply);
		
		System.out.println(myAgent.getLocalName() + ": Olá " + msg.getSender().getLocalName() + ". O que vai querer?");
	}
	
	private void responderPedidoCliente(ACLMessage msg) throws IOException, UnreadableException
	{
		ACLMessage entrega = new ACLMessage(ACLMessage.PROPOSE);
		
		Pedido pedido = (Pedido) msg.getContentObject();
		
		AID cliente = pedido.getCliente();
		ItemCardapio i = pedido.getItem();
		
		entrega.addReceiver(cliente);
		entrega.setContentObject(pedido);
		myAgent.send(entrega);
		
		System.out.println();
		System.out.println(myAgent.getLocalName() + ": Aqui está seu " + i.getNome() + ", " + cliente.getLocalName() + "...");
	}
	
	private void colocarPedidoNaCozinha(Pedido p) {
		Cozinha.colocarPedido(p);
	}

	@Override
	public boolean done() {
		return false;
	}
}
