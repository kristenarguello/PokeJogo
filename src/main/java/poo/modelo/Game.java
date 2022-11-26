package poo.modelo;

import java.util.LinkedList;
import java.util.List;

//import poo.modelo.GameEvent.Action;
//import poo.modelo.GameEvent.Target;

public class Game {
	private static Game game = new Game();
	private CardDeck deckJ1, deckJ2;
	private CardDeck maoJ1, maoJ2;
	private CardDeck ativoJ1, ativoJ2;
	private CardDeck descarteJ1, descarteJ2;
	private CardDeck bancoJ1, bancoJ2;
	private CardDeck verso;

	private int player;
	private List<GameListener> observers;
	private int ganhador;
	
	public static Game getInstance() {
		return game;
	}

	private Game() {
		deckJ1 = new CardDeck(30);
		deckJ2 = new CardDeck(30);
		maoJ1 = new CardDeck(5,deckJ1);
		maoJ2 = new CardDeck(5,deckJ2);
		bancoJ1 = new CardDeck(3,maoJ1);
		bancoJ2 = new CardDeck(3,maoJ2);
		ativoJ1 = new CardDeck(2,bancoJ1);
		ativoJ2 = new CardDeck(2,bancoJ2);
		descarteJ1 = new CardDeck(25,deckJ1);
		descarteJ2 = new CardDeck(25,deckJ2);
		verso = new CardDeck();
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
		return deckJ2;
	}

	public void play(CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		int n;
		if (player>6)
			n = player - 7;
		else 
			n = player;

		CardDeck deckAux, maoAux, bancoAux, ativoAux, descarteAux;
		if (player<6) {
			deckAux = deckJ1;
			maoAux = maoJ1;
			bancoAux = bancoJ1;
			ativoAux = ativoJ1;
			descarteAux = descarteJ1;
		} else {
			deckAux = deckJ2;
			maoAux = maoJ2;
			bancoAux = bancoJ2;
			ativoAux = ativoJ2;
			descarteAux = descarteJ2;
		}

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
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTBUY, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
			//mensagem p/ usuario: comprou uma carta!
		}

		//2 - jogador coloca algum pokemon basico para a reserva - mensagem, clicar nas cartas, depois botao para seguir PODE PASSAR
		if (n == 2){
			//pergunta se quer fazer 
			//testar se pode fazer
			if (deckAcionado.getTipo()==5) {
				Pokemon p;
				if (deckAcionado.getSelected() instanceof Pokemon) {
					p = (Pokemon)deckAcionado.getSelected();
					if (p.getGeracaoAnterior()==null){
						if (bancoAux.getCards().size() < 3) {
							bancoAux.getBaralho().add(deckAcionado.getSelected());
							deckAcionado.getBaralho().remove(deckAcionado.getSelected());
							nextPlayer();
						} else {
							//aviso banco lotado
							//evento
						}
					}	
				}
				else{
					//aviso carta inválida para essa ação
				}
			} else {
				//evento baralho errado
			}
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		} 

		//3 - jogador escolhe uma evolucao (carta) pra aplicar - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 3){	
			boolean continua = false;
			boolean seTem = false;
			for (Card c : maoAux.getCards()){
				if (((Pokemon)c).getGeracaoAnterior().equals(ativoAux.getCards().get(0).getNome())) {
					seTem = true;
					break;
				}
			}
			if (seTem) {
				//quer fazer isso?
					if (deckAcionado.getTipo()==5) {
						if (ativoAux.getCards().size() == 1) {
							Pokemon p;
							if (deckAcionado.getSelected() instanceof Pokemon) {
								p = (Pokemon)deckAcionado.getSelected();
								if (p.getGeracaoAnterior()!=null){
									while (!continua) {
									continua = ((Pokemon)deckAcionado.getSelected()).evoluir((Pokemon)deckAcionado.getSelected(),ativoAux.getBaralho());
									//aviso DENTRO do evoluir q a carta n é correspondente
								}
								nextPlayer();
							}
						} else {

						}
					}
				}
			}
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		//4 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		if (n == 4){
			boolean acontece = false;
			for (Card c : maoAux.getCards())
				if (c instanceof Energia) 
					acontece = true;	
			if (acontece) {
				//aparece o botao pra fazer se quiser{faz abaixo}
					Energia.setEnergia((Pokemon)ativoAux.getCards(),descarteAux.getBaralho(),maoAux.getBaralho());
					nextPlayer();
			}
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		//5 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE PASSAR
		//acontece a ação da carta treinador acontece, mensagem depois dizendo o que aconteceu (sem botão, passa direto)
		if (n == 5){
			boolean acontece = false;
			for (Card c : maoAux.getCards())
				if (c instanceof Treinador) 
					acontece = true;
			
			if (acontece) {
				//botao pra escolher se quer fazer
				if (deckAcionado.getTipo() == 5) {
					if (deckAcionado.getSelected() instanceof Treinador) {
						((Treinador)deckAcionado.getSelected()).treinador((Pokemon)ativoAux.getPokAtivo(),descarteAux.getBaralho(),bancoAux.getBaralho(),ativoAux.getBaralho(),deckAcionado.getBaralho());
						nextPlayer();
					} else {
						//aviso de carta errada
					}
				}
			}
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		
		//6 - jogador aperta o botão de atacar - clicar no botão (se não for possível, aparece mensagem) OBRIGATÓRIO 
		if (n == 6) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTATTACK, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}	
			if (player<6) {
				deckJ1 = deckAux;
				maoJ1 = maoAux;
				bancoJ1 = bancoAux;
				ativoJ1 = ativoAux;
				descarteJ1 = descarteAux;
			} else {
				deckJ2 = deckAux; 
				maoJ2 = maoAux;
				bancoJ2 = bancoAux;
				ativoJ2 = ativoAux;
				descarteJ2 = descarteAux;
			}

