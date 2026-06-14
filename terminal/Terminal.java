package terminal;

import sistema.Navegador;
import sistema.Operador;
import java.util.Scanner;

public class Terminal {
    private final Navegador navegador;
    private final Operador operador;
    private final String userName;

    public Terminal(Navegador navegador, Operador operador) {
        this.navegador = navegador;
        this.operador = operador;
        this.userName = System.getProperty("user.name");
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        boolean rodando = true;

        System.out.print("\033[H\033[2J"); // Aqui ele vai dar um clear quando iniciar 
        System.out.flush();

        while (rodando) {
            String caminhoAbsoluto = navegador.getDiretorioAtual().getAbsolutePath();
            String raizAbsoluta = navegador.getRaizDoSistema().getAbsolutePath();

            // Aqui ele vai simular a raiz do sistema, mostrando apenas o caminho relativo a partir da raiz
            String caminhoSimulado = caminhoAbsoluto.replace(raizAbsoluta, "");
            if (caminhoSimulado.isEmpty()) {
                caminhoSimulado = "/"; 
            }

            // Aqui ele vai mostrar o usuário e o caminho atual coloridos simulando a raiz
            System.out.print("\033[1;32m" + userName + "@file-explorer\033[0m:\033[1;34m" + caminhoSimulado + "\033[0m$ ");
            
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) continue; // Caso aperte enter sem digitar nada, ele vai apenas mostrar o terminal novamente

            String[] partes = entrada.split(" ", 2);
            String comando = partes[0].toLowerCase();   // Aqui ele separar o que o usuário digitar em duas partes
            String argumento = partes.length > 1 ? partes[1] : "";

            switch (comando) { // Pega o comando e executa a ação correspondente
                case "exit":
                    rodando = false;
                    break;
                case "clear":
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;
                case "ls":
                    navegador.listarConteudo();
                    break;
                case "cd":
                    if (argumento.equals("..")) {
                        navegador.voltarDiretorio();
                    } else if (!argumento.isEmpty()) {
                        navegador.entrarNoDiretorio(argumento);
                    } else {
                        while (!navegador.getDiretorioAtual().equals(navegador.getRaizDoSistema())) {
                            navegador.voltarDiretorio();
                        }
                    }
                    break;
                case "touch":
                    if (argumento.isEmpty()) System.out.println("touch: falta operando");
                    else operador.criarArquivo(navegador.getDiretorioAtual(), argumento, navegador.getRaizDoSistema());
                    break;
                case "mkdir":
                    if (argumento.isEmpty()) System.out.println("mkdir: falta operando");
                    else operador.criarPasta(navegador.getDiretorioAtual(), argumento, navegador.getRaizDoSistema());
                    break;
                case "rm":
                    if (argumento.isEmpty()) System.out.println("rm: falta operando");
                    else operador.deletar(navegador.getDiretorioAtual(), argumento, navegador.getRaizDoSistema());
                    break;
                case "cat":
                    if (argumento.isEmpty()) System.out.println("cat: falta operando");
                    else operador.lerArquivo(navegador.getDiretorioAtual(), argumento, navegador.getRaizDoSistema());
                    break;
                case "nano":
                    if (argumento.isEmpty()) System.out.println("nano: falta operando");
                    else operador.editarArquivo(navegador.getDiretorioAtual(), argumento, scanner, navegador.getRaizDoSistema());
                    break;
                case "copy":
                    if (argumento.isEmpty()) {
                        System.out.println("copy: falta operando de arquivo");
                    } else {
                        String[] argsCopy = argumento.split(" ", 2);

                        if (argsCopy.length < 2) {
                            System.out.println("copy: falta operando de destino");
                        } else {
                            operador.copiarArquivo(navegador.getDiretorioAtual(), argsCopy[0], argsCopy[1], navegador.getRaizDoSistema());
                        }
                    }
                    break;
                case "write":
                    if(argumento.isEmpty()){
                        System.out.println("write: falta operando.");
                    } else{
                        String[] argsWrite = argumento.split(" ", 2);
                        if(argsWrite.length < 2){
                            System.out.println("write: uso correto: 'write <arquivo> <conteúdo>");
                        } else{
                            operador.escreverArquivo(navegador.getDiretorioAtual(), argsWrite[0], argsWrite[1], true, navegador.getRaizDoSistema());
                        }
                    }
                    break;
                case "append":
                    if(argumento.isEmpty()){
                        System.out.println("append: falta operando");
                    } else{
                        String[] argsAppend = argumento.split(" ", 2);
                        if (argsAppend.length < 2){ 
                            System.out.println("append: uso correto: 'append <arquivo> <conteúdo>'");
                        } else{
                            operador.escreverArquivo(navegador.getDiretorioAtual(), argsAppend[0], argsAppend[1], false, navegador.getRaizDoSistema());
                        }
                    }
                default:
                    System.out.println(comando + ": comando não encontrado");
            }
        }
        scanner.close();
    }
}