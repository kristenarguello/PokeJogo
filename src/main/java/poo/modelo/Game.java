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
		maoJ1 = new CardDeck(5, deckJ1);
		maoJ2 = new CardDeck(5, deckJ2);
		bancoJ1 = new CardDeck(3, maoJ1);
		bancoJ2 = new CardDeck(3, maoJ2);
		ativoJ1 = new CardDeck(2, bancoJ1);
		ativoJ2 = new CardDeck(2, bancoJ2);
		descarteJ1 = new CardDeck(25, deckJ1);
		descarteJ2 = new CardDeck(25, deckJ2);
		verso = new CardDeck();
		player = 1;
		observers = new LinkedList<>();
		ganhador = 0;
	}

	private void nextPlayer() {
		player++;
		if (player == 14) {
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

		// 1 - jogador compra uma carta - vai direto OBRIGATORIO
		if (player == 1 || player == 7) {
			System.out.println("player>>>> " + player);
			System.out.println("Ta na hora de ir as compras!");
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTBUY, "");
			System.out.println("clicou em outro lugar, nao foi em comprar");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;// isso ta....?
			// mensagem p/ usuario: comprou uma carta!
		}

		if (player == 6 || player == 12) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTATTACK, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		// 2 - jogador coloca algum pokemon basico para a reserva - mensagem, clicar nas
		// cartas, depois botao para seguir PODE PASSAR
		if (deckAcionado == verso || deckAcionado == maoJ1) {
			if (player == 2) {
				System.out.println("n == 2 --------------");
				// testar se pode fazer
				boolean continua = false;
				// for (Card c : maoJ1.getCards()){
				// if (c instanceof Pokemon) {
				// if (((Pokemon)c).getGeracaoAnterior()==null) {
				// continua = true;
				// break;
				// }
				// }
				// }
				if (deckAcionado.getSelected() instanceof Pokemon && deckAcionado.getTipo() == 5) {
					if (((Pokemon) deckAcionado.getSelected()).getGeracaoAnterior() == null)
						continua = true;
				}

				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.VAI, "");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				if (continua) {
					System.out.println("tem como colocar reserva a mais");
					// pergunta se quer fazer
					// gameEvent = new GameEvent(this, GameEvent.Target.GWIN,
					// GameEvent.Action.CONTINUE, "colocar um pokemon basico da mão para a reserva.
					// Clique em \"PULAR\" caso não queira");
					// for (var observer : observers) {
					// observer.notify(gameEvent);
					// } botar nas regras

					Pokemon p;
					if (deckAcionado.getSelected() instanceof Pokemon) {
						p = (Pokemon) deckAcionado.getSelected();
						if (p.getGeracaoAnterior() == null) {
							if (bancoJ1.getCards().size() < 3) {// isso nao pode na 1 verificacao?
								// bancoJ1.getBaralho().add(deckAcionado.getSelected());
								bancoJ1.addCard(deckAcionado.getSelected());
								deckAcionado.removeSel();
								System.out.println("adicionou no banco e tirou do deck acionado");
							} else {
								// aviso banco lotado
								// evento
								System.out.println("banco lotado");
								return;
							}
						}
					}

				} else {
					System.out.println("baralho ou carta errado");
					// evento baralho errado

					System.out.println("player>>> " + player);
				}
				nextPlayer();
				System.out.println("proximo jogador, terminou a primeira parte");
				System.out.println("player>>>" + player);
				return;
				// --------------------------------------------------------------------------------------------------------------
				// 3 - jogador escolhe uma evolucao (carta) pra aplicar - mensagem, clicar na
				// carta, botao para seguir PODE PASSAR
			} else if (player == 3) {
				System.out.println("entrou no 3");
				boolean continua = false;
				boolean seTem = false;
				for (Card c : maoJ1.getCards()) {
					if (c instanceof Pokemon) {
						if (((Pokemon) c).getGeracaoAnterior().equals(ativoJ1.getCards().get(0).getNome())) {
							seTem = true;
							break;
						}
					}
				}
				if (seTem) {
					if (deckAcionado.getTipo() == 5) {
						if (ativoJ1.getCards().size() == 1) {// acho q da pra tirar isso daqui e colocar na conferencia
																// se
																// pode acontecer - nao tem como evoluir ja evoluido
							Pokemon p;
							if (deckAcionado.getSelected() instanceof Pokemon) {
								p = (Pokemon) deckAcionado.getSelected();
								if (p.getGeracaoAnterior() != null) {
									while (!continua) {
										continua = ((Pokemon) deckAcionado.getSelected())
												.evoluir((Pokemon) deckAcionado.getSelected(), ativoJ1.getBaralho());
										// maoJ1.removeSel();
										// aviso DENTRO do evoluir q a carta n é correspondente
									}
								}
							} else {
								// aviso seu pokemon ja esta evoluido
								return;
							}
						} else {
							// mensagem de deck errado
							return;
						}
					}
				}
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				System.out.println("tres parou aqui");
				// --------------------------------------------------------------------------------------------------------------
				// 4 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver
				// a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para
				// seguir PODE PASSAR
			} else if (player == 4) {
				System.out.println("entrou no quatro");
				if (deckAcionado.getSelected() instanceof Energia) {
					System.out.println("entrou no metodo");
					System.out.println("eneriga: " + ((Pokemon) ativoJ1.getPokAtivo()).getEnergia());
					Energia.setEnergia((Pokemon) ativoJ1.getPokAtivo(), descarteJ1.getBaralho(), maoJ1.getBaralho());
					System.out.println("energia: " + ((Pokemon) ativoJ1.getPokAtivo()).getEnergia());
					maoJ1.removeSel();
				}
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// --------------------------------------------------------------------------------------------------------------
				// 5 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo
				// caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE
				// PASSAR
				// acontece a ação da carta treinador acontece, mensagem depois dizendo o que
				// aconteceu (sem botão, passa direto)
			} else if (player == 5) {
				boolean acontece = false;
				boolean vidaMax = true;
				for (Card c : maoJ1.getBaralho())
					if (c instanceof Treinador)
						acontece = true;

				if (deckAcionado.getSelected() instanceof Treinador) {
					if (((Treinador) deckAcionado.getSelected()).getTipo() == Acao.CURA) {
						if (((Pokemon) ativoJ1.getPokAtivo()).getPs() == ((Pokemon) ativoJ1.getPokAtivo())
								.getVidaMaxima())
							vidaMax = false;
					}
				}

				if (acontece && vidaMax) {
					System.out.println("player>>>> " + player);
					if (deckAcionado.getTipo() == 5) {
						if (deckAcionado.getSelected() instanceof Treinador) {
							((Treinador) deckAcionado.getSelected()).treinador((Pokemon) ativoJ1.getPokAtivo(),
									descarteJ1.getBaralho(), bancoJ1.getBaralho(), ativoJ1.getBaralho(),
									deckAcionado.getBaralho(), deckAcionado, bancoJ1, ativoJ1);
							// aviso do que foi feito
							gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.TREINADORFEZ,
									((Treinador) deckAcionado.getSelected()).getQueFaz());
							for (var observer : observers) {
								observer.notify(gameEvent);
							}
							gameEvent = null;
							maoJ1.removeSel();
						} else {
							// aviso de carta errada
							return;
						}
					} else {
						// warning de carta errada, seleciona o baralho certo seu imbecil
						System.out.println("baralho escolhido errado");
						// avisos de warning diferentes
						return;
					}
				} else {
					System.out.println("nao tem treinador ou ta com a vida maxina e a carta é de cura");
				}
				nextPlayer();
				System.out.println("player>>>" + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// --------------------------------------------------------------------------------------------------------------
			} else if (player != 2 & player != 3 && player != 4 && player != 5) {
				System.out.println("jogador errado");
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
			}
			// =======================================================================================================================
		} else if (deckAcionado == verso || deckAcionado == maoJ2) {
			if (player == 8) {
				System.out.println("n == 8 --------------");
				// testar se pode fazer
				boolean continua = false;
				// for (Card c : maoJ1.getCards()){
				// if (c instanceof Pokemon) {
				// if (((Pokemon)c).getGeracaoAnterior()==null) {
				// continua = true;
				// break;
				// }
				// }
				// }
				if (deckAcionado.getSelected() instanceof Pokemon && deckAcionado.getTipo() == 5) {
					if (((Pokemon) deckAcionado.getSelected()).getGeracaoAnterior() == null)
						continua = true;
				}

				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.VAI, "");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				if (continua) {
					System.out.println("tem como colocar reserva a mais");
					// pergunta se quer fazer
					// gameEvent = new GameEvent(this, GameEvent.Target.GWIN,
					// GameEvent.Action.CONTINUE, "colocar um pokemon basico da mão para a reserva.
					// Clique em \"PULAR\" caso não queira");
					// for (var observer : observers) {
					// observer.notify(gameEvent);
					// } botar nas regras

					Pokemon p;
					if (deckAcionado.getSelected() instanceof Pokemon) {
						p = (Pokemon) deckAcionado.getSelected();
						if (p.getGeracaoAnterior() == null) {
							if (bancoJ2.getCards().size() < 3) {// isso nao pode na 1 verificacao?
								// bancoJ2.getBaralho().add(deckAcionado.getSelected());
								bancoJ2.addCard(deckAcionado.getSelected());
								deckAcionado.removeSel();
								System.out.println("adicionou no banco e tirou do deck acionado");
							} else {
								// aviso banco lotado
								// evento
								System.out.println("banco lotado");
								return;
							}
						}
					}

				} else {
					System.out.println("baralho ou carta errado");
					// evento baralho errado

					System.out.println("player>>> " + player);
				}
				nextPlayer();
				System.out.println("proximo jogador, terminou a primeira parte");
				System.out.println("player>>>" + player);
				return;
				// --------------------------------------------------------------------------------------------------------------
				// 9 - jogador escolhe uma evolucao (carta) pra aplicar - mensagem, clicar na
				// carta, botao para seguir PODE PASSAR
			} else if (player == 9) {
				System.out.println("entrou no 9");
				boolean continua = false;
				boolean seTem = false;
				for (Card c : maoJ1.getCards()) {
					if (c instanceof Pokemon) {
						if (((Pokemon) c).getGeracaoAnterior().equals(ativoJ2.getCards().get(0).getNome())) {
							seTem = true;
							break;
						}
					}
				}
				if (seTem) {
					if (deckAcionado.getTipo() == 5) {
						if (ativoJ2.getCards().size() == 1) {// acho q da pra tirar isso daqui e colocar na conferencia
																// se
																// pode acontecer - nao tem como evoluir ja evoluido
							Pokemon p;
							if (deckAcionado.getSelected() instanceof Pokemon) {
								p = (Pokemon) deckAcionado.getSelected();
								if (p.getGeracaoAnterior() != null) {
									while (!continua) {
										continua = ((Pokemon) deckAcionado.getSelected())
												.evoluir((Pokemon) deckAcionado.getSelected(), ativoJ2.getBaralho());
										// maoJ1.removeSel();
										// aviso DENTRO do evoluir q a carta n é correspondente
									}
								}
							} else {
								// aviso seu pokemon ja esta evoluido
								return;
							}
						} else {
							// mensagem de deck errado
							return;
						}
					}
				}
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				System.out.println("tres parou aqui");
				// --------------------------------------------------------------------------------------------------------------
				// 10 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver
				// a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para
				// seguir PODE PASSAR
			} else if (player == 10) {
				System.out.println("entrou no quatro");
				if (deckAcionado.getSelected() instanceof Energia) {
					System.out.println("entrou no metodo");
					System.out.println("eneriga: " + ((Pokemon) ativoJ2.getPokAtivo()).getEnergia());
					Energia.setEnergia((Pokemon) ativoJ2.getPokAtivo(), descarteJ2.getBaralho(), maoJ2.getBaralho());
					System.out.println("energia: " + ((Pokemon) ativoJ2.getPokAtivo()).getEnergia());
					maoJ2.removeSel();
				}
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// --------------------------------------------------------------------------------------------------------------
				// 11 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo
				// caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE
				// PASSAR
				// acontece a ação da carta treinador acontece, mensagem depois dizendo o que
				// aconteceu (sem botão, passa direto)
			} else if (player == 11) {
				boolean acontece = false;
				boolean vidaMax = true;
				for (Card c : maoJ2.getCards())
					if (c instanceof Treinador)
						acontece = true;
				if (deckAcionado.getSelected() instanceof Treinador) {
					if (((Treinador) deckAcionado.getSelected()).getTipo() == Acao.CURA) {
						if (((Pokemon) ativoJ1.getPokAtivo()).getPs() == ((Pokemon) ativoJ1.getPokAtivo())
								.getVidaMaxima())
							vidaMax = false;
					}
				}

				if (acontece && vidaMax) {
					if (deckAcionado.getTipo() == 5) {
						if (deckAcionado.getSelected() instanceof Treinador) {
							((Treinador) deckAcionado.getSelected()).treinador((Pokemon) ativoJ2.getPokAtivo(),
									descarteJ2.getBaralho(), bancoJ2.getBaralho(), ativoJ2.getBaralho(),
									deckAcionado.getBaralho(), deckAcionado, bancoJ2, ativoJ2);
							gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SHOWTABLE, "");
							for (var observer : observers) {
								observer.notify(gameEvent);
							}
							maoJ2.removeSel();
						} else {
							// aviso de carta errada
							return;
						}
					}
				}
				nextPlayer();
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
				// --------------------------------------------------------------------------------------------------------------
			} else if (player != 8 && player != 9 && player != 10 && player != 11) {
				System.out.println("jogador errado");
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
			}
		}

		// 3 - jogador escolhe uma evolucao (carta) pra aplicar - mensagem, clicar na
		// carta, botao para seguir PODE PASSAR

		// 4 - jogador escolhe a carta que aplica energia -- vai no ativo (se nao tiver
		// a carta, passar direto essa etapa) - mensagem, clicar na carta, botao para
		// seguir PODE PASSAR

		// 5 - jogador escolher uma carta treinador para se utilizar (aplicar no ativo
		// caso seja de pokemon) - mensagem, clicar na carta, botao para seguir PODE
		// PASSAR
		// acontece a ação da carta treinador acontece, mensagem depois dizendo o que
		// aconteceu (sem botão, passa direto)

		// 6 - jogador aperta o botão de atacar - clicar no botão (se não for possível,
		// aparece mensagem) OBRIGATÓRIO

	}

	public void confereMorrer() {
		GameEvent gameEvent = null;
		if (player == 13) {
			System.out.println("player>>> " + player);
			Pokemon.morrer(descarteJ1.getBaralho(), ativoJ1.getBaralho());
			Pokemon.morrer(descarteJ2.getBaralho(), ativoJ2.getBaralho());
			gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.SHOWTABLE, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		if (ativoJ1.getCards().size() == 0) {
			boolean perde = true;
			for (Card c : bancoJ1.getCards()) {
				if (c instanceof Pokemon) {
					Pokemon p = (Pokemon) c;
					ativoJ1.getBaralho().add(p);
					bancoJ1.getBaralho().remove(p);
					perde = false;
					break;
				}
			}
			if (perde) {
				ganhador = 2;
			}
		}

		if (ativoJ2.getCards().size() == 0) {
			boolean perde = true;
			for (Card c : bancoJ2.getCards()) {
				if (c instanceof Pokemon) {
					Pokemon p = (Pokemon) c;
					ativoJ2.getBaralho().add(p);
					bancoJ2.getBaralho().remove(p);
					perde = false;
					break;
				}
			}
			if (perde) {
				ganhador = 1;
			}
		}

		for (var observer : observers) {
			observer.notify(gameEvent);
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

	// acionado pelo botao de comprar
	public void comprarCartaJ1() {
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

		maoJ1.addCard(deckJ1.getCards().get(0));
		deckJ1.getBaralho().remove(0);
		nextPlayer();
		System.out.println("carta comprarda e proximo jogador");
		System.out.println("player>>>>> " + player);
	}

	public void comprarCartaJ2() {
		GameEvent gameEvent = null;
		if (player != 7) {
			return;
		}
		if (ganhador != 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}

		maoJ2.addCard(deckJ2.getCards().get(0));
		deckJ2.getBaralho().remove(0);
		nextPlayer();
		System.out.println("carta comprarda e proximo jogador");
		System.out.println("player>>>>> " + player);
	}

	// acionado pelo botao de atacar
	public void atacar(int jogador) {
		String text = null;
		GameEvent gameEvent = null;
		CardDeck aux;
		CardDeck principal;
		if (jogador == 1) {
			principal = ativoJ1;
			aux = ativoJ2;
		} else {
			principal = ativoJ2;
			aux = ativoJ1;
		}
		System.out.println("vida do outro antes>>>" + ((Pokemon) aux.getPokAtivo()).getPs());
		System.out.println("energia do atacante antes>>>" + ((Pokemon) principal.getPokAtivo()).getEnergia());
		Pokemon.atacar((Pokemon) principal.getPokAtivo(), (Pokemon) aux.getPokAtivo());
		System.out.println("ataque feito");
		System.out.println("vida do outro depois>>>" + ((Pokemon) aux.getPokAtivo()).getPs());
		System.out.println("energia do atacante depois>>>" + ((Pokemon) principal.getPokAtivo()).getEnergia());

		nextPlayer();
		confereMorrer();

		if (jogador == 1) {
			ativoJ1 = principal;
			ativoJ2 = aux;
		} else {
			ativoJ2 = principal;
			ativoJ1 = aux;
		}

		if (ganhador == 0) {
			if (jogador == 1)
				text = "2";
			else if (jogador == 2)
				text = "1";
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.KEEPPLAYING, text);
		} else if (ganhador == 1) {
			// aviso e acabou o jogo
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME,
					"O jogador 1 ganhou o jogo!!!");
		} else if (ganhador == 2) {
			// mensagem de ganhar
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME,
					"O jogador 2 ganhou o jogo!!!");
		}

		for (var observer : observers) {
			observer.notify(gameEvent);
		}
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