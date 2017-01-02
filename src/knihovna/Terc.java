package knihovna;



public class Terc extends Kruh{
    private static final SpravcePlatna SP = SpravcePlatna.getInstance();
    private static final Barva ZLUTA = Barva.ZLUTA;
    private static final Barva MODRA = Barva.MODRA;
    private static final Barva CERVENA = Barva.CERVENA;
    
    private static int poradi;
    
    private final Kruh kruhU;
    private final Kruh kruhS;
    private final Cara caraV;
    private final Cara caraS;
    
    private int krok;
    private int vel2;
    private int vel3;
    private int vel6;
    
    public Terc(){
        this(SP.getKrok()/2, SP.getKrok()/2);
    }
    
    
    public Terc(int x, int y){
        this(x, y, SP.getKrok());
    }
    
    
    public Terc(int x, int y, int velikost){
        this(x, y, velikost, ZLUTA, MODRA, CERVENA);
    }

    
    public Terc(int x, int y, int velikost, Barva barva1, Barva barva2, Barva barva3){
        super();
        this.krok = SP.getKrok();
        this.vel2 = velikost/2;
        this.vel3 = velikost/3;
        this.vel6 = velikost/6;
        kruhU = new Kruh();
        kruhS = new Kruh();
        caraV = new Cara();
        caraS = new Cara();
        setPozice(x, y);
        setRozmer(velikost);
        super.setBarva(barva1);
        kruhU.setBarva(barva2);
        kruhS.setBarva(barva3);
        Terc.poradi++;
    }
    
    
    /***************************************************************************
     * Nastavi novou pozici instance.
     *
     * @param x   Nova x-ova pozice instance
     * @param y   Nova y-ova pozice instance
     */
    @Override
    public void setPozice(int x, int y)
    {
        SP.nekresli(); {
            super.setPozice(x-vel2, y-vel2);
            kruhU.setPozice(x-vel3, y-vel3);
            kruhS.setPozice(x-vel6, y-vel6);
            caraV.setPozice(x-vel2, y);
            caraS.setPozice(x, y-vel2);
        } SP.vratKresli();
    }
    
    
    @Override
    public int getX(){
        Pozice p = getPozice();
        return p.x;
    }
    
    
    @Override
    public int getY(){
        Pozice p = getPozice();
        return p.y;
    }
    
    
    @Override
    public Oblast getOblast(){
        Pozice p = getPozice();
        Rozmer r = super.getRozmer();
        return new Oblast(p, r);
    }
    
    
    @Override
    public Pozice getPozice(){
        int x = caraV.getX();
        int y = caraS.getY();
        return new Pozice(x + vel2, y + vel2);
    }
    

//     @Override
//     public void setRozmer(int sirka, int vyska){
//         int i = Math.min(sirka, vyska);
//         this.setRozmer(i);
//     }
    
    /***************************************************************************
     * Nastavi novou velikost nafukovaciho objektu;
     * pozice objektu by se pritom nemela zmenit.
     *
     * @param vyska  Nova sirka objektu
     * @param sirka  Nova vyska objektu
     */
    @Override
    public void setRozmer(int velikost){
        setRozmer(velikost, velikost);
    }
    
    /***************************************************************************
     * Nastavi novou velikost nafukovaciho objektu;
     * pozice objektu by se pritom nemela zmenit.
     *
     * @param vyska  Nova sirka objektu
     * @param sirka  Nova vyska objektu
     */
    @Override
    public void setRozmer(int sir, int vys){
        Pozice p = getPozice();
        this.vel2 = (sir+vys)/4;
        this.vel3 = (sir+vys)/2/3;
        this.vel6 = (sir+vys)/2/6;
        int x = super.getX();
        int y = super.getY();
            
        SP.nekresli(); {
            super.setRozmer((sir+vys)/2, (sir+vys)/2);
            kruhU.setRozmer(vel3*2, vel3*2);
            kruhS.setRozmer(vel3, vel3);
            caraV.setPozice(x - vel2, y);
            caraV.setKPozice(x + vel2, y);
            caraS.setPozice(x, y - vel2);
            caraS.setKPozice(x, y + vel2);
            setPozice(p);
        } SP.vratKresli();
    }
    
    
    public void setOblast(Oblast o){
        setPozice(o.x, o.y);
        setRozmer(o.sirka, o.vyska);
    }
    
    /***************************************************************************
     * Presune instanci o zadany pocet bodu vpravo,
     * pri zaporne hodnote parametru vlevo.
     *
     * @param vzdalenost Vzdalenost, o kterou se instance presune.
     */
    public void posunVpravo(int vzdalenost)
    {
        setPozice(getX() + vzdalenost, getY() );
    }

    
    /***************************************************************************
     * Presune instanci o zadany pocet bodu dolu,
     * pri zaporne hodnote parametru nahoru.
     *
     * @param vzdalenost    Pocet bodu, o ktere se instance presune.
     */
    public void posunDolu(int vzdalenost)
    {
        setPozice( getX(), getY() + vzdalenost );
    }
    
    
    /***************************************************************************
     * Presune instanci o krok bodu vpravo.
     */
    public void posunVpravo()
    {
        posunVpravo( SP.getKrok() );
    }


    /***************************************************************************
     * Presune instanci o krok bodu vlevo.
     */
    public void posunVlevo()
    {
        posunVpravo( -SP.getKrok() );
    }


    /***************************************************************************
     * Presune instanci o krok bodu dolu.
     */
    public void posunDolu()
    {
        posunDolu( SP.getKrok() );
    }


    /***************************************************************************
     * Presune instanci o krok bodu nahoru.
     */
    public void posunVzhuru()
    {
        posunDolu( -SP.getKrok() );
    }
   
    
    // nakresli xobdelnik
    @Override
    public void nakresli(Kreslitko k)
    {
        super.nakresli(k);
        kruhU.nakresli(k);
        kruhS.nakresli(k);
        caraV.nakresli(k);
        caraS.nakresli(k);
    }
    
    
    @Override
    public String toString(){
        Oblast o = this.getOblast();
        return "terc_"+ poradi + ": x=" + o.x + ", y=" + o.y +
               ", sirka=" + o.sirka + ", vyska=" + o.vyska +
               ", barva=" + super.getBarva();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
