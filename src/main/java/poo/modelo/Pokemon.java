package poo.modelo;

//import java.util.List;

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
    public boolean evoluir(Pokemon evolucao, CardDeck ativo) {//chama a partir do basico 
        if (getNome().equals(evolucao.getGeracaoAnterior())) {
            ativo.addCard(evolucao);
            return true;
        } else {
            //faz a mensagenzinha:
            System.out.println("nao é compativel");
            //"Evolução não compatível! Tente com outra carta numa próxima rodada."
            return false;
        }
    }

    //ataque acontece (dano)
    public static void atacar(Pokemon atacante, Pokemon atacado) {
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
            atacante.gastaEnergia(atacante.getEnergiaGasta());
            atacado.setPs(atacado.getPs() - ((int)(atacante.getValorAtaque() * diferenca)));
        } else {
            //mensagem que nem a da vez, dizendo que nao tem energia pra atacar
            //dizer quanto de energia tem e quaanto precisa (usar os getters)
        }
    }

    //morre
    public static boolean morrer(CardDeck descarte, CardDeck ativo) {
        if (ativo.getCards().size()==1) {
            Card a = ativo.getBaralho().get(0);
            Pokemon p = (Pokemon)a;
            if (p.getPs() <= 0) {
                p.setEnergiaComeco();
                a = p;
                descarte.getBaralho().add(a);
                ativo.getBaralho().clear();   
                System.out.println("entrou no que tem tam 1, e apagou o ativo");
                return true;
            }
        } else if (ativo.getCards().size()==2) {
            for (Card c : ativo.getCards()) {
                if (c instanceof Pokemon) {
                    Pokemon p = (Pokemon)c;
                    if (!(p.getGeracaoAnterior() == null)) {
                        if (p.getPs() <= 0) {
                            for (Card a : ativo.getCards()) {
                                Pokemon b = (Pokemon)a;
                                b.setEnergiaComeco();
                                a = b;
                                descarte.getBaralho().add(b);
                                ativo.getBaralho().clear();  
                                System.out.println("entrou no que tem tam 2, e apagou o ativo"); 
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("deu ruim");
        }  
        System.out.println("entrou no tamanho dos ativos mas não morreu nenhum");
        return false;  
        
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

    public void gastaEnergia(int energia) {
        this.energia -= energia;
    }

    public void setEnergiaComeco() {
        this.energia = 10;
    }

    public void setEnergia(int energia) {
        this.energia += energia;
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
