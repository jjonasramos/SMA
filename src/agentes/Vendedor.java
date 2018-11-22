package agentes;
import java.util.Hashtable;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Vendedor extends Agent 
{
	// guardar os livros a serem vendidos
	private Hashtable catalogo;
	// permite adicionar livros ao catalogo
	private GuiVendaLivros myGui;
	
	protected void setup() {
		
		// cria o catalogo
		System.out.println("Bem vindo agente vendedor " + getAID().getName() + ".");
		catalogo = new Hashtable();
		
		myGui = new GuiVendaLivros(this);
		myGui.showGui();
		
		// registra o servico do vendedor de livros nas paginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("venda-livros");
		sd.setName("Carteira JADE");
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
		// adiciona um comportamento para servir as consultas dos compradores
		addBehaviour(new DemandaOfertaServidor());	
		
		// adiciona um comportamento servindo ordens de compra dos compradores
		addBehaviour(new OrdemCompraServidor());
	}
	
	@Override
	protected void takeDown() {
		
		try {
			DFService.deregister(this);
		} catch(FIPAException fe) {
			fe.printStackTrace();
		}
		
		myGui.dispose();
		// demite o agente
		System.out.println("Agente vendedor " + getAID().getName() + "demitido.");
	}
	
	public void atualizaCatalogo(final String titulo, final int preco) {
		
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				catalogo.put(titulo, new Integer(preco));
				System.out.println("Livro: " + titulo + " está diponível pelo preço de: R$ " + preco);
			}
		});
		
	}
	
	/* Esta é a classe responsavel pelo comportamento dos agentes vendedores de livros
	 * Se o livro solicitado está no catalogo local, o agente  vendedor responder
	 * com uma mensagem PROPOSE especificando o preço,
	 * Caso contrário, uma mensagem de REFUSE  é enviado  de volta.
	 * */
	
	private class DemandaOfertaServidor extends CyclicBehaviour {
		
		
		public DemandaOfertaServidor() {}

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null) {
				// processar a mensagem recebida
				String titulo = msg.getContent();
				ACLMessage resposta = msg.createReply();
				
				Integer preco = (Integer) catalogo.get(titulo);
				
				if(preco != null) {
					// se o livro estiver disponivel, responde com preco
					resposta.setPerformative(ACLMessage.PROPOSE);
					resposta.setContent(String.valueOf(preco.intValue()));
				} else {
					resposta.setPerformative(ACLMessage.REFUSE);
					resposta.setContent("Não disponível.");
				}
				
				myAgent.send(resposta);
			} else {
				block();
			}
		}

	
	}
	
	private class OrdemCompraServidor extends CyclicBehaviour {

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			
			if(msg != null) {
				String titulo = msg.getContent();
				ACLMessage resposta = msg.createReply();
				
				Integer preco = (Integer) catalogo.remove(titulo);
				
				if(preco != null) {
					resposta.setPerformative(ACLMessage.PROPOSE);
					resposta.setContent(String.valueOf(preco.intValue()));
				} else {
					resposta.setPerformative(ACLMessage.REFUSE);
					resposta.setContent("Não disponível.");
				}
				
				myAgent.send(resposta);
			} else {
				block();
			}
		} 
	}
}
