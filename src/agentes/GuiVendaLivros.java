package agentes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jade.core.Agent;

public class GuiVendaLivros extends JFrame {

	private Vendedor meuAgente;
	
	private JTextField campoTitulo, campoPreco;
	
	public GuiVendaLivros(Vendedor v) {
		super(v.getLocalName());
		
		meuAgente = v;
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Título do livro:"));
		campoTitulo = new JTextField(15);
		p.add(campoTitulo);
		
		p.add(new JLabel("Preço:"));
		campoPreco = new JTextField(15);
		p.add(campoPreco);
		getContentPane().add(p, BorderLayout.CENTER);
		
		JButton addButton = new JButton("Adicionar");
		
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					String title = campoTitulo.getText().trim();
					String price = campoPreco.getText().trim();
					meuAgente.atualizaCatalogo(title, Integer.parseInt(price));
					campoTitulo.setText("");
					campoPreco.setText("");
				} catch (Exception exception) {
					
				}
			}
		});
		
		p = new JPanel();
		p.add(addButton);
		
		getContentPane().add(p, BorderLayout.SOUTH);
		
		// quando fechar a janela mata o agente
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				meuAgente.doDelete();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setResizable(false);
	}
	
	public void showGui() {
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
	
	
}
