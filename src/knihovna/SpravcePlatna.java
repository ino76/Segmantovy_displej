package knihovna;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;


/*******************************************************************************
 * Trida <b><code>SpravcePlatna</code></b> slouzi k jednoduchemu kresleni
 * na virtualni platno a pripadne nasledne animaci nakreslenych obrazku.
 * </p><p>
 * Trida neposkytuje verejny konstruktor, protoze chce, aby jeji instance
 * byla jedinacek, tj. aby se vsechno kreslilo na jedno a to same platno.
 * Jedinym zpusobem, jak ziskat odkaz na instanci tridy
 * <code>SpravcePlatna</code>,
 * je volani jeji staticke metody <code>getInstance()</code>.
 * </p><p>
 * Trida <code>SpravcePlatna</code> funguje jako manazer, ktery dohlizi
 * na to, aby se po zmene zobrazeni nektereho z tvaru vsechny ostatni tvary
 * radne prekreslily, aby byly spravne zachyceny vsechny prekryvy
 * a aby se pri pohybu jednotlive obrazce vzajemne neodmazavaly.
 * Aby vse spravne fungovalo, je mozno pouzit jeden ze dvou pristupu:</p>
 * <ul>
 *   <li>Manazer bude obsah platna prekreslovat
 *       <b>v pravidelnych intervalech</b>
 *       bez ohledu na to, jestli se na nem udala nejaka zmena ci ne.
 *       <ul><li>
 *       <b>Vyhodou</b> tohoto pristupu je, ze se zobrazovane objekty
 *       nemusi starat o to, aby se manazer dozvedel, ze se jejich stav zmenil.
 *       </li><li>
 *       <b>Neyhodou</b> tohoto pristupu je naopak to, ze manazer
 *       spotrebovava na neustale prekreslovani jistou cast vykonu
 *       procesoru, coz muze u pomalejsich pocitacu pusobit problemy.
 *       <br>&nbsp;</li></ul></li>
 *   <li>Manazer prekresluje platno <b>pouze na vyslovne pozadani</b>.
 *       <ul><li>
 *       <b>Vyhodou</b> tohoto pristupu je uspora spotrebovaneho vykonu
 *       pocitace v obdobi, kdy se na platne nic nedeje.
 *       </li><li>
 *       <b>Nevyhodou</b> tohoto pristupu je naopak to, ze kreslene
 *       objekty musi na kazdou zmenu sveho stavu upozornit manazera,
 *       aby vedel, zed ma platno prekreslit.
 *   </li>
 * </ul><p>
 * Trida <code>SpravcePlatna</code> poziva druhou z uvedenych strategii,
 * tj. <b>prekresluje platno pouze na pozadani</b>.
 * </p><p>
 * Obrazec, ktery chce byt zobrazovan na platne, se musi nejprve prihlasit
 * u instance tridy <code>SpravcePlatna</code>, aby jej tato zaradila
 * mezi spravovane obrazce (sada metod <code>pridej&hellip;</code>).
 * Prihlasit se vsak mohou pouze instance trid, ktere implementuji
 * rozhrani <code>IKresleny</code>.
 * </p><p>
 * Neprihlaseny obrazec nema sanci byti zobrazen, protoze na platno
 * se muze zobrazit pouze za pomoci kreslitka, jez muze ziskat jedine od
 * instance tridy <code>SpravcePlatna</code>, ale ta je poskytuje pouze
 * instancim, ktere se prihlasily do jeji spravy.
 * </p><p>
 * Obrazec, ktery jiz dale nema byt kreslen, se muze odhlasit zavolanim
 * metody <code>odstran(IKresleny)</code>.Zavolanim metody
 * <code>odstranVse()</code> se ze seznamu spravovanych (a tim i z platna)
 * odstrani vsechny vykreslovane obrazce.
 * </p><p>
 * Efektivitu vykreslovani je mozne ovlivnit volanim metody
 * <code>nekresli()</code>, ktera pozastavi prekreslovani platna po nahlasenych
 * zmenach. Jeji volani je vyhodne napr. v situaci, kdy je treba vykreslit
 * obrazec slozeny z rady mensich obrazcu a bylo by nevhodne prekreslovat
 * platno po vykresleni kazdeho z nich.
 * </p><p>
 * Do puvodniho stavu prevedeme platno volanim metody <code>vratKresli()</code>,
 * ktera vrati vykreslovani do stavu pred poslednim volanim metody
 * <code>nekresli()</code>. Nemuzec se tedy stat, ze by se pri zavolani metody
 * <code>nekresli()</code> v situaci, kdy je jiz vykreslovani pozastaveno,
 * zacalo po nasledem zavolani <code>vratKresli()</code> hned vykreslovat.
 * Po dvou volanich <code>vratKresli()</code> se zacne vykreslovat az po
 * dvou zavolanich <code>vratKresli()</code>.
 * </p><p>
 * Proto platno pouze zadame, aby se vratilo do toho kresliciho stavu,
 * ve kterem bylo v okamziku, kdy jsme je naposledy zadali o to,
 * aby se prestalo prekreslovat. Nemuze se tedy stat, ze by se pri zavolani
 * metody <code>nekresli()</code> v situaci, kdy je jiz vykreslovani
 * pozastaveno, zacalo po naslednem zavolani <code>vratKresli()</code> hned
 * vykreslovat.
 * </p><p>
 * Kazde zavolani metody <code>nekresli()</code> musi byt doplneno
 * odpovidajicim volanim <code>vratKresli()</code>. Teprve kdyz posledni
 * <code>vratKresli()</code> odvola prvni <code>nekresli()</code>, bude
 * prekreslovani opet obnoveno.
 * </p>
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public final class SpravcePlatna
{
////%A+ =999%
//    static {
//        msgS("SpravcePlatna - class constructor");
//    }
//    {
//        msgS("SpravcePlatna - deklarace atributu instance");
//    }
////%A-
//== KONSTANTNI ATRIBUTY TRIDY =================================================

    /** Titulek okna aktivniho platna. */
    private static final String TITULEK_0  = "Platno ovladane spravcem";

    /** Pocatecni polickova sirka aktivni plochy platna. */
    private static final int SIRKA_0 = 6;

    /** Pocatecni polickova vyska aktivni plochy platna. */
    private static final int VYSKA_0 = 6;

    /** Pocatecni barva pozadi platna. */
    private static final Barva POZADI_0 = Barva.KREMOVA;

    /** Pocatecni barva car mrizky. */
    private static final Barva BARVA_CAR_0 = Barva.CERNA;

    /** Implicitni roztec ctvercove site. */
    private static final int KROK_0 = 50;

    /** Maximalni povolena velikost roztece ctvercove site. */
    private static final int MAX_KROK = 200;



