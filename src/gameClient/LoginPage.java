package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class represents a very simple GUI class to show
 * a simple log-in page before starting a game.
 *
 * this page allows the user to input their desired level
 * and ID.
 *
 */
public class LoginPage extends JFrame{

    private static JPanel panel;
    private static JLabel level_label;
    private static JTextField level;
    private static JLabel id_label;
    private static JTextField id;
    private static JButton play;

    private static boolean status;
    private static int stage;
    private static int id_;

    LoginPage(String a) {
        super(a);

        stage = -1;
        status = false;

        level_label = new JLabel();
        level_label.setText("Enter stage ->");
        level = new JTextField();

        id_label = new JLabel();
        id_label.setText("User ID (9 digits) ->");
        id = new JTextField();

        play = new JButton("Initialize");

        play.setEnabled(false);

        level.addKeyListener(new KeyAdapter() {


            @Override
            public void keyReleased(KeyEvent e) {

                if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    String text = level.getText();
                    String id_ = id.getText();

                    int l = !text.equals("") ? Integer.parseInt(text) : -1;
                    boolean condition = l >= 0 && id_.length() == 9;


                    level.setEditable(true);
                    play.setEnabled(condition);


                } else {

                    level.setEditable(false);

                }

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    String text = level.getText();
                    String id_ = id.getText();

                    int l = !text.equals("") ? Integer.parseInt(text) : -1;
                    boolean condition = l >= 0 && id_.length() == 9;

                    level.setEditable(true);
                    play.setEnabled(condition);

                } else {

                    level.setEditable(false);

                }

            }
        });

        id.addKeyListener(new KeyAdapter() {


            @Override
            public void keyReleased(KeyEvent e) {

                if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    String text = level.getText();
                    String id_ = id.getText();

                    int l = !text.equals("") ? Integer.parseInt(text) : -1;
                    boolean condition = l >= 0 && id_.length() == 9;

                    id.setEditable(true);
                    play.setEnabled(condition);


                } else {

                    id.setEditable(false);

                }

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    String text = level.getText();
                    String id_ = id.getText();

                    int l = !text.equals("") ? Integer.parseInt(text) : -1;
                    boolean condition = l >= 0 && id_.length() == 9;

                    id.setEditable(true);
                    play.setEnabled(condition);

                } else {

                    id.setEditable(false);

                }

            }
        });

        panel = new JPanel(new GridLayout(3, 1));

        panel.add(level_label);
        panel.add(level);

        panel.add(id_label);
        panel.add(id);

        panel.add(play);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        play.addActionListener(e -> {

            String text_level = level.getText();
            String text_id = id.getText();

//            System.out.println("you choose level: "  + text_level);
//            System.out.println("user id: " + text_id);

            setLevel(Integer.parseInt(text_level));
            setID(Integer.parseInt(text_id));
            setStatus();

            setVisible(false);

        });

        add(panel, BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);
    }

    public long getID() {return id_;}
    private void setID(int id) {id_ = id;}

    public int getLevel() {return stage;}
    private void setLevel(int l) {stage = l;}

    public boolean getStatus() { return status;}
    private void setStatus() {status = true;}

}