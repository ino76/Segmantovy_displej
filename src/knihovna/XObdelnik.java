package knihovna;

/*******************************************************************************
 * Trida {@code XObdelnik} je prazdnou verzi tridy vytvorene v 10. kapitole.
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public class XObdelnik extends Obdelnik{
    private final int krok = Elipsa.SP.getKrok();
    private final Cara cara1;
    private final Cara cara2;
    private int sir2;
    private int vys2;
    
    
    /***************************************************************************
     * Vytvori novou instanci s implicitnimi rozmery, umistenim a barvou.
     * Instance bude mit stred na pruseciku hran prvniho a druheho pole
     * a bude mit vysku 1 pole a sirku 2 pole.
     */
    public XObdelnik(){
        this(SP.getKrok(), SP.getKrok());
    }
    
    
    /***************************************************************************
     * Vytvori novou instanci se zadanou polohou a rozmery
     * a implicitni barvou.
     *
     * @param x      x-ova souradnice instance, x>=0, x=0 ma levy okraj platna
     * @param y      y-ova souradnice instance, y>=0, y=0 ma horni okraj platna
     */
    public XObdelnik(int x, int y){
        this(x, y, SP.getKrok()*2, SP.getKrok());
    }
    
    
    /***************************************************************************
     * Vytvori novou instanci se zadanou polohou a rozmery
     * a implicitni barvou.
     *
     * @param x      x-ova souradnice instance, x>=0, x=0 ma levy okraj platna
     * @param y      y-ova souradnice instance, y>=0, y=0 ma horni okraj platna
     * @param sirka   Sirka vytvarene instance,  sirka > 0
     * @param vyska   Vyska vytvarene instance,  vyska > 0
     */
    public XObdelnik(int x, int y, int sirka, int vyska){
        this(x, y, sirka, vyska, Obdelnik.IMPLICITNI_BARVA);
    }
    
    
    /***************************************************************************
     * Vytvori novou instanci se zadanou polohou a rozmery a barvou.
     *
     * @param x      x-ova souradnice instance, x>=0, x=0 ma levy okraj platna
     * @param y      y-ova souradnice instance, y>=0, y=0 ma horni okraj platna
     * @param sirka  Sirka vytvarene instance,  sirka > 0
     * @param vyska  Vyska vytvarene instance,  vyska > 0
     * @param barva  Barva obdelnika tvoriciho instanci
     */
    public XObdelnik(int x, int y, int sirka, int vyska, Barva barva){
        super();
        cara1 = new Cara();
        cara2 = new Cara();
        
        this.sir2 = getSirka()/2;
        this.vys2 = getVyska()/2;
        setPozice(x, y);
        setRozmer(sirka, vyska);
        super.setBarva(barva);
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
            super.setPozice(x-sir2, y-vys2);
            cara1.setPozice(x-sir2, y-vys2);
            cara2.setPozice(x-sir2, y+vys2);
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
        Pozice p = cara1.getPozice();
        return new Pozice(p.x + sir2, p.y + vys2);
    }
    

    /***************************************************************************
     * Nastavi nove rozmery nafukovaciho objektu zadane jako jako instance
     * tridy {@code Rozmer}; pozice objektu by se pritom nemela zmenit.
     *
     * @param rozmer  Nove rozmery objektu
     */
    @Override
    public void setRozmer( Rozmer rozmer ){
        setRozmer(rozmer.sirka, rozmer.vyska);
    }


    /***************************************************************************
     * Nastavi novou velikost nafukovaciho objektu;
     * pozice objektu by se pritom nemela zmenit.
     *
     * @param vyska  Nova sirka objektu
     * @param sirka  Nova vyska objektu
     */
    @Override
    public void setRozmer(int sirka, int vyska){
        SP.nekresli(); {
            Pozice p = getPozice();
            super.setRozmer(sirka, vyska);
            this.sir2 = sirka/2;
            this.vys2 = vyska/2;
            int x = super.getX() + sir2;
            int y = super.getY() + vys2;
            cara1.setPozice(x-sir2, y-vys2);
            cara1.setKPozice(x-sir2+sirka, y-vys2+vyska);
            cara2.setPozice(x-sir2, y+vys2);
            cara2.setKPozice(x-sir2+sirka, y+vys2-vyska);
            setPozice(p);
        } SP.vratKresli();
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
        cara1.nakresli(k);
        cara2.nakresli(k);
    }
    
    
    @Override
    public String toString(){
        Oblast o = this.getOblast();
        return super.getNazev() + ": x=" + o.x + ", y=" + o.y +
               ", sirka=" + o.sirka + ", vyska=" + o.vyska +
               ", barva=" + super.getBarva();
    }
}


