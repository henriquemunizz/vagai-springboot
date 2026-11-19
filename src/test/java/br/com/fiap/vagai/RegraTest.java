package br.com.fiap.vagai;

import br.com.fiap.vagai.model.Regra;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegraTest {
    @Test
    void domingoAtravessaParaSegundaEIntervaloIgualDuraVinteQuatroHoras() {
        Regra regra = new Regra();
        regra.setAtivo(1);
        regra.setDiaSemana(7);
        regra.setInicio(1200);
        regra.setFim(360);
        assertTrue(regra.aplicaEm(7, 1200));
        assertFalse(regra.aplicaEm(7, 1199));
        assertTrue(regra.aplicaEm(1, 359));
        assertFalse(regra.aplicaEm(1, 360));
        regra.setFim(1200);
        assertTrue(regra.aplicaEm(1, 1199));
        assertFalse(regra.aplicaEm(1, 1200));
        regra.setAtivo(0);
        assertFalse(regra.aplicaEm(1, 1199));
    }
}
