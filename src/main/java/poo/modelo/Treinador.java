package poo.modelo;

import java.util.List;

public class Treinador extends Card {
    private Acao tipo;
    private int cura;

    //carta pokebola e energia 
    public Treinador(String anId, String anImageId, String nome, Acao tipo) {
        super(anId, anImageId, nome);
        this.tipo = tipo;
    }
    //carta de cura
    public Treinador(String anId, String anImageId, String nome, int cura) {
        super(anId, anImageId, nome);
        tipo = Acao.CURA;
        this.cura = cura; //30 ou 60
    }

    public void treinador(Pokemon pativo, List<Card> descarte, List<Card> banco, List<Card> ativo, List<Card> mao) {
        if (tipo == Acao.CURA) {
            curarDanos(pativo);
        } else if (tipo == Acao.POKEBOLA) {
            if (getNome().equalsIgnoreCase("reviver")) {
                reviver(descarte, banco);
            } else {
                substituicao(banco, ativo, mao);
            }
        } else if (tipo == Acao.ENERGIA) {
            if (getNome().equalsIgnoreCase("substituição de energia")) {
                substituicaoEnergia(pativo, banco);
            } else {
                recuperacaoEnergia(descarte, mao);
            }
        }
        for (Card c : mao) {
            if (c.getNome().equals(getNome())) {//tem que chamar o metodo usando a propria
                mao.remove(c);
                descarte.add(c);
            }
        }
    }

    //curar danos
    public void curarDanos(Pokemon p) {
        if (p.getPs() + cura > p.getVidaMaxima())
            p.setPs(p.getVidaMaxima());
        else
            p.setPs(p.getPs() + cura);
    }


    //mexer em algum baralho e procurar um pokemon basico -reviver
    public void reviver(List<Card> descarte, List<Card> banco) {
        if (banco.size() < 3) {
            for (Card c : descarte) {
                if (c instanceof Pokemon) {
                    Pokemon a = (Pokemon) c;
                    if (a.getGeracaoAnterior().equals(null)) {
                        banco.add(a);
                        descarte.remove(a);
                        break;
                    }
                }
            } 
            //mensagenzinha:
            //"Não há pokémon básico na pilha de descarte!"
        } else {
            //mensagenzinha:
            //"bANCO ESTA CHEIO"
        }
    }

    //trocar o pokemon ativo pelo bem da esquerda do banco
    //substituicao
    public void substituicao(List<Card> banco, List<Card> ativo, List<Card> mao) {

        if (banco.size() != 0) {
            Card c = banco.get(0);
            for (Card card : ativo) {
                if (card instanceof Pokemon) {
                    Pokemon p = (Pokemon)card;
                    if (!p.getGeracaoAnterior().equals(null)) {
                        mao.add(p);
                    } else 
                        banco.add(p);
                } 
            }
            banco.remove(0);
            ativo.clear();
            ativo.add(c);
        } else {
            //mensagenzinha:
            //"Banco está vazio"
        }
    }

    //mexer em algum baralho e procurar 3 cartas de energia (se nao achar, colocar null)
    public void recuperacaoEnergia(List<Card> descarte, List<Card> mao) {
        int contador = 0;
        for (Card c : descarte) {
            if (contador<3) {
                if (c instanceof Energia) {
                    Energia e = (Energia)c;
                    mao.add(e);
                    descarte.remove(e);
                    contador++;
                }
            } else break;
        }

        if (contador == 0) {
            //mensagenzinha avisando que nao achou nenhuma
        }
    }

    public void substituicaoEnergia(Pokemon ativo, List<Card> banco) {
        Pokemon maiorEnergia = null;
        for (Card c : banco) {
            if (c instanceof Pokemon) {
                Pokemon p = (Pokemon)c;
                if (maiorEnergia.equals(null)) {
                    maiorEnergia = p;//primeiro pokemon na lista
                }
                if (!maiorEnergia.equals(null) && p.getEnergia()>maiorEnergia.getEnergia()) {
                    maiorEnergia = p;//so vai se energia for maior que o ultimo pokemon
                }
            }
        }

        int aux = ativo.getEnergia();
        ativo.setEnergia(maiorEnergia.getEnergia());
        maiorEnergia.setEnergia(aux);
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
