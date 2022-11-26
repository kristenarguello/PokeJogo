package poo.modelo;

public class TesteDaLogica {
    public static void main(String[] args) {
        // CardDeck deck = new CardDeck(30);
        // CardDeck mao = new CardDeck(5, deck);
        // CardDeck banco = new CardDeck(3, mao);
        // CardDeck ativo = new CardDeck(2,banco);

        System.out.println("DECK");
        printaDeck(Game.getInstance().getDeckJ1());

        System.out.println("\n\nMAO");
        printaDeck(Game.getInstance().getMaoJ1());

        System.out.println("\n\nBANCO");
        printaDeck(Game.getInstance().getBancoJ1());

        System.out.println("\n\nATIVO");
        printaDeck(Game.getInstance().getAtivoJ1());
    }
    public static void printaDeck(CardDeck cd) {
        for(Card c : cd.getCards()) {
            System.out.println(c);
        }
    }

}


