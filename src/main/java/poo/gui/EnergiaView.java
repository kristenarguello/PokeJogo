package poo.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import poo.modelo.Game;
import poo.modelo.GameEvent;
import poo.modelo.GameListener;
import poo.modelo.Pokemon;


public class EnergiaView extends GridPane implements GameListener {
    private TextField j1, j2;

	public EnergiaView() {
		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));

		Game.getInstance().addGameListener(this);

		j1 = new TextField();
		j2 = new TextField();

		this.add(new Label("Jogador 1:"), 0, 0);
		this.add(j1, 1, 0);
		this.add(new Label("Jogador 2:"), 0, 1);
		this.add(j2, 1, 1);

		// if (qual==1) {
		j1.setText("energia ->  " + ((Pokemon)Game.getInstance().getAtivoJ1().getPokAtivo()).getEnergia());
		j2.setText("energia ->  " + ((Pokemon)Game.getInstance().getAtivoJ2().getPokAtivo()).getEnergia());
		// } else if (qual==2) {
	 	// j1.setText("vida - " + ((Pokemon)Game.getInstance().getAtivoJ1().getPokAtivo()).getPs());
		// j2.setText("vida - " + ((Pokemon)Game.getInstance().getAtivoJ2().getPokAtivo()).getPs());
		// }
    }

	@Override
	public void notify(GameEvent event) {
		if (Game.getInstance().getAtivoJ1().getCards().size()==0) {
			j1.setText("morreu");
		} else {
			j1.setText("energia -> " + ((Pokemon)Game.getInstance().getAtivoJ1().getPokAtivo()).getEnergia());
		}
		
		if (Game.getInstance().getAtivoJ2().getCards().size()==0) {
			j2.setText("morreu");
		} else {
			j2.setText("energia -> " + ((Pokemon)Game.getInstance().getAtivoJ2().getPokAtivo()).getEnergia());
		}

	}
}
