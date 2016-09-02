import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.io.File;

public class gui extends JFrame
{
	public static void main(String[] args)
	{
		gui frame = new gui();
		frame.setVisible(true);
	}
	
	private JPanel contentPane;

	public gui()
	{
		setTitle("CompressionGUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 890, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("OK");
		fileChooser.setBounds(12, 13, 848, 329);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") +"/Desktop"));
		contentPane.add(fileChooser);
		
		JButton compress = new JButton("Compress");
		compress.setFont(new Font("Tahoma", Font.PLAIN, 42));
		compress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCompression.lLCompressionTXT(fileChooser.getSelectedFile());
				fileChooser.updateUI();
			}
		});
		compress.setBounds(12, 342, 419, 98);
		contentPane.add(compress);
		
		JButton decompress = new JButton("Decompress");
		decompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCompression.lLDCompressionTXT(fileChooser.getSelectedFile());
				fileChooser.updateUI();
			}
		});
		decompress.setFont(new Font("Tahoma", Font.PLAIN, 42));
		decompress.setBounds(430, 342, 430, 98);
		contentPane.add(decompress);
		
	
	}
}
