package app;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class View extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtSurname;
	private JTextArea txtResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 108, 14);
		contentPane.add(lblName);
		
		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setBounds(168, 11, 108, 14);
		contentPane.add(lblSurname);
		
		txtName = new JTextField();
		txtName.setBounds(10, 27, 135, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtSurname = new JTextField();
		txtSurname.setColumns(10);
		txtSurname.setBounds(168, 27, 135, 20);
		contentPane.add(txtSurname);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String name = txtName.getText().toString();
				String surname = txtSurname.getText().toString();
				
				String result = HL7Provider.searchPatient(name,surname);
				
				txtResult.setText(result);
				
			}
		});
		btnSearch.setBounds(335, 26, 89, 23);
		contentPane.add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 67, 414, 352);
		contentPane.add(scrollPane);
		
		txtResult = new JTextArea();
		scrollPane.setViewportView(txtResult);
	}
}
