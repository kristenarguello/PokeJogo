package poo.gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

		primaryStage.setTitle("Batalha de Cartas");

		Group root = new Group();

        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Jogador 1");
        Tab tab2 = new Tab("Jogador 2");
        Tab tab3 = new Tab("Mesa");
        //Tab tab4 = new Tab("Mesa Jogador 2");

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
		butBuy1.setOnAction(e -> Game.getInstance().comprarCarta(1));
		
		Button butAtt = new Button("Atacar");
		grid1.add(butAtt, 2, 1);
		butAtt.setOnAction(e -> Game.getInstance().atacar(1));


//-------------------------------------------------------------------


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

		// DeckView ativoJ2 = new DeckView(6);
		// ScrollPane sd6 = new ScrollPane();
		// sd6.setPrefSize(1200, 250);
		// sd6.setContent(ativoJ2);
		// grid2.add(sd6, 0, 0);

		DeckView descarteJ2 = new DeckView(8);
		ScrollPane sd8 = new ScrollPane();
		sd8.setPrefSize(100, 250);
		sd8.setContent(descarteJ2);
		grid2.add(sd8, 0, 2);

		// DeckView bancoJ2 = new DeckView(10);
		// ScrollPane sd10 = new ScrollPane();
		// sd10.setPrefSize(1200, 250);
		// sd10.setContent(bancoJ2);
		// grid2.add(sd10, 0, 0);

		Button butBuy2 = new Button("Comprar");
		grid2.add(butBuy2, 1, 0);
		butBuy2.setOnAction(e -> Game.getInstance().comprarCarta(2));

		Button butAtt2 = new Button("Atacar");
		grid2.add(butAtt2, 2, 1);
		butAtt2.setOnAction(e -> Game.getInstance().atacar(2));

	
//-------------------------------------------------------------------

		GridPane grid3 = new GridPane();
		grid3.setAlignment(Pos.CENTER);
		grid3.setHgap(10);
		grid3.setVgap(10);
		grid3.setPadding(new Insets(25, 25, 25, 25));

		// PlacarView placar = new PlacarView();
		// grid3.add(placar, 0, 1);

		Button butClean = new Button("Clean");
		grid3.add(butClean, 1, 1);
		butClean.setOnAction(e -> Game.getInstance().removeSelected());

		DeckView ativoJ1 = new DeckView(5);
		ScrollPane sd5 = new ScrollPane();
		sd5.setPrefSize(1200, 250);
		sd5.setContent(ativoJ1);
		grid3.add(sd5, 0, 0);

		DeckView bancoJ1 = new DeckView(9);
		ScrollPane sd9 = new ScrollPane();
		sd9.setPrefSize(1200, 250);
		sd9.setContent(bancoJ1);
		grid3.add(sd9, 1, 0);


		tab1.setContent(grid1);
        tab2.setContent(grid2);
        tab3.setContent(grid3);


		root.getChildren().add(tabPane);
		
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
	

	@Override
	public void notify(GameEvent eg) {
		Alert alert;
		if (eg == null) return;
		if (eg.getTarget() == GameEvent.Target.GWIN) {
			switch (eg.getAction()) {
			case INVPLAY:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText("Jogada inválida!!");
				alert.setContentText("Era a vez do jogador " + eg.getArg());
				alert.showAndWait();
				break;
			case MUSTCLEAN:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText(null);
				alert.setContentText("Utilize o botao \"Clean\"");
				alert.showAndWait();
				break;
			case ENDGAME:
				String text = "Fim de Jogo !!\n";
				// if (Game.getInstance().getPtsJ1() > Game.getInstance().getPtsJ2()) {
				// 	text += "O jogador 1 ganhou !! :-)";
				// } else {
				// 	text += "O jogador 2 ganhou !! :-)";
				// }
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Parabens !!");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
				break;
			case REMOVESEL:
				// Esse evento não vem para cá
			case MUSTBUY:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("COMPRE");
				alert.setHeaderText("Você precisa comprar uma carta para começar a jogada");
				alert.setContentText("Compre uma carta com o botão \"Comprar\"");
				alert.showAndWait();
			case MUSTATTACK:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("ATAQUE");
				alert.setHeaderText("Você precisa atacar para finalizar sua jogada!!!");
				alert.setContentText("Ataque com o botão \"Atacar\"");
				alert.showAndWait();
			}
			
		}
	}

}
