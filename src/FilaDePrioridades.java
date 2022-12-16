import java.util.ArrayList;
import java.util.List;

public class FilaDePrioridades implements Cloneable{ //implements Cloneable
    private List<No> lista;

    //construtor da Fila
    public FilaDePrioridades (FilaDePrioridades modelo) throws Exception  
    {
        if (modelo == null)
            throw new Exception("modelo ausente");

        this.lista = copia(modelo.lista);
    }
    
    //enfileira
    public void enfileirar(No item){
         //varre a lista
        for (int i = 0; i <lista.size(); i++){
            //checa a se frequencia do item que estamos tentando inserir é menor que o próximo item da lista
            if(lista.get(i).getFrequencia() > item.getFrequencia()) { 
                //caso seja, faz a inserção
                lista.add(i, item);
                return;
            }
        } // se chegou aqui ele é o maior ou a lista está vazia, então é só adicionar no final
        lista.add(item);
    }

    //serve para enfilar na ordem, transformando a fila do arquivo em uma que possamos utilizar
    public void enfileirarNaOrdem(No item){
        lista.add(item);
    }

    //verifica se a lista está vazia, caso não esteja remove o elemento
    public No removerElemento() throws Exception {
        if(lista.isEmpty()) //verifica se a lista está vazia
            throw new Exception("Erro - remover elemento. Lista vazia");

        return lista.remove(0); //remove o ultimo elemento
    }

    //restaga o tamanho da lista
    public int tamanho(){ 
        return lista.size();
    }

    public FilaDePrioridades(){   //método publico que será usado para a classe zipador
        lista = new ArrayList<No>(); //é instanciado um novo arraylist do tipo "No" e adiciona a lista 
    }

    public ArrayList<String> showFila(){
        ArrayList<String> result = new ArrayList<>();
        for(No no : lista){ //declaramos o no e retornamos ele e sua frequência
            result.add(no.getInfo() + "");
            result.add(no.getFrequencia() + "");
        }
        return result;
    }
    
    private List copia (List<No> lista)    //construtor de cópia
    {
        if (lista==null) return null;

        List<No> result =  new ArrayList ();  //cria uma lista de No com base no List

        for (No item: lista) {
            result.add( new No(item.getEsq(), item.getInfo(), item.getFrequencia(), item.getDir())); //adiciona o No com a sua informação, o que está a esuqerda a sua direita e a sua frequencia
        }

        return result;
    }

    public Object clone () //método clone normal
    {
        FilaDePrioridades ret = null;

        try
        {
            ret = new FilaDePrioridades (this);
        }
        catch (Exception erro)
        {}

        return ret;
    }
}
