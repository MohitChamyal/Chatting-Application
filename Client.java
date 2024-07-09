package GroupChat.src;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;

public class Client implements ActionListener{

    JTextField text;
    static JPanel area;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    static JFrame frame = new JFrame();

    Client()
    {
        frame.setLayout(null); //used to make user to set element by itself

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        frame.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(8, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        JLabel name = new JLabel("Gaurav Chamyal");
        name.setBounds(110, 15, 150, 25);
        name.setForeground(Color.white);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 39, 100, 18);
        status.setForeground(Color.white);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
        p1.add(status);

        area = new JPanel();
        area.setBounds(5, 75, 440, 570);
        frame.add(area);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        frame.add(text);

        JButton send = new JButton("SEND");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.addActionListener(this);
        frame.add(send);

        frame.setSize(450, 700);
        frame.setUndecorated(true);
        frame.setLocation(700, 50);
        frame.getContentPane().setBackground(Color.WHITE);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try
        {
            String str = text.getText();
            JPanel chat = formatPanel(str);

            area.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(chat, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            area.add(vertical, BorderLayout.PAGE_START);
            dout.writeUTF(str);

            frame.repaint();
            frame.validate();
            frame.invalidate();

            text.setText("");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static JPanel formatPanel(String str)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel(str);
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15, 15, 50));

        panel.add(output);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(calendar.getTime()));
        panel.add(time);

        return panel;
    }

    public static void main(String[] args) {
        new Client();

        try
        {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true)
            {
                area.setLayout(new BorderLayout());
                String msg = dis.readUTF();
                JPanel panel = formatPanel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);

                vertical.add(Box.createVerticalStrut(15));
                area.add(vertical, BorderLayout.PAGE_START);

                frame.validate();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

}
