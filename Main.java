import sistema.Navegador;
import sistema.Operador;
import terminal.Terminal;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Cria a pasta root no diretório do projeto
        File pastaRoot = new File(System.getProperty("user.dir"), "root");
        
        if (!pastaRoot.exists()) {
            pastaRoot.mkdir();
        }

        Navegador navegador = new Navegador(pastaRoot.getAbsolutePath());
        Operador operador = new Operador();

        Terminal terminal = new Terminal(navegador, operador);

        terminal.iniciar();
    }
}