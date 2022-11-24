package poo.modelo;

import java.util.LinkedList;
import java.util.List;

//import poo.modelo.GameEvent.Action;
//import poo.modelo.GameEvent.Target;

public class Game {
	private static Game game = new Game();
	private CardDeck deckJ1, deckJ2;//tirar daqui, e fazer que nem get instance de game (singleton)
	private CardDeck mao1, mao2;
	private CardDeck ativo1, ativo2;
	private CardDeck descarte1, descarte2;
	private CardDeck banco1, banco2;

	public CardDeck getMao1() {
		return mao1;
	}

	public CardDeck getMao2() {
		return mao2;
	}

	public CardDeck getAtivo1() {
		return ativo1;
	}

	public CardDeck getAtivo2() {
		return ativo2;
	}

	public CardDeck getDescarte1() {
		return descarte1;
	}

	public CardDeck getDescarte2() {
		return descarte2;
	}

	public CardDeck getBanco1() {
		return banco1;
	}

	public CardDeck getBanco2() {
		return banco2;
	}

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
		if (player == 16) {
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
		if (player>6)
			n = player - 7;
		else 
			n = player;

		if (deckAcionado==deckJ1 && player>6){
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		if(deckAcionado==deckJ2 && player<7){
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

			if (acontece) {
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
			}
			return;
		}


		//4 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 4){
			boolean acontece = false;
			for (Card c : deckAcionado.getMao())
				if (c instanceof Energia) 
					acontece = true;
			
			if (acontece) {
				//aparece o botao pra fazer se quiser
				Energia.setEnergia((Pokemon)deckAcionado.getPokAtivo(),deckAcionado.getDescarte(),deckAcionado.getMao());
			}	
		}

		//5 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		// - acontece a ação da carta treinador (caso 6 seja true) - acontece, mensagem depois dizendo o que aconteceu (sem botão, passa direto)

		if (n == 5){
			boolean acontece = false;
			for (Card c : deckAcionado.getMao())
				if (c instanceof Treinador) 
					acontece = true;
			
			if (acontece) {
				//botao pra escolher se quer fazer
				//escolhe a carta
				Treinador selectedCard = null;//tem que ser a selecionada na tela, mudar isso
				selectedCard.treinador((Pokemon)deckAcionado.getPokAtivo(),deckAcionado.getDescarte(),deckAcionado.getBanco(),deckAcionado.getAtivo(),deckAcionado.getMao());
			}
		}

		//6 - jogador escolhe se quer trocar o pokemon ativo - mensagem, botao para decidir se quer ou não OBRIGATÓRIO
		if (n == 6){
			boolean acontece = false;
			for (Card c : deckAcionado.getBanco()){
				if (c instanceof Pokemon){
					Pokemon aux = (Pokemon)c;
					if (aux.getGeracaoAnterior()==null){
						//mensagem se quer trocar o pokemon ativo
						//se sim boolean vir true
						acontece = true;
					}
				}
			}
			
			if (acontece){
				Pokemon selectedCard = null;//tem que ser a selecionada na tela, mudar isso
				if (deckAcionado.getAtivo().size()==0){
					deckAcionado.getBanco().add(deckAcionado.getAtivo().get(0));
				}

				else {
					deckAcionado.getBanco().add(deckAcionado.getAtivo().get(1));
					deckAcionado.getDescarte().add(deckAcionado.getAtivo().get(0));
				}

				deckAcionado.getAtivo().clear();	
				deckAcionado.getAtivo().add(selectedCard);
				deckAcionado.getBanco().remove(selectedCard);
			}

		}
		
		//7 - jogador aperta o botão de atacar - clicar no botão (se não for possível, aparece mensagem) OBRIGATÓRIO 
		if (n == 7) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTATTACK, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			CardDeck aux = null;
			if (deckAcionado.equals(deckJ1))
				aux = deckJ2;
			else 
				aux = deckJ1;
			Pokemon.atacar((Pokemon)deckAcionado.getPokAtivo(),(Pokemon)aux.getPokAtivo());
			
			return;
		}
		/*
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
			}*/
		}
	

	public void outroPlayzao(CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		CardDeck aux;

		/*
		15 - checar se alguem morreu AUTOMATICO, pode ser os dois ao mesmo tempo
		16 - o que foi derrotado, é descartado AUTOMATICO
		AVISO PARA JOGADOR 1
		17 - confere se tem banco no jogador 1, se não perdeu OBRIGATORIO
		18 - jogador 1 escolhe qual do banco vai substituir - escolhe a carta, clica botao continuar CASO 23 TRUE
		AVISO DE TROCA DE JOGADOR
		19 - confere se tem banco no jogador 2, se não perdeu OBRIGATORIO, ARMAZENA 
		20 - jogador 2 escolhe qual do banco vai substituir - ecolhe a carta, clica botao continuar CASO 25 TRUE

		21 - aviso de quem ganhou (a partir de 23 e 24 caso false - caso nao tenham mais cartas) CASO FALSE 
		22 - botao pra proxima rodada, caso 27 false
		***boolean fim de jogo (while false)
		*/
		deckJ1.inicio();
		deckJ2.inicio();

		while (ganhador == 0) {
			playzinho(deckAcionado, gameEvent);
			nextPlayer();
			if (player>14) {
				if (deckAcionado.equals(deckJ1))
					aux = deckJ2;
				else 
					aux = deckJ1;
				if (player == 15) {
					Pokemon.morrer(deckAcionado.getDescarte(), deckAcionado.getAtivo());
					Pokemon.morrer(aux.getDescarte(),aux.getAtivo());
				}
				

				if (deckAcionado.getAtivo().size()==0){
					boolean perde = true;
					for (Card c : deckAcionado.getBanco()){
						if (c instanceof Pokemon){
							Pokemon p = (Pokemon) c;
							deckAcionado.getAtivo().add(p); //se o pokemon morre adiciona o primeiro possivel
							deckAcionado.getBanco().remove(p);
							perde = false;
							break;
						}
					}
					if(perde){
						ganhador = 2;
					}
				}	
				
				if (aux.getAtivo().size()==0) {
					boolean perde = true;
					for (Card c : aux.getBanco()) {
						if (c instanceof Pokemon) {
							Pokemon p = (Pokemon)c;
							aux.getAtivo().add(p);
							aux.getBanco().remove(p);
							perde = false;
							break;
						}
					}
					if(perde){
						ganhador = 1;
					}
				}
			}

			if (player == 15) {
				if (ganhador == 0) {
					//mensagem de continuar o jogo, prox rodada
				}
			}

		}
		if (ganhador == 1) {
			//aviso e acabou o jogo 
		} else if (ganhador==2) {
			//mensagem de ganhar
		} 

		gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}
	
	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (player != 22) {
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