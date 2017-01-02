package knihovna;

/*******************************************************************************
 * Rozhrani {@code IHybaci} nedefinuje zadne nove metody, 
 * pouze slucuje pozadavky svych rodicu pod jednu strechu.
 *
 * @author  Rudolf PECINOVSKY
 * @version 2.05.2611 \u2014 2011-09-25
 */
public interface IHybaci extends INafukovaci, IPosuvny
{
//== VEREJNE KONSTANTY =========================================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI =====================================
//== OSTATNI METODY K IMPLEMENTACI =============================================
//== VNORENE TRIDY =============================================================
    
    /***************************************************************************
     * Trida definuje implicitni verze vsech metod pozadovanych 
     * implementovanym rozhranim.
     */
    public static class Adapter implements IHybaci
    {
        /** {@inheritDoc} */
        public Pozice getPozice() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void setPozice( Pozice pozice ) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void setPozice(int x, int y) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public Rozmer getRozmer() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void setRozmer( Rozmer rozmer ) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void setRozmer(int sirka, int vyska) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public void nakresli( Kreslitko kreslitko ) {
            throw new UnsupportedOperationException();
        }
    }
}

