package sistema;

import java.io.File;
import java.io.FileWriter; // Usado para escrever texto dentro de um arquivo
import java.io.PrintWriter; // Facilita a escrita, permitindo usar o .println() igual no System.out
import java.io.IOException;
import java.util.Scanner;

public class Operador {

    // Simula o comando 'touch'
    public void criarArquivo(File diretorioAtual, String nomeDoArquivo) {
        File novoArquivo = new File(diretorioAtual, nomeDoArquivo);
        try {
            // O createNewFile retorna true se o arquivo não existia e foi criado
            // Se já existia, ele retorna false e não faz nada
            novoArquivo.createNewFile(); 
        } catch (IOException e) {
            System.out.println("touch: permissão negada ou erro ao criar '" + nomeDoArquivo + "'");
        }
    }

    // Simula o comando 'mkdir'
    public void criarPasta(File diretorioAtual, String nomeDaPasta) {
        File novaPasta = new File(diretorioAtual, nomeDaPasta);
        
        // O método mkdir() já tenta criar e retorna um booleano do resultado.
        if (!novaPasta.mkdir()) {
            System.out.println("mkdir: não foi possível criar o diretório '" + nomeDaPasta + "': Arquivo já existe ou caminho inválido");
        }
    }

    // Simula o comando 'rm'
    public void deletar(File diretorioAtual, String nome) {
        File alvo = new File(diretorioAtual, nome);
        
        if (!alvo.exists()) {
            System.out.println("rm: não foi possível remover '" + nome + "': Arquivo ou diretório inexistente");
            return;
        }
        
        // IMPORTANTE: No Java, o .delete() apaga arquivos tranquilamente, mas SÓ apaga pastas se elas estiverem VAZIAS.
        // Isso imita perfeitamente o comportamento seguro do 'rmdir' ou do 'rm' sem o '-r'.
        if (!alvo.delete()) {
            System.out.println("rm: não foi possível remover '" + nome + "': É um diretório com arquivos dentro ou você não tem permissão");
        }
    }

    // Simula o comando 'cat'
    public void lerArquivo(File diretorioAtual, String nomeDoArquivo) {
        File arquivo = new File(diretorioAtual, nomeDoArquivo);

        // Validações básicas antes de tentar ler
        if (!arquivo.exists()) {
            System.out.println("cat: " + nomeDoArquivo + ": Arquivo inexistente");
            return;
        }
        if (arquivo.isDirectory()) {
            System.out.println("cat: " + nomeDoArquivo + ": É um diretório"); // Não se pode dar 'cat' em uma pasta
            return;
        }

        // Usamos o "Try-with-resources" (esse try com parênteses).
        // Isso garante que o Scanner(arquivo) vai ser fechado automaticamente no final, mesmo se der erro, liberando a memória RAM.
        try (Scanner leitorDeArquivo = new Scanner(arquivo)) {
            while (leitorDeArquivo.hasNextLine()) {
                System.out.println(leitorDeArquivo.nextLine());
            }
        } catch (Exception e) {
            System.out.println("cat: erro ao tentar ler o arquivo");
        }
    }

    // Simula o editor 'nano'
    public void editarArquivo(File diretorioAtual, String nomeDoArquivo, Scanner leitorDoTerminal) {
        File arquivo = new File(diretorioAtual, nomeDoArquivo);

        if (arquivo.isDirectory()) {
            System.out.println("nano: " + nomeDoArquivo + ": É um diretório");
            return;
        }

        // Limpa a tela para dar a imersão de que abrimos um editor de texto
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(" === Arquivo: " + nomeDoArquivo + " | Digite ':wq' em uma nova linha para salvar e sair === ");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        
        // Se o arquivo já existir,imprime o que tem dentro dele para o usuário ver antes de continuar escrevendo
        if (arquivo.exists()) {
            try (Scanner leitorDeArquivo = new Scanner(arquivo)) {
                while (leitorDeArquivo.hasNextLine()) {
                    System.out.println(leitorDeArquivo.nextLine());
                }
            } catch (Exception ignored) {} // Ignora o erro silenciosamente se não der pra ler
        }

        // O parâmetro 'true' no FileWriter significa "Modo Append" (Adicionar ao final). 
        // Sem esse 'true', toda vez que abrisse o nano, ele apagaria tudo que já estava escrito antes.
        try (FileWriter fw = new FileWriter(arquivo, true);
             PrintWriter escritor = new PrintWriter(fw)) {
            
            // Loop infinito de digitação até o usuário digitar a palavra de saída
            while (true) {
                String linha = leitorDoTerminal.nextLine(); // Pega o Scanner que veio lá do Terminal.java
                
                if (linha.trim().equals(":wq")) {
                    break;
                }
                escritor.println(linha); // Escreve a linha fisicamente no HD
            }
        } catch (IOException e) {
            System.out.println("nano: Erro fatal ao tentar gravar no arquivo");
        }
        
        // Quando sair do loop do nano, limpa a tela de novo para devolver o usuário ao terminal limpo
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}