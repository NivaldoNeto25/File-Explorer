import sistema.Navegador;
import sistema.OperadorDeDisco;
import terminal.Terminal;

public class Main {
    public static void main(String[] args) {

        String caminhoInicial = System.getProperty("user.home");

        Navegador navegador = new Navegador(caminhoInicial);
        OperadorDeDisco operador = new OperadorDeDisco();

        Terminal terminal = new Terminal(navegador, operador);

        terminal.iniciar();
    }
}