import sistema.Navegador;
import sistema.OperadorDeDisco;
import terminal.Terminal;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Cria a pasta no diretório do projeto
        File pastaRoot = new File(System.getProperty("user.dir"), "root");
        
        if (!pastaRoot.exists()) {
            pastaRoot.mkdir();
        }

        Navegador navegador = new Navegador(pastaRoot.getAbsolutePath());
        OperadorDeDisco operador = new OperadorDeDisco();

        Terminal terminal = new Terminal(navegador, operador);

        terminal.iniciar();
    }
}