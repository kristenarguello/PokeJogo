package poo.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import poo.modelo.Game;
import poo.modelo.GameEvent;
import poo.modelo.GameListener;

public class JogadaView extends GridPane implements GameListener {
	private TextField player;

	public JogadaView() {//1=energia | 2=vida 
		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));

		Game.getInstance().addGameListener(this);

		player = new TextField();

		this.add(new Label("Jogada:"), 0, 0);
		this.add(player, 1, 0);
		

	 	player.setText("nº -> " + Game.getInstance().getPlayer());
		
	
	}


	@Override
	public void notify(GameEvent event) {
		if (Game.getInstance().getPlayer()>7) {
            player.setText("nº -> " + (Game.getInstance().getPlayer()-7));
        } else {
            player.setText("nº -> " + Game.getInstance().getPlayer());
        }
	}
}
