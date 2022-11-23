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
	private List<Card> deck;
	private List<Card> selected;
	private List<GameListener> observers;

	private List<Card> mao;
	private List<Card> banco;
	private List<Card> descarte;
	
	public Card getSelected() {
		int tam = selected.size();
		return selected.get(tam+1);
	}

	public List<Card> getAtivo() {
		return selected;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public List<Card> getMao() {
		return mao;
	}

	public List<Card> getBanco() {
		return banco;
	}

	public List<Card> getDescarte() {
		return descarte;
	}

	public static int energia = 0;
	public static int pokemon = 0;
	public static int treinador = 0;

	public CardDeck(int nrCartas) {
		deck = new ArrayList<>();
		mao = new ArrayList<>();
		banco = new ArrayList<>();
		descarte = new ArrayList<>();


		int totalCartas = 0;
		ArrayList<Card> todas = new ArrayList<>();
		todas.add(new Pokemon(20,70,Tipos.AGUA, "Squirtle",null,"squirtle","imgSquirtle"));
		todas.add(new Pokemon(20,70,Tipos.PLANTA,"Bulbasaur",null,"bulbasaur","imbBulbasaur"));
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
			deck.add(new Energia(10, "Energia", "energia", "imgEnergia"));
		}
		totalCartas += 15;
		energia += 15;
			
		for(int i=0;i<4;i++) {
			int selecao = r.nextInt(9);
			Card c = todas.get(selecao);
			deck.add(c);
			pokemon++;
			if (c.getNome().equals("Squirtle")) {
				deck.add(new Pokemon(50,90,Tipos.AGUA,"Wartortle","Squirtle","wartortle","imgWartortle"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Bulbasaur")) {
				deck.add(new Pokemon(50,100,Tipos.PLANTA,"Ivysaur","Bulbasaur","ivysaur","imgIvysaur"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Charmander")) {
				deck.add(new Pokemon(50, 90, Tipos.FOGO,"Charmeleon","Charmander","charmeleon","imgCharmeleon"));
				totalCartas++;
				pokemon++;
			} else if (c.getNome().equals("Eevee")) {
				int qual = r.nextInt(3);
				deck.add(evolucaoEve.get(qual));
				totalCartas++;
				pokemon++;
			}
		}
		totalCartas += 4;

		while (totalCartas<nrCartas) {
			int qual = r.nextInt(6) + 9;
			Card c = todas.get(qual);
			deck.add(c);
			totalCartas++;
			treinador++;
		}
		Collections.shuffle(deck);

		List<Card> aux = new ArrayList<Card>();
		for (int i=0;i<deck.size();i++) {
			if (deck.get(i) instanceof Pokemon) {
				Card c = deck.get(i);
            	Pokemon p = (Pokemon)c;
				if (p.getGeracaoAnterior()==null) {
					deck.remove(c);
					Stack<Card> pilha = new Stack<>();
					pilha.addAll(deck);
					pilha.add(c);
					aux = pilha;
					break;
				}
			}
		}
		deck.clear();
        for (int i = aux.size() - 1;i>=0;i--) {
            deck.add(aux.get(i));
        }
		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(deck);
	}

	public int getNumberOfCards() {
		return deck.size();
	}

	public void removeSel() {
		if (selected.size()==0) {
			return;
		}
		if (Pokemon.morrer(descarte,selected)) {
			GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
	}

	public void addCard(Card card) {
		System.out.println("add: "+ card);
		deck.add(card);
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SHOWTABLE, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void addSelected(Card card) {
		selected.add(card);
	}

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

	public void inicio() {
		for (int i=0;i<5;i++) {
			Card c = deck.get(i);
			mao.add(c);
		}
		for(int i=0;i<5;i++)
			deck.remove(i);

		printar(mao);
		int cont = 0;
		int pos = 0;
		int quantidade = 0;
		for (Card c : mao) {
			if (c instanceof Pokemon) {
				Pokemon p = (Pokemon)c;
				if (p.getGeracaoAnterior() == null) {
					if (quantidade<2) {
						banco.add(p);
						quantidade++;
					}	
				}
			}
			cont++;
		}
		cont =0;
		pos = 0;
		while(cont<5) {
			if (mao.get(pos) instanceof Pokemon) {
				Pokemon p = (Pokemon)mao.get(pos++);
				if (p.getGeracaoAnterior() == null) {
					mao.remove(p);
				}
			}
			cont++;
		}
		
	}


}
