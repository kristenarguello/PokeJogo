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

	public void playzinho(CardDeck deckAcionado, GameEvent gameEvent) {
		int n;
		if (player>10)
			n = player - 10;
		else 
			n = player;

		if (deckAcionado==deckJ1 && player>10){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		if(deckAcionado==deckJ2 && player<11){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		//1 - jogador compra uma carta - vai direto OBRIGATORIO
		if (n == 1){
			List<Card> aux = deckAcionado.getDeck();
			deckAcionado.getMao().add(aux.get(0));
			deckAcionado.getDeck().remove(0);
			//mensagem p/ usuario: comprou uma carta!
		}

		//2 - jogador coloca algum pokemon basico para a reserva - mensagem, clicar nas cartas, depois botao para seguir PODE PASSAR
		if (n == 2){
			boolean acontece = false;
			Pokemon p = null;
			List<Card> aux = deckAcionado.getMao();
			for (Card c : aux) {
				if (c instanceof Pokemon) {
					p = (Pokemon)c;
					if (p.getGeracaoAnterior()==null){
						acontece = true;
						break;
					}	
				}
			}
			if (acontece) {
				if (deckAcionado.getBanco().size() < 3) {
					//botao escolher se quer botar reserva
					deckAcionado.getBanco().add(p);
					deckAcionado.getMao().remove(p);
				}
			}
		}

		//3 - jogador escolhe uma evolucao (carta) pra aplicar - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 3){	
			boolean acontece = false;	
			if (deckAcionado.getAtivo().size() == 0) {
				acontece = true;
			}

			
			Pokemon p = null;
			List<Card> aux = deckAcionado.getMao();
			for (Card c : aux) {
				if (c instanceof Pokemon) {
					p = (Pokemon)c;
					if (p.getGeracaoAnterior()!=null){
						Pokemon carta = (Pokemon)deckAcionado.getAtivo().get(0);
						carta.evoluir(p,deckAcionado.getAtivo());
						break;
					}
				}
			}
			return;
		}

		//4 - jogador escolhe um pokemon basico do banco ou ativo para evoluir (caso 3 seja true) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 4){
			return;
		}

		//5 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 5){
			
		}

		//6 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 6){
			
		}

		//7 - acontece a ação da carta treinador (caso 6 seja true) - acontece, mensagem depois dizendo o que aconteceu (sem botão, passa direto)
		if (n == 7){
			
		}

		//8 - jogador escolhe se quer trocar o pokemon ativo - mensagem, botao para decidir se quer ou não OBRIGATÓRIO
		if (n == 8){
			
		}
		
		//9 - jogador escolhe qual pokemon do banco vai substituir o ativo (caso 8 seja true) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 9){
			
		}
		
		//10 - jogador aperta o botão de atacar - clicar no botão (se não for possível, aparece mensagem) OBRIGATÓRIO 
		if (n == 10) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTATTACK, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			
			return;
		}
		//acho q a partir daqui 
		if (deckAcionado == deckJ1) {
			if (n != 1) {	
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
			if (n != 2) {
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
	

	public void outroPlayzao() {
		GameEvent gameEvent = null;

		gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
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
