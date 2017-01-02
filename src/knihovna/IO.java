package knihovna;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*******************************************************************************
 * Knihovni trida {@code IO} obsahuje sadu metod
 * pro jednoduchy vstup a vystup prostrednictvim dialogovyach oken
 * spolu s metodou zastavujici beh programu na dany pocet milisekund
 * a metodu prevadejici texty na ASCII jednoduchym odstranenim diakritiky.
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public final class IO
{
//== KONSTANTNI ATRIBUTY TRIDY =================================================

    /** Prepravka pro nulove veikosti okraju. */
    private static final Insets NULOVY_OKRAJ = new Insets(0, 0, 0, 0);

    /** Rozdil mezi tloustkou ramecku okna ohlasovanou pred a po
     *  volani metody {@link #setResizable(boolean)}.
     *  Tento rozdil ve Windows ovlivnuje nastaveni velikosti a pozice.
     *  Pri {@code setResizable(true)} jsou jeho hodnoty vetsi,
     *  a proto se spocte se jako "true" - "false". */
    private static final Insets INSETS_DIF;

    /** Informace o tom, budou-li se opravovat pozice a rozmery oken. */
    private static final boolean OPRAVOVAT;



//== PROMENNE ATRIBUTY TRIDY ===================================================

    /** Pozice dialogovych oken. */
    private static Point poziceOken = new Point(0,0);

    /** Priznak testovaciho rezimu - je-li nastaven na {@code true},
     *  metoda {@link #zprava(Object)} neotevira dialogove okno
     *  a metoda {@link #cekej(int)} neceka. */
    private static boolean testujeme = false;



//== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR ========================

    /** Windows Vista + Windows 7 se neumeji dohodnout s Javou na skutecne
     *  velikosti oken a jejich ramu a v dusledku toho nefunguje spravne
     *  ani umistovani oken na zadane souradnice.
     *  Nasledujici staticky konstruktor se snazi zjistit chovani aktualniho
     *  operacniho systemu a podle toho pripravit potrebne korekce.
     *  Doufejme, ze zahy prestane byt potreba.
     */
    static {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            JFrame okno = new JFrame();
            okno.setLocation(-1000, -1000);
            okno.setResizable(true);
            okno.pack();
            Insets insTrue  = okno.getInsets();
//            System.out.println("Insets - resizable=true:  " + insTrue);
            okno.setResizable(false);
            Insets insFalse = okno.getInsets();
//            System.out.println("Insets - resizable=false: " + insFalse);
            Insets insets;
            insets = new Insets(insTrue.top    - insFalse.top,
                                insTrue.left   - insFalse.left,
                                insTrue.bottom - insFalse.bottom,
                                insTrue.right  - insFalse.right );
            if (NULOVY_OKRAJ.equals(insets)) {
                //Neverim mu, urcite keca
                //TODO Proverit jak je to s tim prepoctem pozice a rozmeru
//                int ubytek = (insTrue.left == 8)  ?  5  :  1;
                int ubytek = 1;
                insets = new Insets(ubytek, ubytek, ubytek, ubytek);
            }
            INSETS_DIF = insets;
            OPRAVOVAT = true;
//            OPRAVOVAT = ! NULOVY_OKRAJ.equals(INSETS_DIF);
        }
        else {
            INSETS_DIF = NULOVY_OKRAJ;
            OPRAVOVAT  = false;
        }
//        System.out.println("INSETS_DIF: " + INSETS_DIF +
//                         "\nOPRAVOVAT:  " + OPRAVOVAT + "\n");
    }



