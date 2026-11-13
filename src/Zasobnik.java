public class Zasobnik {
    private Node top;
    private int velikost;

    public Zasobnik() {
        this.top = null;
        this.velikost = 0;
    }

    // Test na prázdný zásobník
    public boolean isEmpty() {
        return top == null; // zároveň platí velikost == 0
    }

    public int getVelikost() {
        return velikost;
    }

    // Vložení prvku na vrchol (push)
    public void push(Vybaveni prvek) {
        Node novy = new Node(prvek);
        novy.next = top;
        top = novy;
        velikost++;
    }

    // Odebrání prvku z vrcholu (pop) s ošetřením prázdného zásobníku
    public Vybaveni pop() {
        if (isEmpty()) {
            return null;
        }
        Vybaveni data = top.data;
        top = top.next;
        velikost--;
        return data;
    }

    // Nahlédnutí na vrchol bez odebrání (peek)
    public Vybaveni peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }
}