//== PROMENNE ATRIBUTY TRIDY ===================================================

    /** Jedina instance tridy. */
    private static volatile SpravcePlatna SP;



//== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR ========================
//== KONSTANTNI ATRIBUTY INSTANCI ==============================================

    /** Aplikacni okno animacniho platna. */
    private final JFrame okno;

    /** Instance lokalni tridy, ktera je zrizena proto, aby odstinila
     *  metody sveho rodice JPanel. */
    private final JPanel platno;

    /** Seznam zobrazovanych predmetu. */
    private final List<IKresleny> predmety = new ArrayList<IKresleny>();



//== PROMENNE ATRIBUTY INSTANCI ================================================

    //Z venku neovlivnitelne Atributy pro zobrazeni platna v aplikacnim okne

        /** Vse se kresli na obraz - ten se snadneji prekresli. */
        private Image obrazPlatna;

        /** Kreslitko ziskane od obrazu platna, na nejz se vlastne kresli. */
        private Kreslitko kreslitko;

        /** Semafor branici prilis castemu prekreslovani. Prekresluje se pouze
         *  je-li ==0. Nesmi byt <0. */
        private int nekreslit = 0;

        /** Priznak toho, ze kresleni prave probiha,
         *  takze vypinani nefunguje. */
        private boolean kreslim = false;

        /** Cary zobrazujici na plante mrizku. */
        private Cara[] vodorovna,   svisla;


    //Primo ovlivnitelne atributy

        /** Roztec ctvercove site. */
        private int krok = KROK_0;

        /** Zobrazuje-li se mrizka. */
        private boolean mrizka = true;

        /** Barva pozadi platna. */
        private Barva barvaPozadi = POZADI_0;

        /** Barva car mrizky. */
        private Barva barvaCar = BARVA_CAR_0;

        /** Sirka aktivni plochy platna v udavana v polich. */
        private int sloupcu = SIRKA_0;

        /** Vyska aktivni plochy platna v udavana v polich. */
        private int radku = VYSKA_0;

        /** Sirka aktivni plochy platna v bodech. */
        private int sirkaBodu = SIRKA_0 * krok;

        /** Vyska aktivni plochy platna v bodech. */
        private int vyskaBodu = VYSKA_0 * krok;

        /** Zda se maji prizpusobivi upozornovat na zmeny rozmeru pole. */
        private boolean hlasitZmenyRozmeru = true;

        /** Zda je mozno menit velikost kroku. */
        private Object vlastnikPovoleniZmenyKroku = null;

        /** Pozice platna na obrazovace - pri pouzivani vice obrazovek
         *  je obcas treba ji po zviditelneni obnovit. */
        Point pozicePlatna;



//== PRISTUPOVE METODY VLASTNOSTI TRIDY ========================================
//== OSTATNI NESOUKROME METODY TRIDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVARNI METODY =============================================

    /***************************************************************************
     * Metoda umoznujici ziskat odkaz na instanci spravce platna
     * a pripadne zviditelnit jeho aplikacni okno.
     * Vraci vsak pokazde odkaz na stejnou instanci,
     * protoze instance platna je jedinacek.
     * <p>
     * Pokud instance pri volani metody jeste neexistuje,
     * metoda instanci vytvori.</p>
     *
     * @return Instance tridy {@code SpravcePlatna}
     */
    public static SpravcePlatna getInstance()
    {
        return getInstance(true);
    }


    /***************************************************************************
     * Metoda umoznujici ziskat odkaz na instanci spravce platna
     * a soucasne nastavit, zda ma byt jeho aplikacni okno viditelne.
     * Vraci vsak pokazde odkaz na stejnou instanci,
     * protoze instance platna je jedinacek.
     * <p>
     * Pokud instance pri volani metody jeste neexistuje,
     * metoda instanci vytvori.</p>
     *
     * @param viditelny Ma-li se zajistit viditelnost instance;
     *                  {@code false} aktualne nastavenou viditelnost nemeni
     * @return Instance tridy {@code SpravcePlatna}
     */
    public static SpravcePlatna getInstance(boolean viditelny)
    {
////%A+ =999%
//        msgS("getInstance()");
////%A-
        if (SP == null) {
            synchronized(SpravcePlatna.class) {
                if (SP == null) {
                    inicializuj();
                }
            }
        }
        if (viditelny) {
            SP.setViditelne(true);
        }
////%A+ =999%
//        msgF("getInstance()");
////%A-
        return SP;
    }


    /***************************************************************************
     * Vytvori instanci tridy - jedinacka => je volan pouze jednou.
     */
    @SuppressWarnings("serial")
    private SpravcePlatna()
    {
////%A+ =999%
//        msgS("SpravcePlatna - Konstruktor - telo");
////%A-
        okno = new JFrame();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////%A+ =999%
//        msg("SpravcePlatna - Konstruktor - Okno pripraveno");
////%A-

        platno = new JPanel()
        {   /** Overrides parent's abstract method. */
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(obrazPlatna, 0, 0, null);
            }
        };
