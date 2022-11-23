package poo.modelo;

import java.util.LinkedList;
import java.util.List;

//import poo.modelo.GameEvent.Action;
//import poo.modelo.GameEvent.Target;

public class Game {
	private static Game game = new Game();
	private CardDeck deckJ1, deckJ2;
	private int player;
	private List<GameListener> observers;
	private int ganhador;
	
	public static Game getInstance() {
		return game;
	}

	private Game() {
		deckJ1 = new CardDeck(30); //aparecer na iiinterface o List Card ao inves do cardDeck
		deckJ2 = new CardDeck(30);
		player = 1;
		observers = new LinkedList<>();
		ganhador = 0;
	}

	private void nextPlayer() {
		player++;
		if (player == 28) {
			player = 1;
		}
	}

	public CardDeck getDeckJ1() {
		return deckJ1;
	}

	public CardDeck getDeckJ2() {
		return deckJ2;//dentro tem os outros baralhos
	}

	public void play(CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		if (player == 3) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (deckAcionado == deckJ1) {
			if (player != 1) {	
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta
				deckJ1.getSelected().flip();
				// Proximo jogador
				nextPlayer();
			}
		} else if (deckAcionado == deckJ2) {
			if (player != 2) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta
				deckJ2.getSelected().flip();
				// Verifica quem ganhou a rodada
				// if (deckJ1.getSelectedCard().getValue() > deckJ2.getSelectedCard().getValue()) {
				// 	ptsJ1++;
				// } else if (deckJ1.getSelectedCard().getValue() < deckJ2.getSelectedCard().getValue()) {
				// 	ptsJ2++;
				// }
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// Próximo jogador
				nextPlayer();
			}
		}
	}

	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (player != 28) {
			return;
		}
		if (ganhador != 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		
		deckJ1.removeSel();
		deckJ2.removeSel();

		nextPlayer();
	}
	
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}
