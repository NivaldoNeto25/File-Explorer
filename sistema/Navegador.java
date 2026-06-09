package sistema;

import java.io.File; // aqui utiliza file pq File nao é o arquivo de fato, ele é o endereço e ai tem métodos para falar coisas sobre o caminho ("existe?","é pasta? ou arquivo?","qual o caminho completo?", etc)
// poderia usar string mas ai ia ser bem mais trabalhoso fazer na mão, o file deixa mais simples de se fazer

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
}
