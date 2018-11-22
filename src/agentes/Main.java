package agentes;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;

public class Main 
{
	public static void main(String[] args) 
	{
		Runtime runtime = Runtime.instance();
		
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.MAIN_HOST, "localhost");
		profile.setParameter(Profile.GUI, "true");
		
		ContainerController containerController = runtime.createMainContainer(profile);
		
		criarAgentes();
	}
	
	private static void criarAgentes()
	{
		// TODO: Criar agentes
	}
}
