package restaurante.agentes.cliente;

import java.io.IOException;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import restaurante.agentes.caixa.FilaCaixa;

public class PagarNoCaixa extends Behaviour 
{
	private Cliente cliente;
	
	private static final int ESPERANDO_NA_FILA = 0;
	private static final int ESPERANDO_CAIXA_RECEBER = 1;
	private static final int PAGOU = 2;
	
	private int estado = ESPERANDO_NA_FILA;

    //TODO Ir pra fila do caixa
	public PagarNoCaixa(Cliente cliente) 
	{
		super(cliente);
		
		this.cliente = cliente;
		
		System.out.println(cliente.getLocalName() + ": Indo para fila do caixa");
		FilaCaixa.entrar(cliente);
	}

	@Override
	public void action() 
	{
		switch(estado)
		{
		case ESPERANDO_NA_FILA:
			ACLMessage chamada = myAgent.receive();
			
			if(chamada != null)
			{
				ACLMessage reply = chamada.createReply();
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				
				try {
					reply.setContentObject(cliente.getValorAPagar());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				myAgent.send(reply);
				
				System.out.println(myAgent.getLocalName() + ": Sou eu! Aqui está o dinheiro...");
				
				estado = ESPERANDO_CAIXA_RECEBER;
			}
			else
				block();
			break;
			
		case ESPERANDO_CAIXA_RECEBER:
			ACLMessage confirmacao = myAgent.receive();
			
			if(confirmacao != null)
			{
				System.out.println(myAgent.getLocalName() + ": tudo certo!");
				
				estado = PAGOU;
			}
			else
				block();
		}
	}

	@Override
	public boolean done() {
		return estado == PAGOU;
	}

}
