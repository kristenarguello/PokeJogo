package poo.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

//import poo.modelo.GameEvent.Action;
//import poo.modelo.GameEvent.Target;

public class CardDeck {
	private List<Card> cartas;
	private List<GameListener> observers;
	private Card selected;
	private int tipo;
	
	public int getTipo() {
		return tipo;
	}

	public Card getPokAtivo() {
		int tam = cartas.size();
		return cartas.get(tam+1);
	}
	
	public Card getSelected() {
		return selected;
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public List<Card> getBaralho() {
		return cartas;
	}

	public static int energia = 0;
	public static int pokemon = 0;
	public static int treinador = 0;

	public CardDeck() {
		cartas = new ArrayList<>(1);
		Card c = new Card("imgVerso","imgVerso","verso");
		c.setFaceUp();
		cartas.add(c);
		observers = new LinkedList<>();

	}

	public CardDeck(int nrCartas, CardDeck cd) {
		cartas = new ArrayList<>();
		tipo = nrCartas;
		if (nrCartas == 2) {
			Card c = cd.getBaralho().get(0);
			cd.getBaralho().remove(0);//botar o banco como cd
			cartas.add(c);
		} else if (nrCartas == 5) {
			for (int i=0;i<5;i++) {
				Card c = cd.getBaralho().get(i);//botar o deck do jogador como cd
				cartas.add(c);
			}
			for(int i=0;i<5;i++)
				cd.getBaralho().remove(i);
		} else if (nrCartas == 3) {//cd = mão
			int cont = 0;
			int pos = 0;
			int quantidade = 0;
			for (Card c : cd.getBaralho()) {
				if (c instanceof Pokemon) {
					Pokemon p = (Pokemon)c;
					if (p.getGeracaoAnterior() == null) {
						if (quantidade<2) {
							cartas.add(p);
							quantidade++;
						}	
					}
				}
				cont++;
			}
			cont =0;
			pos = 0;
			while(cont<5) {
				if (cd.getBaralho().get(pos) instanceof Pokemon) {
					Pokemon p = (Pokemon)cd.getBaralho().get(pos);
					if (p.getGeracaoAnterior() == null) {
						cd.getBaralho().remove(pos);
					} 
				} else 
					pos++;
				cont++;
			}
		} 
		else if(nrCartas == 25){
		}
		observers = new LinkedList<>();
	} 

	public CardDeck(int nrCartas) {
		cartas = new ArrayList<>();

		int totalCartas = 0;
		ArrayList<Card> todas = new ArrayList<>();
		todas.add(new Pokemon(20,70,Tipos.AGUA, "Squirtle",null,"squirtle","imgSquirtle"));
		todas.add(new Pokemon(20,70,Tipos.PLANTA,"Bulbasaur",null,"bulbasaur","imgBulbasaur"));
		todas.add(new Pokemon(20,70,Tipos.FOGO,"Charmander",null,"charmander","imgCharmander"));
		todas.add(new Pokemon(20,70,Tipos.NORMAL,"Eevee",null, "eevee","imgEevee"));
		todas.add(new Pokemon(40,110,Tipos.NORMAL,"Snorlax",null,"snorlax","imgSnorlax"));
		todas.add(new Pokemon(40,110,Tipos.NORMAL,"Chansey",null,"chansey","imgChansey"));
		todas.add(new Pokemon(50,110,Tipos.AGUA,"Alomomola",null,"alomomola","imgAlomomola"));
		todas.add(new Pokemon(50, 110, Tipos.PLANTA,"Maractus",null,"maractus","imgMaractus"));
		todas.add(new Pokemon(50,100,Tipos.FOGO,"Torkoal",null,"torkoal","imgTorkoal"));

		todas.add(new Treinador("pocao","imgPocao","Poção",30));
		todas.add(new Treinador("super_pocao","imgSuper","Super Poção",60));
		todas.add(new Treinador("substituicao","imgSubst","Substituição",Acao.POKEBOLA));
		todas.add(new Treinador("reviver","imgRev","Reviver",Acao.POKEBOLA));
		todas.add(new Treinador("recupera","imgRecupera","Recuperação de Energia",Acao.ENERGIA));
		todas.add(new Treinador("substitu_energ","imgSubstitu_energ","Substituição de Energia",Acao.ENERGIA));


		ArrayList<Card> evolucaoEve = new ArrayList<>();
		evolucaoEve.add(new Pokemon(60,120,Tipos.AGUA,"Vaporeon","Eevee","vaporeon","imgVaporeon"));
		evolucaoEve.add(new Pokemon(60,110,Tipos.FOGO,"Flareon","Eevee","flareon","imgFlareon"));
		evolucaoEve.add(new Pokemon(60,110,Tipos.PLANTA,"Leafeon","Eevee","leafeon","imgLeafeon"));
		Random r = new Random();
		
		for(int i=0;i<15;i++) {
			cartas.add(new Energia(10, "Energia", "energia", "imgEnergia"));
		}
		totalCartas += 15;
		energia += 15;
			
		for(int i=0;i<4;i++) {
			int selecao = r.nextInt(9);
			Card c = todas.get(selecao);
			cartas.add(c);
			pokemon++;
			if (c.getNome().equals("Squirtle")) {
				cartas.add(new Pokemon(50,90,Tipos.AGUA,"Wartortle","Squirtle","wartortle","imgWartortle"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Bulbasaur")) {
				cartas.add(new Pokemon(50,100,Tipos.PLANTA,"Ivysaur","Bulbasaur","ivysaur","imgIvysaur"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Charmander")) {
				cartas.add(new Pokemon(50, 90, Tipos.FOGO,"Charmeleon","Charmander","charmeleon","imgCharmeleon"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Eevee")) {
				int qual = r.nextInt(3);
				cartas.add(evolucaoEve.get(qual));
				totalCartas++;
				pokemon++;
			}
		}
		totalCartas += 4;

		while (totalCartas<nrCartas) {
			int qual = r.nextInt(6) + 9;
			Card c = todas.get(qual);
			cartas.add(c);
			totalCartas++;
			treinador++;
		}
		Collections.shuffle(cartas);

		List<Card> aux = new ArrayList<Card>();
		for (int i=0;i<cartas.size();i++) {
			if (cartas.get(i) instanceof Pokemon) {
				Card c = cartas.get(i);
            	Pokemon p = (Pokemon)c;
				if (p.getGeracaoAnterior()==null) {
					cartas.remove(c);
					Stack<Card> pilha = new Stack<>();
					pilha.addAll(cartas);
					pilha.add(c);
					aux = pilha;
					break;
				}
			}
		}
		cartas.clear();
        for (int i = aux.size() - 1;i>=0;i--) {
            cartas.add(aux.get(i));
        }
		observers = new LinkedList<>();
	}

	public int getNumberOfCards() {
		return cartas.size();
	}

	public void removeSel() {
		System.out.println("TESTE AQUI!!! CARDDECK -----------------------------");
		if (selected == null) {
			return;
		}
		cartas.remove(selected);
		selected = null;
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void addCard(Card card) {
		System.out.println("add: "+ card);
		cartas.add(card);
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SHOWTABLE, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	// public void addAtivo(Card card) {
	// 	ativo.add(card);
	// }
	

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public void printar(List<Card> lista) {
		for (Card c : lista) {
			System.out.println(c);
		}
		System.out.println();
		System.out.println("energia: " + energia);
		System.out.println("pokemon: " + pokemon);
		System.out.println("treinador: " + treinador);
	}

	

	public void setSelectedCard(Card card) {
		selected = card;
	}
}
