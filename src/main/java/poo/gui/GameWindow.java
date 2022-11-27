package poo.gui;

import javafx.application.Application;
//import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import poo.modelo.Game;
import poo.modelo.GameEvent;
import poo.modelo.GameListener;

public class GameWindow extends Application implements GameListener {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Game.getInstance().addGameListener(this);

		primaryStage.setTitle("PokeJogo");

		Group root = new Group();

		TabPane tabPane = new TabPane();

		Tab tab1 = new Tab("Jogador 1");
		Tab tab2 = new Tab("Jogador 2");
		Tab tab3 = new Tab("Mesa");

		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);

		GridPane grid1 = new GridPane();
		grid1.setAlignment(Pos.BASELINE_LEFT);
		grid1.setHgap(10);
		grid1.setVgap(10);
		grid1.setPadding(new Insets(25, 25, 25, 25));

		DeckView deckJ1 = new DeckView(1);
		ScrollPane sd1 = new ScrollPane();
		sd1.setPrefSize(100, 250);
		sd1.setContent(deckJ1);
		grid1.add(sd1, 0, 0);

		DeckView maoJ1 = new DeckView(3);
		ScrollPane sd3 = new ScrollPane();
		sd3.setPrefSize(1000, 250);
		sd3.setContent(maoJ1);
		grid1.add(sd3, 0, 1);

		DeckView descarteJ1 = new DeckView(7);
		ScrollPane sd7 = new ScrollPane();
		sd7.setPrefSize(100, 250);
		sd7.setContent(descarteJ1);
		grid1.add(sd7, 0, 2);

		Button butBuy1 = new Button("Comprar");
		grid1.add(butBuy1, 1, 0);
		butBuy1.setOnAction(e -> Game.getInstance().comprarCartaJ1());

		Button butAtt = new Button("Atacar");
		grid1.add(butAtt, 2, 0);
		butAtt.setOnAction(e -> Game.getInstance().atacar(1));

		Button confirm2 = new Button("Finalizar Jogada");
		grid1.add(confirm2, 1, 1);
		confirm2.setOnAction(e -> Game.getInstance().confirmar(1));

		// -------------------------------------------------------------------

		GridPane grid2 = new GridPane();
		grid2.setAlignment(Pos.BASELINE_LEFT);
		grid2.setHgap(10);
		grid2.setVgap(10);
		grid2.setPadding(new Insets(25, 25, 25, 25));

		DeckView deckJ2 = new DeckView(2);
		ScrollPane sd2 = new ScrollPane();
		sd2.setPrefSize(100, 250);
		sd2.setContent(deckJ2);
		grid2.add(sd2, 0, 0);

		DeckView maoJ2 = new DeckView(4);
		ScrollPane sd4 = new ScrollPane();
		sd4.setPrefSize(1000, 250);
		sd4.setContent(maoJ2);
		grid2.add(sd4, 0, 1);

		DeckView descarteJ2 = new DeckView(8);
		ScrollPane sd8 = new ScrollPane();
		sd8.setPrefSize(100, 250);
		sd8.setContent(descarteJ2);
		grid2.add(sd8, 0, 2);

		Button butBuy2 = new Button("Comprar");
		grid2.add(butBuy2, 1, 0);
		butBuy2.setOnAction(e -> Game.getInstance().comprarCartaJ2());

		Button butAtt2 = new Button("Atacar");
		grid2.add(butAtt2, 2, 0);
		butAtt2.setOnAction(e -> Game.getInstance().atacar(2));

		Button confirm1 = new Button("Finalizar Jogada");
		grid2.add(confirm1, 1, 1);
		confirm1.setOnAction(e -> Game.getInstance().confirmar(2));

		// Button skip2 = new Button("Pular");
		// grid2.add(skip2, 2, 0);
		// skip2.setOnAction(e -> Game.getInstance().pular());

		// -------------------------------------------------------------------

		GridPane grid3 = new GridPane();
		grid3.setAlignment(Pos.BASELINE_LEFT);
		grid3.setHgap(10);
		grid3.setVgap(10);
		grid3.setPadding(new Insets(25, 25, 25, 25));

		// PlacarView placar = new PlacarView();
		// grid3.add(placar, 0, 1);

		// Button butClean = new Button("Clean");
		// grid3.add(butClean, 1, 1);
		// butClean.setOnAction(e -> Game.getInstance().removeSelected());

		DeckView ativoJ1 = new DeckView(5);
		ScrollPane sd5 = new ScrollPane();
		sd5.setPrefSize(250, 275);
		sd5.setContent(ativoJ1);
		grid3.add(sd5, 0, 0);

		DeckView bancoJ1 = new DeckView(9);
		ScrollPane sd9 = new ScrollPane();
		sd9.setPrefSize(650, 275);
		sd9.setContent(bancoJ1);
		grid3.add(sd9, 1, 0);

		DeckView ativoJ2 = new DeckView(6);
		ScrollPane sd6 = new ScrollPane();
		sd6.setPrefSize(225, 275);
		sd6.setContent(ativoJ2);
		grid3.add(sd6, 0, 1);

		DeckView bancoJ2 = new DeckView(10);
		ScrollPane sd10 = new ScrollPane();
		sd10.setPrefSize(625, 275);
		sd10.setContent(bancoJ2);
		grid3.add(sd10, 1, 1);

		EnergiaView placarEnergia = new EnergiaView();
		grid3.add(placarEnergia, 0, 2);

		VidaView placarVida = new VidaView();
		grid3.add(placarVida, 1, 2);

		TextArea regras = new TextArea("=-=-=-=-REGRAS-=-=-=-=" +
										"\nJogador 1 = cima " +
										"\nJogador 2 = baixo" +
										"\nCaso seu pokemon esteja evoluído, considere a segunda carta ativa." + 
										"\n\nConfira a ordem de jogada para não se perder: " +
										"\n1- Compre uma carta;" + 
										"\n2- Coloque um pokemon básico no banco (clique na carta virada para baixo para pular);" + 
										"\n3- Escolha uma evolução para seu pokemon ativo (clique na carta virada para baixo para pular);" +
										"\n4- Aplique uma carta de energia (clique na carta virada para baixo para pular);" +
										"\n5- Escolha uma carta de treinador para utilizar (clique na carta virada para baixo para pular);" + 
										"\n6- Ataque;" + 
										"\n7- Finalize sua rodada.");
		regras.setPrefWidth(225);
		regras.setPrefHeight(275);
		regras.setWrapText(true);
		grid3.add(regras, 2, 0);

		tab1.setContent(grid1);
		tab2.setContent(grid2);
		tab3.setContent(grid3);

		root.getChildren().add(tabPane);

		Scene scene = new Scene(root, Paint.valueOf("#DC143C"));

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void notify(GameEvent eg) {
		Alert alert;
		if (eg == null)
			return;
		if (eg.getTarget() == GameEvent.Target.GWIN) {
			switch (eg.getAction()) {
				case INVPLAY:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Jogada inválida!!");
					alert.setContentText("Era a vez do jogador " + eg.getArg());
					alert.showAndWait();
					break;
				case MUSTCLEAN:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText(null);
					alert.setContentText("Utilize o botao \"Clean\"");
					alert.showAndWait();
					break;
				case ENDGAME:
					String text = "Fim de Jogo !!\n";
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("PARABÉNS");
					alert.setHeaderText(text);
					alert.setContentText("O jogador " + eg.getArg() + " ganhou o jogo!!!");
					alert.showAndWait();
					break;
				case REMOVESEL:
					// Esse evento não vem para cá
					break;
				case MUSTBUY:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("COMPRE");
					alert.setHeaderText("Você precisa comprar uma carta para começar a jogada.");
					alert.setContentText("Compre uma carta com o botão \"Comprar\"");
					alert.showAndWait();
					break;
				case MUSTATTACK:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATAQUE");
					alert.setHeaderText("Você precisa atacar para finalizar sua jogada!!!");
					alert.setContentText("Ataque com o botão \"Atacar\"");
					alert.showAndWait();
					break;
				case CONTINUE:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Você deseja realizar a operação seguinte?");
					alert.setContentText("Operação: " + eg.getArg());
					alert.showAndWait();
					break;
				case VAI:
					// não faz nada
					break;
				case KEEPPLAYING:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("O jogo continuará! Ninguém ganhou ainda.");
					alert.setContentText("Vez do JOGADOR " + eg.getArg());
					alert.showAndWait();
					break;
				case TREINADORFEZ:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("AVISO");
					alert.setHeaderText("A carta treinador recém acionada fez...");
					alert.setContentText(eg.getArg());
					alert.showAndWait();
					break;
				case MUSTCONFIRM:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("CONFIRME");
					alert.setHeaderText("Finalize sua jogada!!");
					alert.setContentText(
							"Clique no botão \"Finalizar Jogada\" para conferir se nenhum pokemón já foi derrotado!");
					alert.showAndWait();
					break;
				case ATTACK:
					// nao vem pra ca
					break;
				case SHOWTABLE:
					// nao vem pra ca
					break;
				case WRONGBUTTON:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Jogada inválida!!");
					alert.setContentText("Não deveria clicar nesse botão agora! É a vez do jogador " + eg.getArg() +
							" ou deveria fazer outra ação.");
					alert.showAndWait();
					break;
				case BANCOLOTADO:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Seu banco está lotado!!");
					alert.setContentText(null);
					alert.showAndWait();
					break;
				case NAOCOMPATIVEL:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Evolução selecionada não é compatível com seu pokemón ativo!!");
					alert.setContentText("No canto esquerdo superior da sua carta de evolução, " +
							"existe um desenho do pokemón compatível. Dê uma olhada lá!");
					alert.showAndWait();
					break;
				case VIDAMAX:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("ATENÇÃO");
					alert.setHeaderText("Carta treinador \"" + eg.getArg() + "\"  não aplicada!");
					alert.setContentText("Não há danos em seu pokemón ativo para curar.");
					alert.showAndWait();
					break;
				case SEMENERGIA:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("SEM ENERGIA");
					alert.setHeaderText("Seu pokemón ativo não conseguiu realizar o ataque!");
					alert.setContentText("Não há energia suficiente para atacar. Tente atacar na próxima rodada.");
					alert.showAndWait();
					break;
				case SEMCARTA:
					alert = new Alert(AlertType.WARNING);
					alert.setTitle("SEM CARTA");
					alert.setHeaderText("Não há cartas disponíveis na pilha de descarte!");
					alert.setContentText(
							"Talvez você não tenha descartado nenhuma carta ainda, ou, não tenha descartado um pokemón básico para reviver.");
					alert.showAndWait();
					break;

			}

		}
	}

}
