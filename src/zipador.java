import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class zipador
{
public class compactador
{
    //private List<Byte> arquivoDeBytes;
    byte[] arquivoDeBytes;
    FilaDePrioridades filaDePrioridades = new FilaDePrioridades();
    Arvore arvore;

    //chama os metodos e juntos compactam o arquivo
    public void compactar(String arquivo) throws Exception {
        lerArquivo(arquivo);
        criarListaDePrioridade();
        arvore = new Arvore((FilaDePrioridades)filaDePrioridades.clone());
        gerarArquivoCompactado(arquivo);
    }

    private void lerArquivo(String arquivo) throws  Exception{
        // ler o doc pra entender o que cada método faz
       // https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html
       File newFIle = new File(arquivo);
       RandomAccessFile acessoAoArquivo = new RandomAccessFile(newFIle, "rw");
       this.arquivoDeBytes = new byte[(int)acessoAoArquivo.length()];
       acessoAoArquivo.read(this.arquivoDeBytes); //Lê um byte de dados deste arquivo.
       acessoAoArquivo.close(); 
   }

       //cria o map com bytes da arvore e adiciona na string
       private String addBytesArvore(Arvore a){
        StringBuilder mapDeBytes = new StringBuilder();
        Map<Byte, String>  map = a.toHashMap();
        //para cada Byte do MAP, pega o valor e adiciona na string -> pega o caminho da arvore e cria umm map de bytes
        for (byte arquivoDeBytes : this.arquivoDeBytes) {
            mapDeBytes.append(map.get(arquivoDeBytes));
        }
        return mapDeBytes.toString();
    }
   private void criarListaDePrioridade() { //Aqui há os bytes e sua frequencia
    Map<Byte, Integer> frequenciaDeBytes = new HashMap<>();

//         (AALINE)
//          1° A → cria uma chav
//          2° A → procura se essa chave já existe. 
// 	        Se existir faz chave anterior(1) + 1
//          –
//          se não existir
//          add valor 1 pra nova chave

   //verifica se o byte possui iteração(chave que o haspMap cria)
    for (byte arquivoDeBytes : this.arquivoDeBytes) {
        if(!frequenciaDeBytes.containsKey(arquivoDeBytes)) { 
            // soma o valor da chave: (<byte>=n+1)
            frequenciaDeBytes.put(arquivoDeBytes, 1);

        } else {
            // se NÃo existir, adcionar o byte com o valor 1: (<byte>=1)
            frequenciaDeBytes.put(arquivoDeBytes,frequenciaDeBytes.get(arquivoDeBytes) + 1); 
        }
    }

    // para cada chave do MAP, gera um novo Nó e o enfileira
    // Cada Nó tem sua chave e valor 
    frequenciaDeBytes.forEach((key, value) -> filaDePrioridades.enfileirar(new No(key, value)));
}


    //gera o arquivo de compactação
    private void gerarArquivoCompactado(String path){ //logica alterada por etapas e separada por bytes
        ArrayList teste = new ArrayList();
        StringBuilder dados = new StringBuilder(addBytesArvore(arvore));
        try {
            // criar um arquivo e abrir ele para escrita
            File arquivo = new File(path+".zipador");
            RandomAccessFile arquivoDeEscita = new RandomAccessFile(arquivo.getAbsolutePath(),"rw");

            // escreve primeiro os bytes e suas frequencias (byte freq)
            ArrayList<String> filaString = filaDePrioridades.showFila();

            for (int i = 0; i < filaString.size(); i++) {
                if(i%2==0){  //verifica se é par e
                    arquivoDeEscita.writeByte((byte)Integer.parseInt(filaString.get(i)));  //escrevendo um byte
                }
                else {
                    arquivoDeEscita.writeInt(Integer.parseInt(filaString.get(i)));
                }
            }

            //escrever tres bytes 128 para separar
            arquivoDeEscita.write(Byte.MIN_VALUE);
            arquivoDeEscita.write(Byte.MIN_VALUE);
            arquivoDeEscita.write(Byte.MIN_VALUE);

            StringBuilder auxiliar = new StringBuilder();
            int contador = 0;

            if(!(dados.length()%7==0)){
                while (!(dados.length()%7==0)){
                    dados.append("0");
                    contador++;
                }
            }

            for (int i = 0; i < dados.length(); i++) {
                auxiliar.append(dados.charAt(i)); //criar grupos de 7
                if((i+1)%7==0){
                    byte b = deStringParaByte(auxiliar.toString());
                    arquivoDeEscita.write(b); //escrevendo no arquivo
                    auxiliar = new StringBuilder();
                }
            }

            //bytes para separar
            arquivoDeEscita.write(Byte.MIN_VALUE);
            arquivoDeEscita.write(Byte.MIN_VALUE);
            arquivoDeEscita.write(Byte.MIN_VALUE);

            //escrever o contador
            arquivoDeEscita.write(contador);


            //fecha o processo
            arquivoDeEscita.close();
        } catch (IOException e) {
            System.out.println("Erro - gerar arquivo compactado");
            e.printStackTrace();
        }
    }
    private byte deStringParaByte(String x){

        byte ret = (byte)0;

        for (byte ps =(byte)6,pb=(byte)0;pb<7;ps--,pb++) {
            if(x.charAt(ps)=='1'){
                ret = setBit(pb,ret);
            }
        }
        return ret;
    }

    private byte setBit(byte qualBit,byte valor){
        byte mascara = (byte) 1;

        mascara<<=qualBit;

        return (byte) (valor | mascara);
    }

    byte resetBit (byte qualBit,byte valor){
        byte mascara = (byte) 1;

        mascara >>= qualBit;
        mascara = (byte) ~mascara;

        return (byte) (valor & mascara);
    }
}

    public class descompactador {
        String binario;
        FilaDePrioridades filaDePrioridades = new FilaDePrioridades();
        Arvore arvore;
        String extencaoDoArquivo;
        String NomeDoArquivo;

    public void extracao(String arquivoCompactado) throws Exception {
        String arquivo = arquivoCompactado.replace("","");
        String[] ItensDoArquivo = arquivo.split("\\.");

        NomeDoArquivo = ItensDoArquivo[0];
        extencaoDoArquivo= "." + ItensDoArquivo[1];

        lerArquivo(arquivoCompactado);
        arvore = new Arvore((FilaDePrioridades)filaDePrioridades.clone());
        ArrayList<String> filaString = filaDePrioridades.showFila();

        criarCaminhoDeExtracao();
    }

    private void lerArquivo(String arquivo) throws IOException { //lendo os valores em bytes
       
        RandomAccessFile readFile = new RandomAccessFile(arquivo, "rw");
        byte[] dados = new byte[(int)readFile.length()];
        readFile.read(dados);   //popular os dados com os daods do arquivo
        Map<Integer, Integer> frequenciaDosBytes = new HashMap<>();

        // salvar o index atual para demarcar de onde parei
        int indexAtual = 0;

        //varre o array de byte
        //se encontrar os 3 bytes 128 então quebra e salva o local aonde achou os bytes separadores
        for (int i = 0; i < dados.length; i+=5) {
            if(dados[i]==-128 && dados[i+1]==-128 && dados[i+2]==-128 ){
                indexAtual = i+3;
                break;
            }
            // pegar a fila de dentro do array
            if(i%5==0){
//                frequenciaDosBytes.put((int)dados[i], (int)dados[i+1]);
                byte[] b = new byte[4]; //monta um byte inteiro
                b[0] = (dados[i+1]);
                b[1] = (dados[i+2]);
                b[2] = (dados[i+3]);
                b[3] = (dados[i+4]);

                filaDePrioridades.enfileirarNaOrdem(new No((int)dados[i], transformarArrayDeByteEmInt(b)));
            }
        }

        // leitura do caminho da arvore
        StringBuilder binariosDoArquivo = new StringBuilder();
        for (int i = indexAtual; i < dados.length; i++) {
            if(dados[i]==-128 && dados[i+1]==-128 && dados[i+2]==-128 ){
                indexAtual = i+3;
                break;
            }

            if(Integer.toBinaryString(dados[i]).length()!=7){
                StringBuilder zerosQueFaltam = new StringBuilder();

                //forma grupos de 7
                int diferenca = Integer.toBinaryString(dados[i]).length() - 7;

                //recupera os bytes
                // aplicando o modulo para sempre ter numeros positivos
                //adiciona os 0

                if (diferenca < 0) diferenca = diferenca * -1;
                for (int j = 0; j < diferenca; j++) {
                    zerosQueFaltam.append("0");
                }

                // vou adicionando os binarios convertidos
                // dentro do binarios do arquivo

                zerosQueFaltam.append(Integer.toBinaryString(dados[i]));
                binariosDoArquivo.append(zerosQueFaltam);
                zerosQueFaltam = new StringBuilder();
            }
            else{
                binariosDoArquivo.append(Integer.toBinaryString(dados[i]));
            }
        }

        int salvarNumeroDzero = dados[indexAtual];
        StringBuilder binariosOriginais = new StringBuilder();

        // removendo os zeros do final do arquivo
        if(salvarNumeroDzero>0){
            for (int i = 0; i < binariosDoArquivo.length()-salvarNumeroDzero; i++) {
                binariosOriginais.append(binariosDoArquivo.charAt(i));
            }
        }else binariosOriginais = binariosDoArquivo;


        binario = binariosOriginais.toString();

    }

    private void criarCaminhoDeExtracao() throws Exception{
        byte[] bytes = extrairBytesDaArvore(); // extrai os bytes da arvore e insere em um array de bytes
        try {
            FileOutputStream outputStream = new FileOutputStream(NomeDoArquivo+"(dezipado)"+extencaoDoArquivo);
            outputStream.write(bytes);
        } catch (IOException e) {
            System.out.println("Erro - criar caminho de extração");
            e.printStackTrace();
        }
    }

        private byte[] extrairBytesDaArvore() {
            String binarioDescompactado = this.binario;
            ArrayList<Comparable> bytesAlt = new ArrayList<>();
            // resultado recebe os bytes que vao montar o arquivo de volta
            // atraves dos binarios (0101010)
            ArrayList<Comparable> resultado = arvore.findByte(binarioDescompactado);
            for (Comparable b:
                    resultado ) {
                bytesAlt.add(b);
            }

            byte[] bytes = new byte[bytesAlt.size()];
            for (int i = 0; i < bytesAlt.size(); i++) {
                bytes[i] = (byte) Integer.parseInt(bytesAlt.get(i).toString());
            }
            return bytes;
        }

        public int transformarArrayDeByteEmInt(byte[] array){
            int valor = 0;
            for (byte b : array) {
                // para cada byte adiciono o 0xFF nele
                // para poder somar dentro da variavel
                valor = (valor << 8) + (b & 0xFF);
            }

            return valor;
        }

}
}