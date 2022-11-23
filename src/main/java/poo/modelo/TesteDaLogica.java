package poo.modelo;

public class TesteDaLogica {
    public static void main(String[] args) {
        CardDeck um = new CardDeck(30);
        System.out.println("DECK");
        um.printar(um.getDeck());
        System.out.println("\nMAO");
        um.printar(um.getMao());
        System.out.println("\nBANCO");
        um.printar(um.getBanco());
        System.out.println("\nDESCARTE");
        um.printar(um.getDescarte());

        um.inicio();
        System.out.println("\n\n\n");
        System.out.println("DECK");
        um.printar(um.getDeck());
        System.out.println("\nMAO");
        um.printar(um.getMao());
        System.out.println("\nBANCO");
        um.printar(um.getBanco());
        System.out.println("\nDESCARTE");
        um.printar(um.getDescarte());

    }
}
