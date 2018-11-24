import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import restaurante.agentes.caixa.Caixa;
import restaurante.agentes.cliente.Cliente;
import restaurante.agentes.cozinheiro.Cozinheiro;
import restaurante.agentes.garcom.Garcom;

public class Main 
{
	private static ContainerController containerController;
	
	public static void main(String[] args) 
	{
		Runtime runtime = Runtime.instance();
		
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.MAIN_HOST, "localhost");
		profile.setParameter(Profile.GUI, "true");
		profile.setParameter(Profile.AGENTS, "true");
		
		containerController = runtime.createMainContainer(profile);
		
		try
		{
			criarAgentes();
		}
		catch(StaleProxyException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void criarAgentes() throws StaleProxyException
	{
		AgentController leticia    = criarEIniciarAgente("Fulano"  , Cliente.class.getName());
		AgentController garcom     = criarEIniciarAgente("Ciclano" , Garcom.class.getName());
		AgentController cozinheiro = criarEIniciarAgente("Beltrano", Cozinheiro.class.getName());
		AgentController caixa      = criarEIniciarAgente("Mulano"  , Caixa.class.getName());
	}
	
	private static AgentController criarEIniciarAgente(String name, String pkg) throws StaleProxyException
	{
		AgentController ac = containerController.createNewAgent(name, pkg, null);
		ac.start();

		return ac;
	}
}
