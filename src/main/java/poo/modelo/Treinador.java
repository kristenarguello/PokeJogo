package poo.modelo;

import java.util.List;

public class Treinador extends Card {
    private Acao tipo;
    private int cura;
    private String queFaz;

    public String getQueFaz() {
        return queFaz;
    }

    // carta pokebola e energia
    public Treinador(String anId, String anImageId, String nome, Acao tipo, String oqf) {
        super(anId, anImageId, nome);
        this.tipo = tipo;
        this.queFaz = oqf;
    }

    // carta de cura
    public Treinador(String anId, String anImageId, String nome, int cura, String oqF) {
        super(anId, anImageId, nome);
        tipo = Acao.CURA;
        this.cura = cura; // 30 ou 60
        this.queFaz = oqF;
    }

    public boolean treinador(Pokemon pativo, List<Card> descarte, List<Card> banco, List<Card> ativo, List<Card> mao,
            CardDeck mao1, CardDeck banco1, CardDeck ativo1) {
        if (tipo == Acao.CURA) {
            curarDanos(pativo);
            return true;
        } else if (tipo == Acao.POKEBOLA) {
            if (getNome().equalsIgnoreCase("reviver")) {
                return reviver(descarte, mao1);
            } else {
                substituicao(banco1, ativo1, mao1);
                return true;
            }
        } else if (tipo == Acao.ENERGIA) {
            if (getNome().equalsIgnoreCase("substituição de energia")) {
                substituicaoEnergia(pativo, banco);
                return true;
            } else {
                recuperacaoEnergia(descarte, mao1);
                return true;
            }
        }
        return true;
    }

    // curar danos
    public void curarDanos(Pokemon p) {
        if (p.getPs() + cura > p.getVidaMaxima())
            p.setPs(p.getVidaMaxima());
        else
            p.setPs(p.getPs() + cura);
    }

    // mexer em algum baralho e procurar um pokemon basico -reviver
    public boolean reviver(List<Card> descarte, CardDeck mao) {
        boolean tem = false;
        if (descarte.size() > 0) {
            for (Card c : descarte) {
                if (c instanceof Pokemon) {
                    Pokemon a = (Pokemon) c;
                    if (a.getGeracaoAnterior() == null) {
                        mao.addCard(c);
                        descarte.remove(c);
                        tem = true;
                        return true;
                    }
                }
                if (!tem) {
                    return false;
                }
            }
        } return false;
    }

    // trocar o pokemon ativo pelo bem da esquerda do banco
    public void substituicao(CardDeck banco, CardDeck ativo, CardDeck mao) {

        if (banco.getCards().size() != 0) {
            Card c = banco.getBaralho().get(0);
            for (Card card : ativo.getBaralho()) {
                if (card instanceof Pokemon) {
                    Pokemon p = (Pokemon) card;
                    if (p.getGeracaoAnterior() != null) {
                        mao.getBaralho().add(p);
                    } else
                        banco.getBaralho().add(p);
                }
            }
            banco.getBaralho().remove(0);
            ativo.getBaralho().clear();
            ativo.getBaralho().add(c);
            ((Pokemon)c).setPs(((Pokemon)c).getVidaMaxima());
            ((Pokemon)c).setEnergiaComeco();
        }
    }

    // mexer em algum baralho e procurar 3 cartas de energia (se nao achar, colocar
    // null)
    public void recuperacaoEnergia(List<Card> descarte, CardDeck mao) {
        int contador = 0;
        for (Card c : descarte) {
            if (contador < 3) {
                if (c instanceof Energia) {
                    mao.addCard(c);
                    contador++;
                }
            } else
                break;
        }

        while (contador > 0) {
            for (Card c : descarte) {
                if (c instanceof Energia) {
                    descarte.remove(c);
                    break;
                }
            }
            contador--;
        }
    }

    public void substituicaoEnergia(Pokemon ativo, List<Card> banco) {
        Pokemon maiorEnergia = null;
        if (banco.size() > 0) {
            for (Card c : banco) {
                if (c instanceof Pokemon) {
                    Pokemon p = (Pokemon) c;
                    if (maiorEnergia == null) {
                        maiorEnergia = p;// primeiro pokemon na lista
                    }
                    if (!(maiorEnergia == null) && p.getEnergia() > maiorEnergia.getEnergia()) {
                        maiorEnergia = p;// so vai se energia for maior que o ultimo pokemon
                    }
                }
            }

            ativo.setEnergia(maiorEnergia.getEnergia());
        }
    }

    public Acao getTipo() {
        return tipo;
    }

    public int getCura() {
        return cura;
    }

    public String toString() {
        return "TREINADOR -- " + getNome() + " : " + tipo + cura;
    }

}
