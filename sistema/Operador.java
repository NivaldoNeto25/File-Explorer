package sistema;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;// escolhemos o buffered para ser mais eficiente

public class Operador {

    private File validarCaminho(File diretorioAtual, String nome, File raizDoSistema, String comando){
        File alvo = new File(diretorioAtual, nome);
        try{
            String caminhoAlvo = alvo.getCanonicalPath();
            String caminhoRaiz = raizDoSistema.getCanonicalPath();
            if(!caminhoAlvo.startsWith(caminhoRaiz)){ //se o diretório estiver fora do "root" do sistema n vai liberar
                System.out.println(comando + ": acesso negado.");
                return null;
            }
        } catch(Exception e){
            System.out.println(comando + ": erro ao resolver caminho.");
            return null;
        }
        return alvo;
    }

    // Simula o comando 'touch'
    public void criarArquivo(File diretorioAtual, String nomeDoArquivo, File raizDoSistema) {
        File novoArquivo = validarCaminho(diretorioAtual, nomeDoArquivo, raizDoSistema, "touch");
        if (novoArquivo == null){
            return;
        }

        try {
            // O createNewFile retorna true se o arquivo não existia e foi criado
            // Se já existia, ele retorna false e não faz nada
            novoArquivo.createNewFile(); 
        } catch (IOException e) {
            System.out.println("touch: permissão negada ou erro ao criar '" + nomeDoArquivo + "'");
        }
    }

    // Simula o comando 'mkdir'
    public void criarPasta(File diretorioAtual, String nomeDaPasta, File raizDoSistema) {
        File novaPasta = validarCaminho(diretorioAtual, nomeDaPasta, raizDoSistema, "mkdir");
        if (novaPasta == null){
            return;
        }
        
        // O método mkdir() já tenta criar e retorna um booleano do resultado.
        if (!novaPasta.mkdir()) {
            System.out.println("mkdir: não foi possível criar o diretório '" + nomeDaPasta + "': Arquivo já existe ou caminho inválido");
        }
    }

    // Simula o comando 'rm'
    public void deletar(File diretorioAtual, String nome, File raizDoSistema) {
        File alvo = validarCaminho(diretorioAtual, nome, raizDoSistema, "rm");
        if (alvo == null){
            return;
        }
        
        if (!alvo.exists()) {
            System.out.println("rm: não foi possível remover '" + nome + "': Arquivo ou diretório inexistente");
            return;
        }
        if (!deletarRecursivo(alvo)) {
            System.out.println("rm: não foi possível remover '" + nome + "': erro ao deletar.");
        }
    }

    private boolean deletarRecursivo(File alvo){ //aq ele vai conseguir "limpar" o diretorio c arquivos para poder apagar ele
        if(alvo.isDirectory()){
            File[] filhos = alvo.listFiles();
            if(filhos != null){
                for (File filho : filhos){
                    if(!deletarRecursivo(filho)){
                        return false;
                    }
                }
            }
        }
        return alvo.delete();
    }

    // Simula o comando 'cat'
    public void lerArquivo(File diretorioAtual, String nomeDoArquivo, File raizDoSistema) {
        File arquivo = validarCaminho(diretorioAtual, nomeDoArquivo, raizDoSistema, "cat");
        if(arquivo == null){
            return;
        }

        // Validações antes de tentar ler
        if (!arquivo.exists()) {
            System.out.println("cat: " + nomeDoArquivo + ": Arquivo inexistente");
            return;
        }
        if (arquivo.isDirectory()) {
            System.out.println("cat: " + nomeDoArquivo + ": É um diretório"); // Não se pode dar 'cat' em uma pasta
            return;
        }
        try (BufferedInputStream leitor = new BufferedInputStream(new FileInputStream(arquivo))){
            byte[] buffer= new byte[1024];
            int bytesLidos;
            
            while((bytesLidos =leitor.read(buffer)) != -1){
                System.out.print(new String(buffer, 0, bytesLidos));
            }
            System.out.println();
            } catch (Exception e) {
            System.out.println("cat: erro ao tentar ler o arquivo");
        }
    }

    // Simula o editor 'nano'
    public void editarArquivo(File diretorioAtual, String nomeDoArquivo, Scanner leitorDoTerminal, File raizDoSistema) {
        File arquivo = validarCaminho(diretorioAtual, nomeDoArquivo, raizDoSistema, "nano");
        if(arquivo == null){
            return;
        }

        if (arquivo.isDirectory()) {
            System.out.println("nano: " + nomeDoArquivo + ": É um diretório");
            return;
        }

        // Limpa a tela
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

        // false = sobrescreve o arquivo inteiro c o que for escrito
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(arquivo, false));
             PrintStream escritor = new PrintStream(bos, true)){ // vai ativar o autoFlush p o buffer ir esvaziando automaticamente sem precisar do flush()
            
           while (true){
            String linha = leitorDoTerminal.nextLine();
            if(linha.trim().equals(":wq")){
                break;
            }
            escritor.println(linha);
           }

        } catch (IOException e) {
            System.out.println("nano: Erro fatal ao tentar gravar no arquivo");
        }
        
        // Quando sair do loop do nano, limpa a tela de novo para devolver o usuário ao terminal limpo
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void copiarArquivo(File diretorioAtual, String nomeOrigem, String nomeDestino, File raizDoSistema) {
        File arquivoOrigem = validarCaminho(diretorioAtual, nomeOrigem, raizDoSistema, "copy");
        if(arquivoOrigem == null){
            return;
        }

        File arquivoDestino = validarCaminho(diretorioAtual, nomeDestino, raizDoSistema, "copy");
        if(arquivoDestino == null){
            return;
        }

        // Verifica o estado no sistema de arquivos. Se não existir ou for diretório, aborta a operação.
        if (!arquivoOrigem.exists() || arquivoOrigem.isDirectory()) {
            System.out.println("copy: não foi possível copiar '" + nomeOrigem + "': Arquivo inexistente ou é diretório");
            return;
        }

        // Abre os fluxos de I/O (Input/Output). O uso do try com parênteses garante que o método .close()
        // seja chamado automaticamente no final, liberando o lock do arquivo no Sistema Operacional.
        try (BufferedInputStream leitor = new BufferedInputStream(new FileInputStream(arquivoOrigem)); BufferedOutputStream escritor = new BufferedOutputStream(new FileOutputStream(arquivoDestino))) {
            
            // Cria um array capaz de armazenar 1024 bytes (1 KB) por ciclo.
            byte[] buffer = new byte[1024]; 
            int bytesLidos;

            // O leitor.read() acessa o disco, puxa até 1024 bytes e joga no array (buffer).
            // Ele retorna o número inteiro de bytes que realmente leu. Quando atinge o EOF (End Of File), retorna -1.
            while ((bytesLidos = leitor.read(buffer)) != -1) {
                // O escritor.write() pega o array na RAM e grava no disco de destino.
                // Começa do índice 0 e vai até a quantidade exata de bytesLidos para não gravar lixo de memória na última volta.
                escritor.write(buffer, 0, bytesLidos);
            }
            escritor.flush();
            
        } catch (IOException e) {
            System.out.println("copy: erro de I/O ao tentar copiar o arquivo");
        }
    }
}