////%A+ =999%
//        msg("SpravcePlatna - Konstruktor - Platno pripraveno");
////%A-
        okno.setContentPane(platno);
////%A+ =999%
//        msgF("SpravcePlatna - Konstruktor - telo");
////%A-
    }



//== ABSTRAKTNI METODY =========================================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI =====================================

    /***************************************************************************
     * Nastavi rozmer platna zadanim bodove velikosti policka a
     * poctu policek ve vodorovnem a svislem smeru.
     * Pri velikosti policka = 1 se vypina zobrazovani mrizky.
     *
     * @param  krok    Nova bodova velikost policka
     * @param  pSirka  Novy pocet policek vodorovne
     * @param  pVyska  Novy pocet policek svisle
     */
    public void setKrokRozmer(int krok, int pSirka, int pVyska)
    {
        setKrokRozmer(krok, pSirka, pVyska, null);
    }


    /***************************************************************************
     * Nastavi rozmer platna zadanim bodove velikosti policka a
     * poctu policek ve vodorovnem a svislem smeru.
     * Pri velikosti policka = 1 se vypina zobrazovani mrizky.
     *
     * @param  krok    Nova bodova velikost policka
     * @param  pSirka  Novy pocet policek vodorovne
     * @param  pVyska  Novy pocet policek svisle
     * @param  menic   Objekt, ktery zada o zmenu rozmeru. Jakmile je jednou
     *                 tento objekt nastaven, nesmi jiz rozmer platna
     *                 menit nikdo jiny.
     */
    public
    synchronized void setKrokRozmer(final int krok,
                                    final int pSirka, final int pVyska,
                                    Object menic)
    {
////%A+ =999%
//        msgS("setKrokRozmer");
////%A-
        setKrokRozmer_OverParametry(krok, pSirka, pVyska, menic);

        nekresli(); {
            int stary = this.krok;
            invokeAndWait(new Runnable() {
                @Override
                public void run()
                {
                    setKrokRozmerInterni(krok, pSirka, pVyska);
                }
            }, "setKrokRozmerInterni from setKrokRozmer");
            pripravCary();
////%A+ >135
//            obvolejPrizpusobive(stary, krok);
////%A-
            setViditelne_Pockam();
        } vratKresli();
////%A+ =999%
//        msgF("setKrokRozmer");
////%A-
    }


    /***************************************************************************
     * Vrati vzdalenost car mrizky = bodovou velikost policka.
     *
     * @return Bodova velikost policka
     */
     public int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastavi vzdalenost car mrizky = bodovou velikost policka.
     * Pri velikosti policka = 1 se vypina zobrazovani mrizky.
     *
     * @param velikost  Nova bodova velikost policka
     */
    public
    void setKrok(int velikost)
    {
        setKrokRozmer(velikost, sloupcu, radku);
    }


    /***************************************************************************
     * Vrati pocet sloupcu platna, tj. jeho polickovou sirku.
     *
     * @return  Aktualni polickova sirka platna (pocet policek vodorovne)
     */
    public int getSloupcu()
    {
        return sloupcu;
    }


    /***************************************************************************
     * Vrati bodovou sirku platna.
     *
     * @return  Aktualni bodova sirka platna (pocet bodu vodorovne)
     */
    public
    int getBsirka()
    {
        return sirkaBodu;
    }


    /***************************************************************************
     * Vrati pocet radku platna, tj. jeho polickovou vysku.
     *
     * @return  Aktualni polickova vyska platna (pocet policek svisle)
     */
    public int getRadku()
    {
        return radku;
    }


    /***************************************************************************
     * Vrati bodovou vysku platna.
     *
     * @return  Aktualni bodova vyska platna (pocet bodu svisle)
     */
    public
    int getBVyska()
    {
        return vyskaBodu;
    }


    /***************************************************************************
     * Vrati polickovy rozmer platna, tj. sirku a vysku v polich.
     *
     * @return  Aktualni polickovy rozmer platna
     */
    public Rozmer getRozmer()
    {
        return new Rozmer(sloupcu, radku);
    }


    /***************************************************************************
     * Nastavi rozmer platna zadanim jeho polickove vysky a sirky.
     *
     * @param  sloupcu  Novy pocet policek vodorovne
     * @param  radku    Novy pocet policek svisle
     */
    public
    void setRozmer(int sloupcu, int radku)
    {
        setKrokRozmer(krok, sloupcu, radku);
    }


    /***************************************************************************
     * Nastavi rozmer platna zadanim jeho polickove vysky a sirky.
     *
     * @param  rozmer  Zadavany rozmer v poctu policek
     */
    public void setRozmer(Rozmer rozmer)
    {
        setRozmer(rozmer.sirka, rozmer.vyska);
    }


    /***************************************************************************
     * Vrati informaci o tom, je-li zobrazovana mrizka.
     *
     * @return Mrizka je zobrazovana = true, neni zobrazovana = false.
     */
    public boolean isMrizka()
    {
    	return mrizka;
    }


    /***************************************************************************
     * V zavislosti na hodnte parametru nastavi nebo potlaci
     * zobrazovani car mrizky.
     *
     * @param zobrazit  Jestli mrizku zobrazovat.
     */
    public synchronized void setMrizka(boolean zobrazit)
    {
        mrizka = zobrazit;
        pripravCary();
        prekresli();
    }


    /***************************************************************************
     * Poskytuje informaci o aktualni viditelnosti okna.
     *
     * @return Je-li okno viditelne, vraci <b>true</b>, jinak vraci <b>false</b>
     */
    public
    boolean isViditelne()
    {
        return okno.isVisible();
    }


    /***************************************************************************
     * V zavislosti na hodnote sveho parametru
     * nastavi nebo potlaci viditelnost platna na displeji.
     *
     * @param viditelne logicka hodnota pozadovane viditelnost (true=viditelne)
     */
    public
    synchronized void setViditelne(final boolean viditelne)
    {
        boolean prekresleno = false;
        boolean zmena = (isViditelne() != viditelne);
        if (! zmena) {
            return;                 //==========>
        }
        if (! viditelne) {
            okno.setVisible(false);
            return;                 //==========>
        }

////%A+ =999%
//        msgS("setViditelne");
////%A-
        //Mame dosud neviditelne okno zobrazit
        pozicePlatna = okno.getLocation();
        if (EventQueue.isDispatchThread()) {
            setViditelneInterni(viditelne);
        }
        else {
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
////%A+ =999%
//                    msgS("run() from setViditelne");
////%A-
                    setViditelneInterni(viditelne);
////%A+ =999%
//                    msgF("run() from setViditelne");
////%A-
                }
            };
            EventQueue.invokeLater(runnable);
        }