//== KONSTANTNI ATRIBUTY INSTANCI ==============================================
//== PROMENNE ATRIBUTY INSTANCI ================================================
//== PRISTUPOVE METODY VLASTNOSTI TRIDY ========================================
//== OSTATNI NESOUKROME METODY TRIDY ===========================================

    /***************************************************************************
     * Pocka zadany pocet milisekund.
     * Na preruseni nijak zvlast nereaguje - pouze skonci driv.
     * Pred tim vsak nastavi priznak, aby volajici metoda poznala,
     * ze vlakno bylo zadano o preruseni.
     *
     * @param milisekund   Pocet milisekund, po nez se ma cekat.
     */
    public static void cekej(int milisekund)
    {
        if (testujeme) {
            Zpravodaj.zpravodaj.cekej(milisekund);
        }
        else {
            try {
                Thread.sleep(milisekund);
            }catch( InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    /***************************************************************************
     * Pri splneni zadane podminky otevre dialogove okno s napisem KONEC
     * a po jeho zavreni ukonci program.
     *
     * @param plati  Podminka, po jejimz splneni se program ukonci
     */
    public static void konecKdyz(boolean plati)
    {
        konecKdyz(plati, null);
    }


    /***************************************************************************
     * Pri splneni zadane podminky otevre dialogove okno se zadanou zpravou
     * a po jeho zavreni ukonci program.
     *
     * @param plati  Podminka, po jejimz splneni se program ukonci
     * @param zprava Zprava vypisovana v dialogovem okne. Je-li {@code null}
     *               nebo prazdny retezec, vypise <b>{@code KONEC}</b>.
     */
    public static void konecKdyz(boolean plati, String zprava)
    {
        if (plati) {
            if ((zprava == null)  ||  (zprava.equals(""))) {
                zprava = "KONEC";
            }
            zprava(zprava);
            System.exit(0);
        }
    }


    /***************************************************************************
     * Zbavi zadany text diakritickych znamenek; soucasne ale odstrani take
     * vsechny dalsi znaky nespadajici do tabulky ASCII.
     *
     * @param text Text urceny k "odhackovani"
     * @return  "Odhackovany" text
     */
    public static String odhackuj( String text )
    {
        return Odhackuj.text(text);
    }


    /***************************************************************************
     * Nastavi pozici pristiho dialogoveho okna.
     *
     * @param x  Vodorovna souradnice
     * @param y  Svisla souradnice
     */
    public static void oknaNa( int x, int y )
    {
        poziceOken = new Point( x, y );
        if (OPRAVOVAT) {
            poziceOken.x += INSETS_DIF.left;
            poziceOken.y += INSETS_DIF.top + INSETS_DIF.bottom;
        }
    }


    /***************************************************************************
     * Zobrazi dialogove okno se zpravou a umozni uzivateli odpovedet
     * ANO nebo NE. Vrati informaci o tom, jak uzivatel odpovedel.
     * Neodpovi-li a zavre dialog, ukonci program.
     *
     * @param dotaz   Zobrazovany text otazky.
     * @return <b>{@code true}</b> Odpovedel-li uzivatel <b>ANO</b>,
     *         <b>{@code false}</b> odpovedel-li <b>NE</b>
     */
    public static boolean souhlas( Object dotaz )
    {
        JOptionPane jop = new JOptionPane(
                                dotaz,
                                JOptionPane.QUESTION_MESSAGE,   //Message type
                                JOptionPane.YES_NO_OPTION       //Option type
                                );
        processJOP( jop );
        int answer = (Integer)jop.getValue();
        return (answer == JOptionPane.YES_OPTION);
    }


    /***************************************************************************
     * Zobrazi dialogove okno s vyzvou k zadani realne hodoty;
     * pri zavreni okna zaviracim tlacitkem ukonci aplikaci.
     *
     * @param vyzva        Text, ktery se uzivateli zobrazi.
     * @param doubleImpl   Implicitni hodnota.
     * @return Uzivatelem zadana hodnota, resp. potvrzena implicitni hodnota.
     */
    public static double zadej( Object vyzva, double doubleImpl )
    {
        return Double.parseDouble( zadej( vyzva, ""+doubleImpl ).trim() );
    }


    /***************************************************************************
     * Zobrazi dialogove okno s vyzvou k zadani celociselne hodoty;
     * pri zavreni okna nebo stisku tlacitka Cancel
     * se cela aplikace ukonci.
     *
     * @param vyzva     Text, ktery se uzivateli zobrazi.
     * @param intImpl   Implicitni hodnota.
     * @return Uzivatelem zadana hodnota, resp. potvrzena implicitni hodnota.
     */
    public static int zadej( Object vyzva, int intImpl )
    {
        return Integer.parseInt( zadej( vyzva, ""+intImpl ).trim() );
    }


    /***************************************************************************
     * Zobrazi dialogove okno s vyzvou k zadani textove hodoty;
     * pri zavreni okna nebo stisku tlacitka Cancel
     * se cela aplikace ukonci.
     *
     * @param vyzva        Text, ktery se uzivateli zobrazi.
     * @param stringImpl   Implicitni hodnota.
     * @return Uzivatelem zadana hodnota, resp. potvrzena implicitni hodnota.
     */
    public static String zadej( Object vyzva, String stringImpl )
    {
        JOptionPane jop = new JOptionPane(
                              vyzva,
                              JOptionPane.QUESTION_MESSAGE,   //Message type
                              JOptionPane.DEFAULT_OPTION  //Option type - OK
                              );
        jop.setWantsInput(true);
        jop.setInitialSelectionValue(stringImpl);
        processJOP(jop);
        String answer = jop.getInputValue().toString();
        return answer;
    }


    /***************************************************************************
     * Zobrazi dialogove okno se zpravou a pocka,
     * az uzivatel stiskne tlacitko OK;
     * pri zavreni okna zaviracim tlacitkem ukonci celou aplikaci.
     *
     * @param text   Zobrazovany text.
     */
    public static void zprava( Object text )
    {
        if (testujeme) {
            Zpravodaj.zpravodaj.zprava(text);
        }
        else {
            JOptionPane jop = new JOptionPane(
                              text,                            //Sended message
                              JOptionPane.INFORMATION_MESSAGE  //Message type
                              );
            processJOP( jop );
        }
    }



//##############################################################################
//== KONSTRUKTORY A TOVARNI METODY =============================================

    /***************************************************************************
     * Trida IO je knihovni tridou a proto neni urcena k tomu,
     * aby mela nejake instance.
     */
    private IO() {}



//== ABSTRAKTNI METODY =========================================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI =====================================
//== OSTATNI NESOUKROME METODY INSTANCI ========================================
//== SOUKROME A POMOCNE METODY TRIDY ===========================================

    /***************************************************************************
     * Creates a dialog from the given {@link JOptionPane}, makes it non-modal
     * and waits for its closing leaving the entered value in the parameter's
     * attribute {@code value}. If the user closed the dialog
     * from the window's system menu, exit the whole application.
     *
     * @param jop
     */
    private static void processJOP( JOptionPane jop )
    {
        final int WAITING=0, CANCELLED=1;
        final Boolean[] USER = {true, false};

        final JDialog jd = jop.createDialog((JDialog)null, "Information"  );

        jd.addWindowListener( new WindowAdapter()
        {
            /** Set the information about closing the window from its
             *  systme menu - the application will be cancelled. */
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized( USER ) {
                    USER[CANCELLED] = true;
                    System.exit( 1 );
                }
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
                poziceOken = jd.getLocation();
                if( jd.isShowing() ) {
                    return;
                }else{
                    jd.dispose();
                    synchronized( USER ) {
                        USER[WAITING] = false;
                        USER.notifyAll();
                    }
                }
            }
         });

        jd.setModal( false );
        jd.setVisible( true );
        jd.setLocation( poziceOken  );
        jd.toFront();
        jd.setAlwaysOnTop(true);
//        jd.setAlwaysOnTop(false);

        //Waiting until the user answers or closes the dialog
        synchronized( USER ) {
            while( USER[WAITING] ) {
                try {
                    USER.wait();
                } catch (InterruptedException ie ) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }



//== SOUKROME A POMOCNE METODY INSTANCI ========================================
//== INTERNI DATOVE TYPY =======================================================

    /***************************************************************************
     * Trida {@code Odhackuj_RUP} je knihovni tridou poskytujici metodu na
     * odstraneni diakritiky ze zadaneho textu a nasledne prevedeni vsech znaku,
     * jejichz kod je stale vetsi nez 127, na prislusne kodove
     * unikove posloupnosti (escape sekvence).
     */
    private static class Odhackuj
    {
    //== KONSTANTNI ATRIBUTY TRIDY =============================================

        /** Mapa s prevody znaku do ASCII. */
        private static final Map<Character,String> PREVOD =
                                           new HashMap<Character, String>(64);



    //== PROMENNE ATRIBUTY TRIDY ===============================================
    //== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR ====================
        static {
            String[][] dvojice = {

                {"A", "A"},  {"a", "a"},    {"AE", "AE"}, {"ae", "ae"},
                {"C", "C"},  {"c", "c"},
                {"D", "D"},  {"d", "d"},    {"\u00cb", "E"},  {"\u00eb", "e"},
                {"E", "E"},  {"e", "e"},
                {"E", "E"},  {"e", "e"},
                {"I", "I"},  {"i", "i"},    {"\u00cf", "IE"}, {"\u00ef", "ie"},
                {"L", "L"},  {"l", "l"},    {"L", "L"},  {"l", "l"},
                {"N", "N"},  {"n", "n"},
                {"O", "O"},  {"o", "o"},    {"OE", "OE"}, {"oe", "oe"},
                {"O", "O"},  {"o", "o"},
                {"R", "R"},  {"r", "r"},    {"R", "R"},  {"r", "r"},
                {"S", "S"},  {"s", "s"},
                {"T", "T"},  {"t", "t"},
                {"U", "U"},  {"u", "u"},    {"UE", "UE"}, {"ue", "ue"},
                {"U", "U"},  {"u", "u"},
                {"Y", "Y"},  {"y", "y"},    {"\u0178", "YE"}, {"\u00ff", "ye"},
                {"Z", "Z"},  {"z", "z"},
                {"ss", "ss"},
                {"<<", "<<"}, {">>", ">>"},
//                {"",""},
            };
            for( String[] ss : dvojice ) {
                PREVOD.put( new Character(ss[0].charAt(0)),  ss[1] );
            }
            dvojice = null;
        }



    //== KONSTANTNI ATRIBUTY INSTANCI ==========================================
    //== PROMENNE ATRIBUTY INSTANCI ============================================
    //== PRISTUPOVE METODY VLASTNOSTI TRIDY ====================================
    //== OSTATNI NESOUKROME METODY TRIDY =======================================

        /***********************************************************************
         * Zbavi zadany text diakritickych znamenek - <b>POZOR</b> -
         * Spolu s nimi odstrani take vsechny znaky s kodem vetsim nez 127.
         *
         * @param text Text urceny k "odhackovani"
         * @return  "Odhackovany" text
         */
        public static String text( CharSequence text )
        {
            final int DELKA = text.length();
            final StringBuilder sb = new StringBuilder(DELKA);
            for( int i = 0;   i < DELKA;   i++ ) {
                char c = text.charAt(i);
                if( c < 128 ) {
                    sb.append(c);
                }else if( PREVOD.containsKey(c) ) {
                    sb.append( PREVOD.get(c) );
                }else {
                    sb.append( rozepis(c) );
                }
            }
            return sb.toString();
        }



    //##########################################################################
    //== KONSTRUKTORY A TOVARNI METODY =========================================

       /** Soukromy konstruktor branici vytvoreni instance. */
        private Odhackuj() {}


    //== ABSTRAKTNI METODY =====================================================
    //== PRISTUPOVE METODY VLASTNOSTI INSTANCI =================================
    //== OSTATNI NESOUKROME METODY INSTANCI ====================================
    //== SOUKROME A POMOCNE METODY TRIDY =======================================

        /***********************************************************************
         * Rozepise zadany znak do prislusne \u0144ikove k\u00b4dove posloupnosti.
         *
         * @param c Prevadeny znak
         * @return Text ve formatu \\uXXXX
         */
        private static String rozepis(char c) {
            return String.format( "\\u%04x", (int)c );
        }



    //== SOUKROME A POMOCNE METODY INSTANCI ====================================
    //== INTERNI DATOVE TYPY ===================================================
    //== TESTY A METODA MAIN ===================================================
    }



///#############################################################################
///#############################################################################
///#############################################################################

    /***************************************************************************
     * Trida {@code Oprava} je knihovni tridou poskytujici metody
     * pro opravy nejruznejsich nesrovnalosti tykajicich se prace
     * s grafickym vstupem a vystupem.
     */
    public static class Oprava
    {
    //== KONSTANTNI ATRIBUTY TRIDY =============================================
    //== PROMENNE ATRIBUTY TRIDY ===============================================
    //== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR ====================
    //== KONSTANTNI ATRIBUTY INSTANCI ==========================================
    //== PROMENNE ATRIBUTY INSTANCI ============================================
    //== PRISTUPOVE METODY VLASTNOSTI TRIDY ====================================
    //== OSTATNI NESOUKROME METODY TRIDY =======================================

        /***********************************************************************
         * Ve Windows 7 pouzivajicich definuje Java jinou velikost okna,
         * nez odpovida velikosti panelu obrazku.
         *
         * @param cont   Kontejner, jehoz rozmery upravujeme
         */
        public static void poziceOkna(Container cont)
        {
            Point  loc;
            if (OPRAVOVAT) {
                loc = cont.getLocation();
                cont.setLocation(loc.x + INSETS_DIF.left,
                                 loc.y + INSETS_DIF.top);
            }
        }


        /***********************************************************************
         * Ve Windows 7 definuje Java jinou velikost okna,
         * nez odpovida velikosti panelu obrazku.
         *
         * @param cont     Kontejner, jehoz rozmery upravujeme
         */
        public static void rozmerOkna(Container cont)
        {
            Dimension dim;
            if (OPRAVOVAT) {
                dim = cont.getSize();
                cont.setSize(dim.width - INSETS_DIF.left - INSETS_DIF.right,
                             dim.height- INSETS_DIF.top  - INSETS_DIF.bottom);
            }
        }



    //##########################################################################
    //== KONSTRUKTORY A TOVARNI METODY =========================================

       /** Soukromy konstruktor branici vytvoreni instance. */
        private Oprava() {}


    //== ABSTRAKTNI METODY =====================================================
    //== PRISTUPOVE METODY VLASTNOSTI INSTANCI =================================
    //== OSTATNI NESOUKROME METODY INSTANCI ====================================
    //== SOUKROME A POMOCNE METODY TRIDY =======================================
    //== SOUKROME A POMOCNE METODY INSTANCI ====================================
    //== INTERNI DATOVE TYPY ===================================================
    //== TESTY A METODA MAIN ===================================================
    }



///#############################################################################
///#############################################################################
///#############################################################################

    /***************************************************************************
     * Instance rozhrani {@code ITester} predstavuji testovaci objekty,
     * ktere chteji byt zpravovany o zajimavych udalostech.
     */
    public interface ITester
    {
    //== KONSTANTY =============================================================
    //== DEKLAROVANE METODY ====================================================

        /***********************************************************************
         * Oznani zavolani metody {@link IO.cekej(int)}
         * a preda v parametru zadanou dobu cekani.
         *
         * @param ms Zadana doba cekani v milisekundach
         */
        public void cekej(int ms);


        /***********************************************************************
         * Oznani zavolani metody {@link IO.zprava(Object)}
         * a preda v parametru vypisovany text.
         *
         * @param zprava Zobrazovany text
         */
        public void zprava(Object zprava);



    //== ZDEDENE METODY ========================================================
    //== INTERNI DATOVE TYPY ===================================================
    }



///#############################################################################
///#############################################################################
///#############################################################################

    /***************************************************************************
     * Instance tridy {@code Zpravodaj} obstarava komunikaci mezi
     * testovanymi a testovacimi objekty.
     */
    public static class Zpravodaj
    {
    //== KONSTANTNI ATRIBUTY TRIDY =============================================

        /** Prostrednik, ktery prihlasenym testovacim programum preposila
         *  zpravy o zavolani definovanych metod. */
        public static final Zpravodaj zpravodaj = new Zpravodaj();



    //== PROMENNE ATRIBUTY TRIDY ===============================================
    //== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR ====================
    //== KONSTANTNI ATRIBUTY INSTANCI ==========================================

        /** Seznam prihlasenych testovacich programu,
         *  kterym budou preposilany zpravy o volani zadanych metod. */
        private final List<ITester> seznam = new ArrayList<ITester>();



    //== PROMENNE ATRIBUTY INSTANCI ============================================
    //== PRISTUPOVE METODY VLASTNOSTI TRIDY ====================================
    //== OSTATNI NESOUKROME METODY TRIDY =======================================
    //##########################################################################
    //== KONSTRUKTORY A TOVARNI METODY =========================================

       /** Soukromy konstruktor branici vytvoreni instance. */
        private Zpravodaj() {}


    //== ABSTRAKTNI METODY =====================================================
    //== PRISTUPOVE METODY VLASTNOSTI INSTANCI =================================
    //== OSTATNI NESOUKROME METODY INSTANCI ====================================

        /***********************************************************************
         * Prida zadany objekt mezi objekty,
         * kterym oznamuje zavolani definovanych metod.
         *
         * @param tester Pridavany testovaci objekt
         */
        public void prihlas(ITester tester)
        {
            if (seznam.contains(tester)) { return; }
            seznam.add(tester);
            testujeme = true;
        }


        /***********************************************************************
         * Odebere zadany objekt ze seznamu objetku,
         * kterym oznamuje zavolani definovanych metod.
         *
         * @param tester Odebirany testovaci objekt
         */
        public void odhlas(ITester tester)
        {
            seznam.remove(tester);
            if (seznam.isEmpty()) {
                testujeme = false;
            }
        }



    //== SOUKROME A POMOCNE METODY TRIDY =======================================
    //== SOUKROME A POMOCNE METODY INSTANCI ====================================

        /***********************************************************************
         * Oznami zavolani metody {@link IO.cekej(int)}
         * a preda v parametru zadanou dobu cekani.
         *
         * @param ms Zadana doba cekani v milisekundach
         */
        private void cekej(int ms)
        {
            for (ITester it : seznam) {
                it.cekej(ms);
            }
        }


        /***********************************************************************
         * Oznami zavolani metody {@link IO.zprava(Object)}
         * a preda v parametru vypisovany text.
         *
         * @param zprava Zobrazovany text
         */
        private void zprava(Object zprava)
        {
            for (ITester it : seznam) {
                it.zprava(zprava);
            }
        }



    //== INTERNI DATOVE TYPY ===================================================
    //== TESTY A METODA MAIN ===================================================
    }



///#############################################################################
///#############################################################################
///#############################################################################

//== TESTY A METODA MAIN =======================================================
}

