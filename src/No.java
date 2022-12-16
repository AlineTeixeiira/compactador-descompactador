public class No <X extends Comparable<X>>
{
    //métodos que utilizamos em aula para realizar os exercicios 
    private X  info;

    private int frenquencia;
    private No esq,dir;

    public No (X i)
    {
        this.esq =null;
        this.info=i;
        this.dir =null;
    }

    public No (int frenquencia)
    {
        this.esq =null;
        this.info=null;
        this.dir =null;
        this.frenquencia = frenquencia;
    }

    public No (No e, X i, int f, No d)
    {
        this.esq =e;
        this.info=i;
        this.frenquencia = f;
        this.dir =d;
    }

    public No (No e, int f, No d)
    {
        this.esq =e;
        this.frenquencia=f;
        this.dir =d;
    }

    public No ( X i, int f)
    {
        this.esq =null;
        this.info=i;
        this.frenquencia = f;
        this.dir =null;
    }

    public No getEsq ()
    {
        return this.esq;
    }

    public X getInfo ()
    {
        return this.info;
    }

    public int getFrequencia() {
        return frenquencia;
    }

    public No getDir ()
    {
        return this.dir;
    }

    public void setEsq (No e)
    {
        this.esq=e;
    }

    public void setFrenquencia(int f){
        this.frenquencia = f;
    }

    public void setInfo (X i)
    {
        this.info=i;
    }

    public void setDir (No d)
    {
        this.dir=d;
    }
}