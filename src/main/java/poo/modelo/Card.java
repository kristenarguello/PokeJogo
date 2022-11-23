package poo.modelo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Card {
	private String id;
	private String imageId;
	private boolean faceUp;
	private final PropertyChangeSupport pcs;
	private String nome;

	public Card(String anId, String anImageId, String nome) {
		id = anId;
		imageId = anImageId;
		this.nome = nome;
		faceUp = true;
		pcs = new PropertyChangeSupport(this); 
	}

	public String getId() {
		return id;
	}

	public String getImageId() {
		return imageId;
	}

	public String getNome() {
		return nome;
	}

	public boolean isFacedUp() {
		return faceUp;
	}

	public void flip() {
		boolean old = faceUp;
		faceUp = !faceUp;
		pcs.firePropertyChange("facedUp", old, faceUp);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
