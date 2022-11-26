package poo.modelo;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
	private static ImageFactory imgf = new ImageFactory();
	private Map<String, Image> images;

	public static ImageFactory getInstance() {
		return imgf;
	}

	private ImageFactory() {
		images = new HashMap<>();
	}

	private String id2File(String imgId) {
		switch (imgId) {
		case "imgSquirtle":
			return ("/imagens/squirtle.png");
		case "imgBulbasaur":
			return ("/imagens/bulbasaur.png");
		case "imgCharmander":
			return ("/imagens/charmander.png");
		case "imgEevee":
			return ("/imagens/eevee.png");
		case "imgSnorlax":
			return ("/imagens/snorlax.png");
		case "imgChansey":
			return ("/imagens/chansey.png");
		case "imgAlomomola":
			return ("/imagens/alomomola.png");
		case "imgMaractus":
			return ("/imagens/maractus.png");
		case "imgTorkoal":
			return ("/imagens/torkoal.png");
		case "imgPocao":
			return ("/imagens/pocao.png");
		case "imgSuper":
			return ("/imagens/superPocao.png");
		case "imgSubst":
			return ("/imagens/substituicao.png");
		case "imgRev":
			return ("/imagens/reviver.png");
		case "imgRecupera":
			return ("/imagens/recuperacaoEnergia.png");
		case "imgSubstitu_energ":
			return ("/imgens/substituicao-energia.png");
		case "imgVaporeon":
			return ("/imagens/vaporeon.png");
		case "imgFlareon":
			return ("/imagens/flareon.png");
		case "imgLeafeon":
			return ("/imagens/leafeon.png");
		case "imgEnergia":
			return ("/imagens/energia.png");
		case "imgWartortle":
			return ("/imagens/wartortle.png");
		case "imgIvysaur":
			return ("/imagens/ivysaur.png");
		case "imgCharmeleon":
			return ("/imagens/charmeleon.png");
		case "imgVerso":
			return ("/imagens/verso.png");
		default:
			throw new IllegalArgumentException("Invalid image Id");
		}
	}


	public ImageView createImage(String imgId) {
		Image img = images.get(imgId);
		
		if (img == null) {
//			img = new Image(id2File(imgId));
			img = new Image(getClass().getResourceAsStream(id2File(imgId)),300,150,true,true);
			images.put(imgId, img);
		}

		ImageView imgv = new ImageView(img);
		return imgv;
	}
}
