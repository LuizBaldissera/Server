import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.FlowLayout;
import javax.swing.*;

public class Cliente {
    public static void main(String[] args) {
            ClienteGUI gui = new ClienteGUI();
            gui.setVisible(true);
    }
}

class ClienteGUI extends JFrame {
    private JTextField txtQtdNumeros;
    private JTextField txtRecebido;
    private JButton btnEnviar;

    public ClienteGUI() {
        setTitle("Cliente");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        txtQtdNumeros = new JTextField(10);
        txtRecebido = new JTextField(30);
        txtRecebido.setEditable(false);
        btnEnviar = new JButton("Enviar");

        add(new JLabel("Quantidade de números a gerar:"));
        add(txtQtdNumeros);
        add(new JLabel("Quantidade de números ordenados:"));
        add(txtRecebido);
        add(btnEnviar);

        btnEnviar.addActionListener(e -> enviarLista());
    }

    private void enviarLista() {
        new Thread(() -> {
            try {
                int qtdNumeros = Integer.parseInt(txtQtdNumeros.getText());

                Socket socket = new Socket("localhost", 12345);

                List<Integer> lista = gerarListaAleatoria(qtdNumeros);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(lista);
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                int qtdOrdenados = in.readInt();

                txtRecebido.setText("Quantidade: " + qtdOrdenados);


                socket.close();
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }).start();
    }


    private List<Integer> gerarListaAleatoria(int qtd) {
        Random random = new Random();
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < qtd; i++) {
            lista.add(random.nextInt(100));
        }
        return lista;
    }
}