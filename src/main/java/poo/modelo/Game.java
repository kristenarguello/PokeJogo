package poo.modelo;

import java.util.LinkedList;
import java.util.List;

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
		if (player == 15) {
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

		
		// jogador compra uma carta
		if (player == 1 || player == 8) {
			System.out.println("player>>>> " + player);
			System.out.println("Ta na hora de ir as compras!");
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTBUY, "");
			System.out.println("clicou em outro lugar, nao foi em comprar");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		if (player == 6 || player == 13) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTATTACK, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		if (player == 7 || player == 14) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCONFIRM, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		if (deckAcionado == verso) {
			nextPlayer();
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			System.out.println("player>>> " + player);
			return;
		}

		

		// jogador coloca algum pokemon basico para a reserva
		if (deckAcionado == maoJ1) {
			if (player == 2) {
				System.out.println("n == 2 --------------");
				// testar se pode fazer
				boolean continua = false;
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
					Pokemon p;
					if (deckAcionado.getSelected() instanceof Pokemon) {
						p = (Pokemon) deckAcionado.getSelected();
						if (p.getGeracaoAnterior() == null) {
							if (bancoJ1.getCards().size() < 3) {
								bancoJ1.addCard(deckAcionado.getSelected());
								deckAcionado.removeSel();
								System.out.println("adicionou no banco e tirou do deck acionado");
							} else {
								// aviso banco lotado
								gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.BANCOLOTADO,
										"");
								for (var observer : observers) {
									observer.notify(gameEvent);
								}
								gameEvent = null;
								System.out.println("banco lotado");
								return;
							}
						}
					}

				} else {
					System.out.println("baralho ou carta errado");
					System.out.println("player>>> " + player);
				}
				gameEvent = null;
				nextPlayer();
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				System.out.println("proximo jogador, terminou a primeira parte");
				System.out.println("player>>>" + player);
				return;
				// --------------------------------------------------------------------------------------------------------------
				// jogador escolhe uma evolucao (carta) pra aplicar evolucao
			} else if (player == 3) {
				boolean seTem = false;
				for (Card c : maoJ1.getCards()) {
					if (c instanceof Pokemon) {
						if (((Pokemon) c).getGeracaoAnterior() != null
								&& ((Pokemon) c).getGeracaoAnterior().equals(ativoJ1.getCards().get(0).getNome())) {
							seTem = true;
							break;
						}
					}
				}
				boolean compativel = true;
				if (seTem) {
					if (deckAcionado.getTipo() == 5) {
						if (ativoJ1.getCards().size() == 1) {
							Pokemon p;
							System.out.println("ativo tinha so uma carta e clicou certo");
							if (deckAcionado.getSelected() instanceof Pokemon) {
								p = (Pokemon) deckAcionado.getSelected();
								if (p.getGeracaoAnterior() != null) {
									compativel = ((Pokemon) deckAcionado.getSelected())
											.evoluir((Pokemon) deckAcionado.getSelected(), ativoJ1);
									deckAcionado.mostraCartas();
									ativoJ2.mostraCartas();
									maoJ1.removeSel();

									if (!compativel) {
										gameEvent = new GameEvent(this, GameEvent.Target.GWIN,
												GameEvent.Action.NAOCOMPATIVEL,
												"");
										for (var observer : observers) {
											observer.notify(gameEvent);
										}
									}
								}
							} else {
								System.out.println("carta errada");
							}
						} else {
							System.out.println("pokemon ja evoluido");
						}
					}
				} else {
					System.out.println("carta nao compativel");
				}
				gameEvent = null;
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				System.out.println("tres parou aqui");
				// --------------------------------------------------------------------------------------------------------------
				// jogador escolhe a carta que aplica energia
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
				// jogador escolher uma carta treinador para se utilizar
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

				boolean aham;

				if (acontece && vidaMax) {
					System.out.println("player>>>> " + player);
					if (deckAcionado.getTipo() == 5) {
						if (deckAcionado.getSelected() instanceof Treinador) {
							aham = ((Treinador) deckAcionado.getSelected()).treinador((Pokemon) ativoJ1.getPokAtivo(),
									descarteJ1.getBaralho(), bancoJ1.getBaralho(), ativoJ1.getBaralho(),
									deckAcionado.getBaralho(), deckAcionado, bancoJ1, ativoJ1);

							deckAcionado.mostraCartas();
							bancoJ1.mostraCartas();
							ativoJ1.mostraCartas();

							if (!aham) {
								gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.SEMCARTA, "");
								for (var observer : observers) {
									observer.notify(gameEvent);
								}
							} else {
								gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.TREINADORFEZ,
										((Treinador) deckAcionado.getSelected()).getQueFaz());
								for (var observer : observers) {
									observer.notify(gameEvent);
								}
							}

							maoJ1.removeSel();
							gameEvent = null;

						} else {
							System.out.println("carta errada, passou direto");
						}
					} else {
						System.out.println("baralho escolhido errado");
					}
				} else {
					System.out.println("nao tem treinador ou ta com a vida maxina e a carta é de cura");
					if (!vidaMax) {
						gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.VIDAMAX,
								deckAcionado.getSelected().getNome());
						for (var observer : observers) {
							observer.notify(gameEvent);
						}
					}
				}
				nextPlayer();
				gameEvent = null;
				System.out.println("player>>>" + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// --------------------------------------------------------------------------------------------------------------
			} else if (player != 2 & player != 3 && player != 4 && player != 5) {
				System.out.println("jogador errado");
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
			}
			// =======================================================================================================================
		} else if (deckAcionado == maoJ2) {
			if (player == 9) {
				System.out.println("n == 9 --------------");
				// testar se pode fazer
				boolean continua = false;
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
					Pokemon p;
					if (deckAcionado.getSelected() instanceof Pokemon) {
						p = (Pokemon) deckAcionado.getSelected();
						if (p.getGeracaoAnterior() == null) {
							if (bancoJ2.getCards().size() < 3) {
								bancoJ2.addCard(deckAcionado.getSelected());
								deckAcionado.removeSel();
								System.out.println("adicionou no banco e tirou do deck acionado");
							} else {
								// aviso banco lotado
								gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.BANCOLOTADO,
										"");
								for (var observer : observers) {
									observer.notify(gameEvent);
								}
								System.out.println("banco lotado");
								return;
							}
						}
					}
				} else {
					System.out.println("baralho ou carta errado");
					System.out.println("player>>> " + player);
				}
				gameEvent = null;
				nextPlayer();
				System.out.println("proximo jogador, terminou a primeira parte");
				System.out.println("player>>>" + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
				// --------------------------------------------------------------------------------------------------------------
				// jogador escolhe uma evolucao (carta) pra aplicar
			} else if (player == 10) {
				System.out.println("entrou no 10");
				boolean seTem = false;
				for (Card c : maoJ2.getCards()) {
					if (c instanceof Pokemon) {
						if (((Pokemon) c).getGeracaoAnterior() != null
								&& ((Pokemon) c).getGeracaoAnterior().equals(ativoJ2.getCards().get(0).getNome())) {
							seTem = true;
							break;
						}
					}
				}

				boolean compativel = true;
				if (seTem) {
					if (deckAcionado.getTipo() == 5) {
						if (ativoJ2.getCards().size() == 1) {
							Pokemon p;
							System.out.println("ativo tinha so uma carta e clicou certo");
							if (deckAcionado.getSelected() instanceof Pokemon) {
								p = (Pokemon) deckAcionado.getSelected();
								if (p.getGeracaoAnterior() != null) {
									compativel = ((Pokemon) deckAcionado.getSelected())
											.evoluir((Pokemon) deckAcionado.getSelected(), ativoJ2);

									deckAcionado.mostraCartas();
									ativoJ2.mostraCartas();

									maoJ2.removeSel();

									if (!compativel) {
										gameEvent = new GameEvent(this, GameEvent.Target.GWIN,
												GameEvent.Action.NAOCOMPATIVEL,
												"");
										for (var observer : observers) {
											observer.notify(gameEvent);
										}
									}
									System.out.println("evoluiu");
								} else {
									System.out.println("deck errado");
								}
							} else {
								System.out.println("ja ta evoluido");
							}
						}
					}
				}
				gameEvent = null;
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				System.out.println("tres parou aqui");
				// --------------------------------------------------------------------------------------------------------------
				// jogador escolhe a carta que aplica energia

			} else if (player == 11) {
				System.out.println("entrou no 11");
				if (deckAcionado.getSelected() instanceof Energia) {
					System.out.println("entrou no metodo");
					System.out.println("eneriga: " + ((Pokemon) ativoJ2.getPokAtivo()).getEnergia());
					Energia.setEnergia((Pokemon) ativoJ2.getPokAtivo(), descarteJ2.getBaralho(),
							maoJ2.getBaralho());
					System.out.println("energia: " + ((Pokemon) ativoJ2.getPokAtivo()).getEnergia());
					maoJ2.removeSel();
				}
				nextPlayer();
				System.out.println("player>>>> " + player);
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// --------------------------------------------------------------------------------------------------------------
				// jogador escolher uma carta treinador para se aplicar no ativo
			} else if (player == 12) {
				boolean acontece = false;
				boolean vidaMax = true;
				for (Card c : maoJ2.getCards())
					if (c instanceof Treinador) {
						System.out.println("É DE TREINADOR");
						acontece = true;
					}

				if (deckAcionado.getSelected() instanceof Treinador) {
					if (((Treinador) deckAcionado.getSelected()).getTipo() == Acao.CURA) {
						if (((Pokemon) ativoJ1.getPokAtivo()).getPs() == ((Pokemon) ativoJ1.getPokAtivo())
								.getVidaMaxima()) {
							vidaMax = false;
							System.out.println("É DE CURA E A VIDA TA MAX");
						}
						System.out.println("É DE CURA MAS DA PRA CURAR");
					}
				}

				if (acontece && vidaMax) {
					if (deckAcionado.getTipo() == 5) {
						if (deckAcionado.getSelected() instanceof Treinador) {
							((Treinador) deckAcionado.getSelected()).treinador((Pokemon) ativoJ2.getPokAtivo(),
									descarteJ2.getBaralho(), bancoJ2.getBaralho(), ativoJ2.getBaralho(),
									deckAcionado.getBaralho(), deckAcionado, bancoJ2, ativoJ2);
							gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.TREINADORFEZ,
									((Treinador) deckAcionado.getSelected()).getQueFaz());
							for (var observer : observers) {
								observer.notify(gameEvent);
							}
							gameEvent = null;
							maoJ2.removeSel();
						} else {
							System.out.println("carta NAO é de treinador");
						}
					} else {
						System.out.println("NAO CLICOU NO BARALHO CERTO");
					}
				}
				nextPlayer();
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
				// --------------------------------------------------------------------------------------------------------------
			} else if (player != 9 && player != 10 && player != 11 && player != 12) {
				System.out.println("jogador errado");
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				return;
			}
		}
	}

	public void confereMorrer() {
		System.out.println("player>>> " + player);
		boolean vai = false;
		if (player < 8) {
			vai = Pokemon.morrer(descarteJ1, ativoJ1);
		} else {
			vai = Pokemon.morrer(descarteJ2, ativoJ2);
		}

		if (vai) {
			if (player < 8) {
				descarteJ1.mostraCartas();
				ativoJ1.mostraCartas();
			} else {
				descarteJ2.mostraCartas();
				ativoJ2.mostraCartas();
			}

		}

	}

	// acionado pelo botao de comprar
	public void comprarCartaJ1() {
		GameEvent gameEvent = null;
		if (player != 1) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, "2");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (ganhador != 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		maoJ1.addCard(deckJ1.getCards().get(0));
		deckJ1.getBaralho().remove(0);
		nextPlayer();
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
		System.out.println("carta comprarda e proximo jogador");
		System.out.println("player>>>>> " + player);
	}

	public void comprarCartaJ2() {
		GameEvent gameEvent = null;
		if (player != 8) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, "1");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (ganhador != 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		maoJ2.addCard(deckJ2.getCards().get(0));
		deckJ2.getBaralho().remove(0);
		gameEvent = null;
		nextPlayer();
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
		System.out.println("carta comprarda e proximo jogador");
		System.out.println("player>>>>> " + player);
	}

	// acionado pelo botao de atacar
	public void atacar(int jogador) {
		GameEvent gameEvent = null;
		String texto;
		if (player != 6 && jogador == 1) {
			texto = "2";
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, texto);
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		} else if (player != 13 && jogador == 2) {
			texto = "1";
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, texto);
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		boolean deuBom = false;
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
		deuBom = Pokemon.atacar((Pokemon) principal.getPokAtivo(), (Pokemon) aux.getPokAtivo());
		System.out.println("ataque feito");
		System.out.println("vida do outro depois>>>" + ((Pokemon) aux.getPokAtivo()).getPs());
		System.out.println("energia do atacante depois>>>" + ((Pokemon) principal.getPokAtivo()).getEnergia());

		if (!deuBom) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.SEMENERGIA, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			gameEvent = null;
		}

		if (jogador == 1) {
			ativoJ1 = principal;
			ativoJ2 = aux;
		} else {
			ativoJ2 = principal;
			ativoJ1 = aux;
		}

		
		nextPlayer();
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	// acionado pelo botao Finalizar Jogada
	public void confirmar(int n) {
		GameEvent gameEvent = null;
		String texto;
		if (player != 7 && n == 1) {
			texto = "2";
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, texto);
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		} else if (player != 14 && n == 2) {
			texto = "1";
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.WRONGBUTTON, texto);
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}

		System.out.println("player>>> " + player);
		System.out.println("confirmando mortes....");

		if (player == 7) {
			Pokemon.morrer(descarteJ2, ativoJ2);
		} else {
			Pokemon.morrer(descarteJ1, ativoJ1);
		}

		if (player == 7) {
			descarteJ2.mostraCartas();
			ativoJ2.mostraCartas();
		} else {
			descarteJ1.mostraCartas();
			ativoJ1.mostraCartas();
		}

		boolean perde1 = false;
		if (ativoJ1.getCards().size() == 0) {
			if (bancoJ1.getBaralho().size() == 0) {
				perde1 = true;
			} else {
				int pos = 0;
				for (Card c : bancoJ1.getBaralho()) {
					if (c instanceof Pokemon) {
						ativoJ1.addCard(c);
						bancoJ1.getBaralho().remove(pos);
						((Pokemon) c).setEnergiaComeco();
						((Pokemon) c).setPs(((Pokemon) c).getVidaMaxima());
						break;
					}
					pos++;
				}
			}
		}

		boolean perde2 = false;
		if (ativoJ2.getCards().size() == 0) {
			if (bancoJ2.getBaralho().size() == 0) {
				perde2 = true;
			} else {
				int pos = 0;
				for (Card c : bancoJ2.getBaralho()) {
					if (c instanceof Pokemon) {
						ativoJ2.addCard(c);
						bancoJ2.getBaralho().remove(pos);
						break;
					}
					pos++;
				}
			}
		}

		ativoJ1.mostraCartas();
		bancoJ1.mostraCartas();
		ativoJ2.mostraCartas();
		bancoJ2.mostraCartas();

		if (perde1)
			ganhador = 1;
		else if (perde2)
			ganhador = 2;
		else
			ganhador = 0;

		String text = null;
		if (n == 1)
			text = "2";
		else
			text = "1";

		if (ganhador == 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.KEEPPLAYING, text);
			nextPlayer();
		} else if (ganhador == 1) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "2");
		} else if (ganhador == 2) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "1");
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

	public int getPlayer() {
		return player;
	}
}