		if (player>12) {
			if (player == 13) {
				Pokemon.morrer(descarteJ1.getBaralho(), ativoJ1.getBaralho());
				Pokemon.morrer(descarteJ2.getBaralho(),ativoJ2.getBaralho());
			}
				
			if (ativoJ1.getCards().size()==0){
				boolean perde = true;
				for (Card c : bancoJ1.getCards()){
					if (c instanceof Pokemon){
						Pokemon p = (Pokemon) c;
						ativoJ1.getBaralho().add(p); 
						bancoJ1.getBaralho().remove(p);
						perde = false;
						break;
					}
				}
				if(perde){
					ganhador = 2;
				}
			}	
				
			if (ativoJ2.getCards().size()==0) {
				boolean perde = true;
				for (Card c : bancoJ2.getCards()) {
					if (c instanceof Pokemon) {
						Pokemon p = (Pokemon)c;
						ativoJ2.getBaralho().add(p);
						bancoJ2.getBaralho().remove(p);
						perde = false;
						break;
					}
					}
					if(perde){
						ganhador = 1;
					}
			}
			
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		if (player == 13) {
			if (ganhador == 0) {
				//mensagem de continuar o jogo, prox rodada
			}
		}
		
		if (ganhador == 1) {
			//aviso e acabou o jogo 
		} else if (ganhador==2) {
			//mensagem de ganhar
		} 

		
	}
	
	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (player != 13) {
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

	//acionado pelo botao de comprar
	public void comprarCarta(int qual) {
		GameEvent gameEvent = null;
		if (player != 1) {
			return;
		}
		if (ganhador != 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		if (qual == 1) {
			maoJ1.addCard(deckJ1.getCards().get(0));
			deckJ1.getBaralho().remove(0);
		}
		else if (qual == 2) {
			maoJ2.addCard(deckJ2.getCards().get(0));
			deckJ2.getBaralho().remove(0);
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		nextPlayer();

	}

	//acionado pelo botao de atacar
	public void atacar(int jogador) {
		CardDeck aux;
		CardDeck principal;
		if (jogador == 1){ 
			principal = ativoJ1;
			aux = ativoJ2;
		} else {
			principal = ativoJ2;
			aux = ativoJ1;
		}
		Pokemon.atacar((Pokemon)principal.getPokAtivo(),(Pokemon)aux.getPokAtivo());
	}
	
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public CardDeck getVerso() {
		return verso;
	}

	public CardDeck getMaoJ1() {
		return maoJ1;
	}

	public CardDeck getMaoJ2() {
		return maoJ2;
	}

	public CardDeck getAtivoJ1() {
		return ativoJ1;
	}

	public CardDeck getAtivoJ2() {
		return ativoJ2;
	}

	public CardDeck getDescarteJ1() {
		return descarteJ1;
	}

	public CardDeck getDescarteJ2() {
		return descarteJ2;
	}

	public CardDeck getBancoJ1() {
		return bancoJ1;
	}

	public CardDeck getBancoJ2() {
		return bancoJ2;
	}
}