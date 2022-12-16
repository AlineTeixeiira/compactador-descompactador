
    public class Main {
        public static void main(String[] args) {

            //chama os métodos
            try{
                zipador ce = new zipador();
                zipador.compactador ci = ce.new compactador(); //chama o compactador
                zipador.descompactador de = ce.new descompactador(); //chama o descompactador
                ci.compactar("/home/alinetr/Downloads/zipadorJava (1)/zipadorJava/src/arquivos/flor2.jpg"); //compacta
                de.extracao("/home/alinetr/Downloads/zipadorJava (1)/zipadorJava/src/arquivos/flor2.jpg.zipador"); //extrai e chama o método de descompactação
            }
            catch (Exception e) {
                 throw new RuntimeException(e);
         }
        }
    }
