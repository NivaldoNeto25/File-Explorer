package sistema;

import java.io.File; // aqui utiliza file pq File nao é o arquivo de fato, ele é o endereço e ai tem métodos para falar coisas sobre o caminho ("existe?","é pasta? ou arquivo?","qual o caminho completo?", etc)
// poderia usar string mas ai ia ser bem mais trabalhoso fazer na mão, o file deixa mais simples de se fazer
import java.util.Arrays;//para listar os arquivos (que traz File[])e ordernar sem ser na mão e em um loop o Arrays ja tem metódo para isso

public class Navegador {
    private final File raizDoSistema; //final pq não vai mudar depois de ser definida
    private File diretorioAtual; //muda nos cd

    public Navegador(String caminhoRaiz){ //vai receber o root/ do Main
        this.raizDoSistema = new File(caminhoRaiz);
        this.diretorioAtual= this.raizDoSistema;//vai começar na raiz
    }

    public File getRaizDoSistema(){
        return raizDoSistema;
    }

    public File getDiretorioAtual(){
        return diretorioAtual;
    
    }
    //passar diretorio e etc para o operador e tals

    public void listarConteudo(){ //funciona como o ls
        File[] itens = diretorioAtual.listFiles();

        if(itens == null || itens.length == 0){
            System.out.println("diretório vazio");
            return;
        }

        Arrays.sort(itens, (a, b) -> { //o sort vai pegar dois elementos do array e vai comparar eles para ver quem vem primeiro. essa seta basicamento significa "tendo a e b, faça isso" que ficaria mais extenso se não usasse esse lambda do java
            if (a.isDirectory() && !b.isDirectory()){
                return -1;
            }
            if(!a.isDirectory() && b.isDirectory()){
                return 1;
            }
            return a.getName().compareToIgnoreCase(b.getName()); //ignorando o maiusculo e minusculo para nao mudar a ordem de prioridade por conta de maiusculas (tabela ASCII)
        });

        for(File item : itens){ //vai percorrer o array ordenado e imprimir os itens
            if(item.isDirectory()){
                System.out.println(item.getName() + "/"); //se for pasta tem a barra
            } else{
                System.out.println(item.getName());
            }
        }
    }

    public void voltarDiretorio(){
        try{
            String caminhoAtual = diretorioAtual.getCanonicalPath(); //vai pegar o caminho verdadeiro deles
            String caminhoRaiz = raizDoSistema.getCanonicalPath();

            if (caminhoAtual.equals(caminhoRaiz)){
                System.out.println("cd: já está na raiz do sistema"); //para não escapar do projeto
                return;
            }
        } catch(Exception e){
            System.out.println("cd: erro ao verificar caminho"); //"tratamento" de erro nos caminhos
            return;
        }
        diretorioAtual = diretorioAtual.getParentFile(); //caso de tudo certo, ele vai pro "pai/mae" dele, ou seja, o diretorio anterior
    }
}
