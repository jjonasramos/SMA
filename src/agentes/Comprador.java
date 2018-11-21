package agentes;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Comprador extends Agent {
	
	private String tituloLivroComprar;
	
	private AID[] agentesVendedores;
	
	// inicializacao do agente
	protected void setup() {
		
		System.out.println("Olá! Agente Comprador " + getAID().getName() + ", qual livro vc procura?");
		
		Object[] args = getArguments();
		
		// pega o titulo do livro para fazer uma busca
		if(args != null && args.length > 0) {
			tituloLivroComprar = (String) args[0];
			System.out.println("Estou a procura do livro: " + tituloLivroComprar + "!!");
			
			// programa o comprador para verificar um vendedor a cada minuto
			addBehaviour(new TickerBehaviour(this, 60000) {
				
				@Override
				protected void onTick() {
					System.out.println("Ola vendedor, quero comprar " + tituloLivroComprar);
					
					// atualiza a lista de vendedores
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					
					sd.setType("venda-livros");
					template.addServices(sd);
					
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template);
						System.out.println("Os seguintes agentes são vendedores:");
						agentesVendedores = new AID[result.length];
						
						for(int i=0; i < result.length; i++) {
							agentesVendedores[i] = result[i].getName();
							System.out.println(agentesVendedores[i].getName());
						}
					} catch (FIPAException fe) {
						fe.printStackTrace();
					}
					
					// executa o pedido
					myAgent.addBehaviour(new PedidoCompra());
				}
			});
		} else {
			System.out.println("O livro não está disponível.");
			doDelete();
		}
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		System.out.println("Agente comprador " + getAID().getName() + " obrigado, volte sempre!!");
	}
	
	private class PedidoCompra extends Behaviour {
		
		private AID bestSeller;
		private int bestPrice;
		private int repliesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;
		
		public PedidoCompra() {}

		@Override
		public void action() {
			
			switch(step) {
				case 0:
					// envia sua identificacao para todos os vendedores
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					
					for(int i = 0; i < agentesVendedores.length; i++) {
						cfp.addReceiver(agentesVendedores[i]);
					}
					
					cfp.setContent(tituloLivroComprar);
					cfp.setConversationId("comercio-livros");
					cfp.setReplyWith("cfp" + System.currentTimeMillis());
					myAgent.send(cfp);
					
					// prepara o modelo de receber respostas
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("comercio-livros"),
							MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
					
					step = 1;
					break;
					
				case 1:
					// recebe todas as respostas / recusado por agentes vendedores
					ACLMessage reply = myAgent.receive(mt);
					
					if(reply != null) {
						// responde
						if(reply.getPerformative() == ACLMessage.PROPOSE) {
							// oferta
							int price = Integer.parseInt(reply.getContent());
							
							if(bestSeller == null || price < bestPrice) {
								// este é o melhor vendedor do momento
								bestPrice = price;
								bestSeller = reply.getSender();
							}
						}
						
						repliesCnt++;
						
						if(repliesCnt >= agentesVendedores.length) {
							// quando receber todas as ofertas
							step = 2;
						}
					} else {
						block();
					}
					break;
					
				case 2:
					// envie o pedido de compra para o melhor vendedor
					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestSeller);
					order.setContent(tituloLivroComprar);
					order.setConversationId("comercio-livros");
					order.setReplyWith("Order " + System.currentTimeMillis());
					myAgent.send(order);
					
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("comercio-livros"),
							MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					
					step = 3;
					break;
					
				case 3:
					// recebe a resposta de ordem de compra
					reply = myAgent.receive(mt);
					
					if(reply != null) {
						// resposta da ordem de compra recebida
						if(reply.getPerformative() == ACLMessage.INFORM) {
							// Compra bem sucedida
							System.out.println("Livro " + tituloLivroComprar + "comprado de " + 
											reply.getSender().getName());
							System.out.println("Preço: R$ " + bestPrice);
							myAgent.doDelete();
						} else {
							System.out.println("Desculpe mas o livro ja foi vendido");
						}
						
						step = 4;
					} else {
						block();
					}
					break;
			}
			
		}

		@Override
		public boolean done() {
			
			if(step == 2 && bestSeller == null) {
				System.out.println("Desculpe mas o livro " + tituloLivroComprar + " não está a venda!	");
			}
			
			 return ((step == 2 && bestSeller == null) ||  step == 4);
		}
	}
}