////%A+ =999%
//        msgF("setViditelne");
////%A-
    }


    /***************************************************************************
     * Vrati aktualni barvu pozadi.
     *
     * @return  Nastavena barva pozadi
     */
    public Barva getBarvaPozadi()
    {
        return barvaPozadi;
    }


    /***************************************************************************
     * Nastavi pro platno barvu pozadi.
     *
     * @param  barva  Nastavovana barva pozadi
     */
    public synchronized void setBarvaPozadi(Barva barva)
    {
        barvaPozadi = barva;
        kreslitko.setPozadi(barvaPozadi);
        prekresli();
    }


    /***************************************************************************
     * Pomocna metoda pro ucely ladeni aby bylo mozno zkontrolovat,
     * ze na konci metody ma semafor stejnou hodnotu, jako mel na pocatku.
     *
     * @return  Stav vnitrniho semaforu: >0  - nebude se kreslit,<br>
     *                                   ==0 - kresli se,<br>
     *                                   <0  - chyba
     */
    public
    int getNekresli()
    {
        return nekreslit;
    }


    /***************************************************************************
     * Vrati aktualni nazev v titulkove liste okna platna.
     *
     * @return  Aktualni nazev okna
     */
    public String getNazev()
    {
        return okno.getTitle();
    }


    /***************************************************************************
     * Nastavi nazev v titulkove liste okna platna.
     *
     * @param nazev  Nastavovany nazev
     */
    public void setNazev(String nazev)
    {
        okno.setTitle(nazev);
    }


    /***************************************************************************
     * Vrati vodorovnou souradnici aplikacniho okna platna.
     *
     * @return Pozice leveho horniho rohu aplikacniho okna platna.
     */
    public Pozice getPozice()
    {
        return new Pozice(okno.getX(), okno.getY());
    }


    /***************************************************************************
     * Nastavi pozici aplikacniho okna aktivniho platna na obrazovce.
     *
     * @param x  Vodorovna souradnice aplikacniho okna platna.
     * @param y  Svisla souradnice aplikacniho okna platna.
     */
    public synchronized void setPozice(int x, int y)
    {
        okno.setLocation(x, y);
        pozicePlatna = new Point(x, y);
    }


    /***************************************************************************
     * Nastavi pozici aplikacniho okna aktivniho platna na obrazovce.
     *
     * @param pozice  Pozadovana pozice aplikacniho okna platna na obrazovce.
     */
    public void setPozice(Pozice pozice)
    {
        okno.setLocation(pozice.getX(), pozice.getY());
    }


//    /***************************************************************************
//     * Vrati instanci tridy <code>Obrazek</code> zobrazujici zadany vyrez
//     * AktivnihoPlatna.
//     * @param x     Vodorovna pozice pozadovaneho vyrezu
//     * @param y     Svisla pozice pozadovaneho vyrezu
//     * @param sirka Sirka pozadovaneho vyrezu v bodech
//     * @param vyska Vyska pozadovaneho vyrezu v bodech
//     * @return Instance tridy <code>Obrazek</code> zobrazujici zadany vyrez
//     */
//    public Obrazek getObrazek(int x, int y, int sirka, int vyska)
//    {
//        BufferedImage bim = getBufferedImage(x, y, sirka, vyska);
//        return new Obrazek(0, 0, bim);
//    }



