import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class MIPS {

	private JFrame frmMipsMachineEmulator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MIPS window = new MIPS();
					window.frmMipsMachineEmulator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MIPS() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMipsMachineEmulator = new JFrame();
		frmMipsMachineEmulator.setTitle("MIPS Emulator");
		frmMipsMachineEmulator.setBounds(100, 100, 388, 387);
		frmMipsMachineEmulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmMipsMachineEmulator.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblSelectOperation = new JLabel("Select Operation");
		lblSelectOperation.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblSelectOperation = new GridBagConstraints();
		gbc_lblSelectOperation.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectOperation.gridx = 4;
		gbc_lblSelectOperation.gridy = 1;
		frmMipsMachineEmulator.getContentPane().add(lblSelectOperation, gbc_lblSelectOperation);
		
		JButton btnNewButton = new JButton("add");
		btnNewButton.setPreferredSize(new Dimension(90,30));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 3;
		frmMipsMachineEmulator.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("sub");
		btnNewButton_1.setPreferredSize(new Dimension(90,30));
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 4;
		gbc_btnNewButton_1.gridy = 4;
		frmMipsMachineEmulator.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("lw");
		btnNewButton_2.setPreferredSize(new Dimension(90,30));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 4;
		gbc_btnNewButton_2.gridy = 5;
		frmMipsMachineEmulator.getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("sw");
		btnNewButton_3.setPreferredSize(new Dimension(90,30));
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 4;
		gbc_btnNewButton_3.gridy = 6;
		frmMipsMachineEmulator.getContentPane().add(btnNewButton_3, gbc_btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("beq");
		btnNewButton_4.setPreferredSize(new Dimension(90,30));
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 4;
		gbc_btnNewButton_4.gridy = 7;
		frmMipsMachineEmulator.getContentPane().add(btnNewButton_4, gbc_btnNewButton_4);
	}
	
	
	

}
