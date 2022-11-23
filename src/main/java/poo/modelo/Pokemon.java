package poo.modelo;

import java.util.ArrayList;

public class Pokemon extends Card {

    private int valorAtaque;
    private int energiaGasta;
    private int ps;
    private int vidaMaxima;
    private String geracaoAnterior;
    private boolean evoluido;
    private Tipos tipo;
    private int energia;


    

    public Pokemon(int valorAtaque, int ps, Tipos tipo, String nome, String geracaoAnterior, String anId, String anImageId) {
        super(anId, anImageId,nome);
        this.valorAtaque = valorAtaque;
        this.ps = ps;
        vidaMaxima = ps;
        this.tipo = tipo;
        this.geracaoAnterior = geracaoAnterior;
        evoluido = false;
        energia = 10;

        if(geracaoAnterior==null) {
            energiaGasta = 10;
        } else {
            energiaGasta = 20;
        }
    }

    //aplicar evolucao
    public void evoluir(Pokemon evolucao, ArrayList<Card> ativo) {//chama a partir do basico 
        if (getNome().equals(evolucao.getGeracaoAnterior())) {
            ativo.add(evolucao);
            //PERGUNTAAAAA lista pra ativos ou outro baralho pra colocar os basicos
            //fazer na app, posicao ultima sendo o ativo msm
        } else {
            //faz a mensagenzinha:
            //"Evolução não compatível! Tente com outra carta numa próxima rodada."
        }
    }

    //ataque acontece (dano)
    public void atacar(Pokemon atacante, Pokemon atacado) {
        if (atacante.getEnergia()>=atacante.getEnergiaGasta()) {
            double diferenca = 1;
            if (atacante.getTipo()==Tipos.FOGO) {
                if (atacado.getTipo()==Tipos.PLANTA) {
                    diferenca = 2;
                } else if (atacado.getTipo()==Tipos.AGUA) {
                    diferenca = 0.5;
                }
            } else if (atacante.getTipo()==Tipos.AGUA) {
                if (atacado.getTipo()==Tipos.FOGO) {
                    diferenca = 2;
                } else if (atacado.getTipo()==Tipos.PLANTA) {
                    diferenca = 0.5;
                }
            } else if (atacante.getTipo()==Tipos.PLANTA) {
                if (atacado.getTipo()==Tipos.AGUA) {
                    diferenca = 2;
                } else if (atacado.getTipo()==Tipos.FOGO) {
                    diferenca = 0.5;
                }
            }
            atacante.setEnergia(atacante.getEnergiaGasta());
            atacado.setPs(atacado.getPs() - (int)(atacado.getValorAtaque() * diferenca));
        } else {
            //mensagem que nem a da vez, dizendo que nao tem energia pra atacar
            //dizer quanto de energia tem e quaanto precisa (usar os getters)
        }
    }

    //morre
    public static void morrer(ArrayList<Card> descarte, ArrayList<Card> ativo) {
        if (ativo.size()==1) {
            Card a = ativo.get(0);
            Pokemon p = (Pokemon)a;
            if (p.getPs() <= 0) {
                p.setEnergia();
                descarte.add(p);
            }
        } else {
            for (Card c : ativo) {
                if (c instanceof Pokemon) {
                    Pokemon p = (Pokemon)c;
                    if (!p.getGeracaoAnterior().equals(null)) {
                        if (p.getPs() <= 0) {
                            for (Card a : ativo) {
                                Pokemon b = (Pokemon)a;
                                b.setEnergia();
                                descarte.add(b);
                            }
                        }
                    }
                }
            }
        }     
        ativo.clear();   
    }

    public int getValorAtaque() {
        return valorAtaque;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getGeracaoAnterior() {
        return geracaoAnterior;
    }

    public boolean isEvoluido() {
        return evoluido;
    }

    public void setEvoluido(boolean evoluido) {
        this.evoluido = evoluido;
    }

    public Tipos getTipo() {
        return tipo;
    }

    public void setTipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia -= energia;
    }

    public void setEnergia() {
        this.energia = 10;
    }

    public int getEnergiaGasta() {
        return energiaGasta;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public String toString() {
        return "POKEMON -- " + getNome() + " : " + tipo;
    }
}