//== OSTATNI NESOUKROME METODY INSTANCI ========================================

    /***************************************************************************
     * Prevede instanci na retezec. Pouziva se predevsim pri ladeni.
     *
     * @return Retezcova reprezentace dane instance.
     */
    @Override
    public String toString()
    {
        return getClass().getName() + "(krok=" + krok +
                ", sirka=" + sloupcu + ", vyska=" + radku +
                ", pozadi=" + barvaPozadi + ")";
    }


    /***************************************************************************
     * Vykresli vsechny elementy.
     */
    public void prekresli()
    {
        if (kreslim) {    //Prave prekresluji - volam neprimo sam sebe
            return;
        }
        if ((nekreslit == 0)  &&  isViditelne())   //Mam kreslit a je proc
        {
////%A+ =999%
//            msgS("prekresli");
////%A-
            kreslim = true;
            synchronized(platno) {
                kreslitko.vyplnRam(0, 0, sirkaBodu, vyskaBodu,
                                   barvaPozadi);
                if (mrizka  &&  (barvaCar != barvaPozadi))
                {
                    //Budeme kreslit mrizku -- bude pod obrazci
                    for (int i=0;   i < sloupcu;  ) {
                        svisla[i++].nakresli(kreslitko);
                    }
                    for (int i=0;   i < radku;  ) {
                        vodorovna[i++].nakresli(kreslitko);
                    }
                }
                for (IKresleny predmet : predmety) {
                    predmet.nakresli(kreslitko);
                }
            }

            //Calls to repaint() don\u2019t need to be done
            //from the event-dispatch thread
            platno.repaint();

            kreslim = false;        //Uz nekreslim
////%A+ =999%
//            msgF("prekresli");
////%A-
        }
    }


    /***************************************************************************
     * Potlaci prekreslovani platna, presneji zvysi hladinu potlaceni
     * prekreslovani o jednicku. Navratu do stavu pred volanim teto metody
     * se dosahne zavolanim metody <code>vratKresli()</code>.</p>
     * <p>
     * Metody <code>nekresli()</code> a <code>vratKresli()</code>
     * se tak chovaji obdobne jako zavorky, mezi nimiz je vykreslovani
     * potlaceno.</p>
     */
    public synchronized void nekresli()
    {
        nekreslit++;
    }


    /***************************************************************************
     * Vrati prekreslovani do stavu pred poslednim volanim metody
     * <code>nekresli()</code>. Predchazelo-li proto vice volani metody
     * <code>nekresli()</code>, zacne se prekreslovat az po odpovidajim poctu
     * zavolani metody <code>vratKresli()</code>.
     *
     * @throws IllegalStateException
     *         Je-li metoda volana aniz by predchazelo odpovidajici volani
     *         <code>nekresli()</code>.
     */
    public synchronized void vratKresli()
    {
        if (nekreslit == 0) {
            throw new IllegalStateException(
                "Vraceni do stavu kresleni musi prechazet zakaz!");
        }
        nekreslit--;
        if (nekreslit == 0)  {
            prekresli();
        }
    }


    /***************************************************************************
     * Odstrani zadany obrazec ze seznamu malovanych.
     * Byl-li obrazec v seznamu, prekresli platno.
     *
     * @param obrazec  Odstranovany obrazec
     *
     * @return  true v pripade, kdyz obrazec v seznamu byl,
     *          false v pripade, kdyz nebylo co odstranovat
     */
    public synchronized boolean odstran(IKresleny obrazec)
    {
        boolean ret = predmety.remove(obrazec);
        if (ret) {
            prekresli();
        }
        return ret;
    }


    /***************************************************************************
     * Vycisti platno, tj. vyprazdni seznam malovanych
     * (odstrani z nej vsechny obrazce).
     */
    public void odstranVse()
    {
        nekresli(); {
            ListIterator<IKresleny> it = predmety.listIterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        } vratKresli();
    }


    /***************************************************************************
     * Neni-li zadany obrazec v seznamu malovanych, prida jej na konec
     * (bude se kreslit jako posledni, tj. na vrchu.
     * Byl-li obrazec opravdu pridan, prekresli platno.
     * Objekty budou vzdy kresleny v poradi, v nemz byly pridany do spravy,
     * tj. v seznamu parametru zleva doprava
     * a drive zaregistrovane objekty pred objekty zaregistrovanymi pozdeji.
     *
     * @param  obrazec  Pridavane obrazce
     * @return  Pocet skutecne pridanych obrazcu
     */
    public synchronized int pridej(IKresleny... obrazec)
    {
        int pocet = 0;
        nekresli(); {
            for (IKresleny ik : obrazec)
            {
                if (! predmety.contains(ik)) {
                    predmety.add(ik);
                    pocet++;
                }
            }
        } vratKresli();
        return pocet;
    }


    /***************************************************************************
     * Prida obrazec do seznamu malovanych tak, aby byl kreslen
     * nad zadanym obrazcem.
     * Pokud jiz v seznamu byl, jenom jej presune do zadane pozice.
     *
     * @param  soucasny  Obrazec, ktery ma byt pri kresleni pod
     *                   pridavanym obrazcem
     * @param  pridany   Pridavany obrazec
     *
     * @return  {@code true}  v pripade, kdyz byl obrazec opravdu pridan,
     *          {@code false} v pripade, kdyz jiz mezi zobrazovanymi byl
     *          a pouze se presunul do jine urovne
     */
    public synchronized boolean pridejNad(IKresleny soucasny,
                                          IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        int kam = predmety.indexOf(soucasny);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referencni objekt neni na platne zobrazovan!");
        }
        predmety.add(kam+1, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Prida obrazec do seznamu malovanych tak, aby byl kreslen
     * pod zadanym obrazcem.
     * Pokud jiz v seznamu byl, jenom jej presune do zadane pozice.
     *
     * @param  soucasny  Obrazec, ktery ma byt pri kresleni nad
     *                   pridavanym obrazcem
     * @param  pridany   Pridavany obrazec
     *
     * @return  true  v pripade, kdyz byl obrazec opravdu pridan,
     *          false v pripade, kdyz jiz mezi zobrazovanymi byl
     *                a pouze se presunul do jine urovne
     */
    public synchronized boolean pridejPod(IKresleny soucasny,
                                          IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        int kam = predmety.indexOf(soucasny);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referencni objekt neni na platne zobrazovan!");
        }
        predmety.add(kam, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Prida obrazec do seznamu malovanych tak, aby byl kreslen
     * nad vsemi obrazci.
     * Pokud jiz v seznamu byl, jenom jej presune do pozadovane pozice.
     *
     * @param  pridany   Pridavany obrazec
     *
     * @return  true  v pripade, kdyz byl obrazec opravdu pridan,
     *          false v pripade, kdyz jiz mezi zobrazovanymi byl
     *                a pouze se presunul do jine urovne
     */
    public
    synchronized boolean pridejNavrch(IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        predmety.add(pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Prida obrazec do seznamu malovanych tak, aby byl kreslen
     * pod zadanym obrazcem.
     * Pokud jiz v seznamu byl, jenom jej presune do zadane pozice.
     *
     * @param  pridany   Pridavany obrazec
     *
     * @return  true  v pripade, kdyz byl obrazec opravdu pridan,
     *          false v pripade, kdyz jiz mezi zobrazovanymi byl
     *                a pouze se presunul do jine urovne
     */
    public
    synchronized boolean pridejDospod(IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        predmety.add(0, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Vrati poradi zadaneho prvku v seznamu kreslenych prvku.
     * Prvky se pritom kresli v rostoucim poradi, takze obrazec
     * s vetsim poradim je kreslen nad obrazcem s mensim poradim.
     * Neni-li zadany obrazec mezi kreslenymi, vrati -1.
     *
     * @param  obrazec  Objekt, na jehoz kreslici poradi se dotazujeme
     *
     * @return  Poradi obrazce; prvy kresleny obrazec ma poradi 0.
     *          Neni-li zadany obrazec mezi kreslenymi, vrati -1.
     */
    public
    int poradi(IKresleny obrazec)
    {
        return predmety.indexOf(obrazec);
    }


    /***************************************************************************
     * Vrati nemodifikovatelny seznam vsech spravovanych obrazku.
     *
     * @return  Pozadovany seznam
     */
    public
    List<IKresleny> seznamKreslenych()
    {
        return Collections.unmodifiableList(predmety);
    }


    /***************************************************************************
     * Nastavi, zda se maji prihlasenym posluchacum hlasit zmeny
     * velikosti kroku a vrati puvodni nastaveni.
     *
     * @param hlasit  Pozadovane nastaveni (true=hlasit, false=nehlasit).
     * @return Puvodni nastaveni
     */
    public boolean hlasitZmenyRozmeru(boolean hlasit)
    {
        boolean ret = hlasitZmenyRozmeru;
        hlasitZmenyRozmeru = hlasit;
        return ret;
    }


    /***************************************************************************
     * Prihlasi posluchace udalosti klavesnice.
     *
     * @param posluchac  Prihlasovany posluchac
     */
    public
    void prihlasKlavesnici(KeyListener posluchac)
    {
        okno.addKeyListener(posluchac);
    }


    /***************************************************************************
     * Odhlasi posluchace klavesnice.
     *
     * @param posluchac  Odhlasovany posluchac
     */
    public
    void odhlasKlavesnici(KeyListener posluchac)
    {
        okno.removeKeyListener(posluchac);
    }


    /***************************************************************************
     * Prihlasi posluchace udalosti mysi.
     *
     * @param posluchac  Prihlasovany posluchac
     */
    public
    void prihlasMys(MouseListener posluchac)
    {
        okno.addMouseListener(posluchac);
    }


    /***************************************************************************
     * Odhlasi posluchace mysi.
     *
     * @param posluchac  Odhlasovany posluchac
     */
    public
    void odhlasMys(MouseListener posluchac)
    {
        okno.removeMouseListener(posluchac);
    }


//    /***************************************************************************
//     * Ulozi obraz aktivniho platna do zadaneho souboru.
//     *
//     * @param soubor Soubor, do nejz se ma obraz platna ulozit
//     */
//    public
//    void ulozJakoObrazek(File soubor)
//    {
//        BufferedImage bim = getBufferedImage();
//        try {
//            ImageIO.write(bim, "PNG", soubor);
//        } catch(IOException exc)  {
//            throw new RuntimeException(
//            	"\nObraz aktivniho platna se nepodarilo ulozit do souboru " +
//                soubor,  exc);
//        }
//    }



//== SOUKROME A POMOCNE METODY TRIDY ===========================================

    /***************************************************************************
     * Bezi-li ve vlakne udalosti, vykona zadanou akci,
     * v opacnem pripade ji zaradi do vlakna a pocka na jeji dokonceni.
     *
     * @param akce Spustena akce
     * @param nazev Nazev akce pro kontrolni tisky
     */
    private static void invokeAndWait(Runnable akce, String nazev)
    {
        if (EventQueue.isDispatchThread()) {
            akce.run();
            return;
        }
        try {
            String zprava = "fronta udalosti - " + nazev;
            msgS(zprava);
            EventQueue.invokeAndWait(akce);
            msgF(zprava);
        }
        catch (Exception ex) {
            throw new RuntimeException( "\nSpusteni akce <<" + nazev +
                      ">> ve fronte udalosti se nezdarilo", ex);
        }
    }


    /***************************************************************************
     * Precte parametry z konfiguracniho souboru.
     * Tento soubor je umisten v domovskem adresari uzivatele
     * ve slozce {@code .rup} v souboru {@code bluej.properties}.
     *
     * @return Pozice, na kterou se ma umistit aplikacni okno
     */
    private static Point konfiguraceZeSouboru()
    {
        Point pozice;

        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File(userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
            String sx = rupProp.getProperty("canvas.x");
            String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            pozice = new Point(x, y);
        }catch(Exception e)  {
            pozice = new Point(0, 0);
        }
        return pozice;
    }


    /***************************************************************************
     * Initialize a canvas manager by putting the initializing code
     * into the AWT Event Queue.
     */
    private static void inicializuj()
    {
////%A+ =999%
//        msgS("inicializuj()");
////%A-
        final Point   pozice  = konfiguraceZeSouboru();
        final Kutloch kutloch = new Kutloch();

        Runnable pripravSP = new Runnable() {
            @Override public void run()
            {
////%A+ =999%
//                msgS("run() z inicializuj()");
////%A-
                pripravSP(pozice, kutloch);
////%A+ =999%
//                msgF("run() z inicializuj()");
////%A-
            }
        };
        try {
            EventQueue.invokeAndWait(pripravSP);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter  pw = new PrintWriter(sw);

            sw.write("\nCreation of CanvasManager doesn't succeed\n");
            ex.printStackTrace(pw);

            String msg = sw.toString();
            System.err.println(msg);
            JOptionPane.showMessageDialog(null, msg);

            System.exit(1);
        }

        //Spravce je vytvoren, budeme umistovat dialogove okna
        SpravcePlatna spravce = kutloch.spravce;
        int x = spravce.okno.getX();
        int y = spravce.okno.getY() + spravce.okno.getHeight();
        IO.oknaNa(x, y);

        //Vse je hotovo, muzeme atribut inicializovat
        SP = spravce;
        //Cary se mohou vytvorit az po inicializaci spravce,
        //protoze si o nej trida car rekne
        SP.pripravCary();

////%A+ =999%
//        msgF("inicializuj(): " + SP);
////%A-
    }


    /***************************************************************************
     * Prepares a canvas manager and its application window
     * while running in AWT Event Queue.
     *
     * @param pozice  Position of the created application window
     * @param kutloch Prepravka pro umisteni vraceneho odkazu na spravce
     */
    private static void pripravSP(Point pozice, Kutloch kutloch)
    {
////%A+ =999%
//        msgS("pripravSP(" + pozice + ")");
////%A-
        SpravcePlatna spravce = new SpravcePlatna();
        spravce.setNazev(TITULEK_0);
        spravce.setPozice(pozice.x, pozice.y);
        spravce.setKrokRozmerInterni(KROK_0, SIRKA_0, VYSKA_0);

        kutloch.spravce = spravce;
////%A+ =999%
//        msgF("pripravSP()");
////%A-
    }



//== SOUKROME A POMOCNE METODY INSTANCI ========================================

//    /***************************************************************************
//     * Vrati obrazek na aktivnim platne.
//     *
//     * @return Obsah platna jako obrazek
//     */
//    private BufferedImage getBufferedImage()
//    {
//        if (obrazPlatna instanceof BufferedImage) {
//            return (BufferedImage) obrazPlatna;         //==========>
//        }
//        else {
//            return getBufferedImage(0, 0, sirkaBodu, vyskaBodu);
//        }
//    }


//    /***************************************************************************
//     * Vrati obrazek vyrezu na aktivnim platne.
//     *
//     * @param x
//     * @param y
//     * @param sirka
//     * @param vyska
//     * @return Vyrez obsahu platna jako obrazek
//     */
//    private BufferedImage getBufferedImage(int x, int y, int sirka, int vyska)
//    {
//        BufferedImage ret = new BufferedImage(sirka, vyska,
//                                              BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = (Graphics2D)ret.getGraphics();
//        g2d.drawImage(obrazPlatna, -x, -y, Kreslitko.OBRAZOR);
//        return ret;
//    }


    /***************************************************************************
     * Obvola vsechny spravovane prizpusobive objekty
     * a oznami jim zmenu kroku.
     *
     * @param staryKrok Puvodni velikost kroku
     *                  (aby si ji prizpusobivi nemuseli pamatovat)
     * @param novyKrok  Nova velikost kroku
     */
    private void obvolejPrizpusobive(int staryKrok, int novyKrok)
    {
        if (hlasitZmenyRozmeru  &&  (staryKrok != novyKrok)) {
            nekreslit++; {
                for (IKresleny ik : seznamKreslenych()) {
                    if (ik instanceof IPrizpusobivy) {
                        ((IPrizpusobivy)ik).krokZmenen(staryKrok, novyKrok);
                    }
                }
            }nekreslit--;
        }
    }


    /***************************************************************************
     * Pripravi cary vyznacujici jednotliva pole aktivniho platna.
     * Pokud se cary kreslit nemaji, vyprazdni odkazy na ne.
     */
    private void pripravCary()
    {
        if (mrizka  &&  (krok > 1))
        {
            if ((svisla == null)  ||  (svisla.length != sloupcu)) {
                svisla = new Cara[sloupcu];
            }
            if ((vodorovna == null)  ||
                (vodorovna.length != radku))
            {
                vodorovna = new Cara[radku];
            }
            for (int i=0, x=krok;   i < sloupcu;      i++, x+=krok) {
                svisla[i] = new Cara(x, 0, x, vyskaBodu, barvaCar);
            }
            for (int i=0, y=krok;   i < radku;   i++, y+=krok) {
                vodorovna[i] = new Cara(0, y, sirkaBodu, y, barvaCar);
            }
        }
        else
        {
            //Uvolneni doposud pouzivanych instanci
            svisla    = null;
            vodorovna = null;
            mrizka    = false;
        }
    }


    /***************************************************************************
     *
     * @param krok
     * @param pSirka
     * @param pVyska
     * @param menic
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws HeadlessException
     */
    private void setKrokRozmer_OverParametry(int krok,
                                             int pSirka, int pVyska,
                                             Object menic)
            throws IllegalArgumentException,
                   IllegalStateException,
                   HeadlessException
    {
        //Kontrola, jestli rozmery meni ten, kdo je menit smi
        if ((menic != null)  &&
            (menic != vlastnikPovoleniZmenyKroku))
        {
            if (vlastnikPovoleniZmenyKroku == null) {
                vlastnikPovoleniZmenyKroku = menic;
            } else {
                throw new IllegalStateException(
                    "Zmena kroku a rozmeru neni danemu objektu povolena");
            }
        }
        //Overeni korektnosti zadanych parametru
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        if ((krok   < 1)  ||
            (pSirka < 2)  ||  (obrazovka.width  < sirkaBodu) ||
            (pVyska < 2)  ||  (obrazovka.height < vyskaBodu))
        {
            throw new IllegalArgumentException(
                "\nSpatne zadane rozmery: " +
                "\n  krok =" + krok  + " bodu," +
                "\n  sirka=" + pSirka +
                   " poli = " + pSirka*krok + " bodu," +
                "\n  vyska=" + pVyska +
                   " poli = " + pVyska*krok + " bodu," +
                "\n  obrazovka= " + obrazovka.width  + "\u00d7" +
                                    obrazovka.height + " bodu\n");
        }
    }


    /***************************************************************************
     *
     * @param pSirka
     * @param krok
     * @param pVyska
     */
    private void setKrokRozmerInterni(int krok, int pSirka, int pVyska)
    {
////%A+ =999%
//        msgS("setKrokRozmerInterni: krok=" + krok + ", pSirka=" + pSirka +
//                                                    ", pVyska=" + pVyska);
////%A-
        sirkaBodu = pSirka * krok;
        vyskaBodu = pVyska * krok;

        okno.setResizable(true);
        platno.setPreferredSize(new Dimension(sirkaBodu, vyskaBodu));
        okno.pack();
        okno.setResizable(false);

        obrazPlatna = platno.createImage(sirkaBodu, vyskaBodu);
        kreslitko   = new Kreslitko((Graphics2D)obrazPlatna.getGraphics());
        kreslitko.setPozadi(barvaPozadi);

        this.krok    = krok;
        this.sloupcu = pSirka;
        this.radku   = pVyska;
//        IO.Oprava.rozmerOkna(okno);
//        IO.Oprava.poziceOkna(okno);
////%A+ =999%
//        msgF("setKrokRozmerInterni");
////%A-
    }


    /***************************************************************************
     *
     * @param viditelne
     */
    private void setViditelneInterni(final boolean viditelne)
    {
////%A+ =999%
//        msgS("setViditelneInterni");
////%A-
        okno.setVisible(viditelne);
        if (viditelne)
        {
            //Na WinXP pri vice obrazovkach po zviditelneni blblo
            //=> bylo treba znovu nastavit pozici
            okno.setLocation(pozicePlatna);

            okno.setAlwaysOnTop(true);
            okno.toFront();
            prekresli();
            okno.pack();
            okno.setAlwaysOnTop(false);
        }
////%A+ =999%
//        msgF("setViditelneInterni");
////%A-
    }


    /***************************************************************************
     *
     */
    private void setViditelne_Pockam()
    {
////%A+ =999%
//        msgS("setViditelne_Pockam");
////%A-
        if (EventQueue.isDispatchThread()) {
            okno.setVisible(true);
            okno.pack();
////%A+ =999%
//            msgF("setViditelne_Pockam - from DispatchThread");
////%A-
            return;
        }
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
////%A+ =999%
//                msgS("run() from setViditelne_Pockam");
////%A-
                okno.setVisible(true);
                prekresli();
                okno.pack();
////%A+ =999%
//                msgF("run() from setViditelne_Pockam");
////%A-
            }
        };
        try {
            EventQueue.invokeAndWait(runnable);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(
                "\nVyjimka vyhozena behem nastavovani kroku a rozmeru", ex );
        }
////%A+ =999%
//        msgF("setViditelne_Pockam");
////%A-
    }



//== VNORENE A VNITRNI TRIDY ===================================================

    /***************************************************************************
     * Prepravka, v niz uzaver predava vytvoreneho spravce.
     */
    private static class Kutloch
    {
        volatile SpravcePlatna spravce;
    }



//== TESTY A METODA MAIN =======================================================

    private static void msgS(String text) {
//        rup.cesky.utility.ThreadMessages.msgS(text);
    }
    private static void msg(String text) {
//        rup.cesky.utility.ThreadMessages.msg(text);
    }
    private static void msgF(String text) {
//        rup.cesky.utility.ThreadMessages.msgF(text);
    }


    /***************************************************************************
     *
     */
    private static void w() {
        try{
            Thread.sleep(10);
        }
        catch(InterruptedException e){}
    }


//    /***************************************************************************
//     * Testovaci metoda
//     */
//    public static void test2()
//    {
//        //Abych zarucil inicializovanost SP
//        SpravcePlatna sp = SpravcePlatna.getInstance();

//        sp.pridej(new Obdelnik());                    w();
//        sp.pridej(new Elipsa(), new Trojuhelnik());   w();
//        IO.zprava("Vychozi obrazek - budu vyjimat vyrez");
//        Obrazek obr = SP.getObrazek(50, 0, 75, 75);   w();
//        sp.pridej(obr);                               w();
//        sp.setBarvaPozadi(Barva.CERNA);               w();
//        IO.zprava("Obrazek pridany?");
//        obr.setPozice(100, 50);                       w();
//        IO.zprava("Posunuty?");
//        obr.setRozmer(150, 150);                      w();
//        IO.zprava("Zvetseny?");
//        sp.setKrokRozmer(50, 5, 2);
////        SP.setKrokRozmer(1, 50, 50);
////        SP.ulozJakoObrazek(new File("D:/SMAZAT.PNG"));

//        System.exit(0);
//    }


    /***************************************************************************
     * Testovaci metoda
     */
    public static void test()
    {
////%A+ =999%
//        msgS("test");
////%A-
        getInstance();  //Abych zarucil nastavenost SP
////%A+ =999%
//        msg("test - SP vytvoren");
////%A-
        IO.zprava("Platno vytvoreno");
////%A+ =999%
//        msg("test - Prvni IO zavreno");
////%A-
        SP.pridej(new Obdelnik   (0, 0, 300, 300));
        SP.pridej(new Elipsa     (0, 0, 300, 300));
        SP.pridej(new Trojuhelnik(0, 0, 300, 300));
////%A+ =999%
//        msg("test - triumvirat vytvoren");
////%A-
        IO.zprava("Triumvirat nakreslen");
////%A+ =999%
//        msg("test - Druhe IO zavreno");
////%A-

        SP.pridej(new Obdelnik   ( 99,  99, 102, 102),
                  new Elipsa     (100, 100, 100, 100),
                  new Trojuhelnik(100, 100, 100, 100));
////%A+ =999%
//        msg("test - druhy triumvirat vytvoren");
////%A-
        IO.zprava("Druhy triumvirat nakreslen");
////%A+ =999%
//        msg("test - Treti IO zavreno");
////%A-

////%A+ =999%
//        msgF("test");
////%A-
        System.exit(0);
    }
    /** @param args Paremtry prikazoveho radku - nepouzite  */
    public static void main(String[] args) { test(); } /**/
////%A+ =999%
//    {
//        msgF("SpravcePlatna - deklarace atributu instance");
//    }
//    static {
//        msgF("SpravcePlatna - class constructor");
//    }
////%A-
}

