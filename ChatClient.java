import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

public class ChatClient extends JFrame implements ActionListener
{
	JButton send, addName;
	JTextField name;
	JTextArea chat, myMessage;
	JScrollPane scroll;

	Socket socket;
	String host = "172.30.102.239";
	int port = 6060;

	OutputStream toServer;
	DataOutputStream out;

	InputStream fromServer;
	DataInputStream in;

	String clientName ;

	ChatClient()
	{
		setSize(800, 500);

		name = new JTextField();
		name.setBounds(50, 20, 200, 30);
		add(name);

		addName = new JButton("Add");
		addName.setBounds(280, 20, 100, 30);
		addName.addActionListener(this);
		add(addName);

		chat = new JTextArea(80, 5);
		chat.setBounds(50, 70, 660, 280);
		chat.setEditable(false);

		scroll = new JScrollPane(chat);
		scroll.setBounds(50, 70, 660, 280);
		add(scroll);

		myMessage = new JTextArea(60, 2);
		myMessage.setBounds(50, 380, 550, 100);
		add(myMessage);

		send = new JButton("Send");
		send.setBounds(665, 465, 80, 30);
		send.addActionListener(this);
		add(send);

		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);


		try
		{
			socket = new Socket(host, port);
			toServer = socket.getOutputStream();
			out = new DataOutputStream(toServer);

			fromServer = socket.getInputStream();
			in = new DataInputStream(fromServer);
			int servPort = socket.getPort();
			int ownPort = socket.getLocalPort();

			System.out.println("Connected to host with port: " + servPort);
			System.out.println("My port is: " + ownPort);

			System.out.println("The server has replied:  " + in.readUTF() );
		}
		catch(Exception ex)
		{
			// e.printStackTrace();

		}

	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(addName) )
		{
			try
			{
				out.writeUTF(name.getText() );
				clientName = name.getText();
				name.setEnabled(false);
				addName.setEnabled(false);
			}
			catch(IOException ioe)
			{
				//Caught!
			}
		}
		if(e.getSource().equals(send) )
		{
			try
			{
				{
					System.out.println("Send");
					chat.setText(chat.getText() + "\n" + clientName + ": " + myMessage.getText() );
					out.writeUTF(myMessage.getText() );
					myMessage.setText("");
					// chat.setText("");
				}
			}
			catch(Exception ex)
			{
				//Caught !
			}
		}
		
	}

	void incoming()
	{
		System.out.println("in coming");
		try
		{
			try
			{
				fromServer = socket.getInputStream();
			}
			catch(NullPointerException npe)
			{
				System.out.println("Null pointer");
			}
			in = new DataInputStream(fromServer);
			String msg = "";
			while(true)
			{
				try
				{
					msg = in.readUTF();
					chat.setText(chat.getText() + "\n" + msg);
				}
				catch(NullPointerException npe)
				{
					// System.out.println("npe caught !");
				}
				finally
				{
					continue;
				}
			}
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
		}
	}

	public static void main(String args[])
	{
		ChatClient client = new ChatClient();
		client.incoming();
		
	}
}