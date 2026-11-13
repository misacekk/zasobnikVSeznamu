import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Zasobnik sane = new Zasobnik();

    public static void main(String[] args) {
        int volba;
        do {
            vypisMenu();
            volba = nactiInt("Zadej volbu: ");

            switch (volba) {
                case 1 -> nalozVybaveni();
                case 2 -> vylozVybaveni();
                case 3 -> zkontrolujVrchol();
                case 4 -> zkontrolujNaklad();
                case 5 -> vypocitejHmotnost();
                case 6 -> vyfiltrujPodleKategorie();
                case 7 -> najdiPoskozeneVybaveni();
                case 8 -> najdiZapomenuteVybaveni();
                case 9 -> odstranPoskozeneZbozi();
                case 10 -> zabalNaZachrannouMisi();
                case 0 -> System.out.println("Ukončuji program. Hodně štěstí na expedici!");
                default -> System.out.println("Neplatná volba, zkus to znovu.");
            }
        } while (volba != 0);
    }

    private static void vypisMenu() {
        System.out.println("\n=== ANTARKTICKÁ EXPEDICE – SPRÁVA SANÍ ===");
        System.out.println("1. Nalož vybavení");
        System.out.println("2. Vylož vybavení");
        System.out.println("3. Zkontroluj vrchol saní");
        System.out.println("4. Zkontroluj náklad");
        System.out.println("5. Vypočítej hmotnost");
        System.out.println("6. Vyfiltruj vybavení podle kategorie");
        System.out.println("7. Najdi poškozené vybavení");
        System.out.println("8. Najdi zapomenuté vybavení");
        System.out.println("9. Odstraň poškozené zboží");
        System.out.println("10. Zabal vybavení na záchrannou misi");
        System.out.println("0. Konec");
    }

    // 1. Nalož vybavení
    private static void nalozVybaveni() {
        System.out.print("Název vybavení: ");
        String nazev = scanner.nextLine().trim();

        System.out.print("Kategorie (např. vědecké, potraviny, bezpečnostní): ");
        String kategorie = scanner.nextLine().trim();

        double hmotnost = nactiKladnyDouble("Hmotnost (kg): ");
        boolean poskozene = nactiBoolean("Je vybavení poškozené? (ano/ne): ");

        sane.push(new Vybaveni(nazev, kategorie, hmotnost, poskozene));
        System.out.println("Vybavení '" + nazev + "' bylo naloženo na vrchol saní.");
    }

    // 2. Vylož vybavení
    private static void vylozVybaveni() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné, není co vykládat.");
            return;
        }
        Vybaveni vylozeno = sane.pop();
        System.out.println("Vyloženo ze saní: " + vylozeno);
    }

    // 3. Zkontroluj vrchol saní
    private static void zkontrolujVrchol() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }
        System.out.println("Na vrcholu saní se nachází: " + sane.peek());
    }

    // 4. Zkontroluj náklad (v pořadí vykládání, tj. od vrcholu dolů)
    private static void zkontrolujNaklad() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }

        Zasobnik pomocny = new Zasobnik();
        int poradi = 1;
        System.out.println("Náklad na saních (od vrcholu dolů):");

        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            System.out.println(poradi++ + ". " + prvek);
            pomocny.push(prvek);
        }

        vratZPomocneho(pomocny);
    }

    // 5. Vypočítej hmotnost
    private static void vypocitejHmotnost() {
        double celkovaHmotnost = spocitejCelkovouHmotnost();
        System.out.printf("Celková hmotnost nákladu na saních: %.2f kg%n", celkovaHmotnost);
    }

    // 6. Vyfiltruj vybavení podle kategorie
    private static void vyfiltrujPodleKategorie() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }

        System.out.print("Zadej hledanou kategorii: ");
        String hledanaKategorie = scanner.nextLine().trim();

        Zasobnik pomocny = new Zasobnik();
        boolean nalezeno = false;

        System.out.println("Vybavení v kategorii '" + hledanaKategorie + "':");
        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            if (prvek.getKategorie().equalsIgnoreCase(hledanaKategorie)) {
                System.out.println(" - " + prvek);
                nalezeno = true;
            }
            pomocny.push(prvek);
        }

        vratZPomocneho(pomocny);

        if (!nalezeno) {
            System.out.println("V této kategorii nebylo nalezeno žádné vybavení.");
        }
    }

    // 7. Najdi poškozené vybavení
    private static void najdiPoskozeneVybaveni() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }

        Zasobnik pomocny = new Zasobnik();
        int pocetPoskozenych = 0;

        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            if (prvek.isPoskozene()) {
                pocetPoskozenych++;
            }
            pomocny.push(prvek);
        }

        vratZPomocneho(pomocny);
        System.out.println("Počet poškozených kusů vybavení na saních: " + pocetPoskozenych);
    }

    // 8. Najdi zapomenuté vybavení
    private static void najdiZapomenuteVybaveni() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }

        System.out.print("Zadej název hledaného vybavení: ");
        String hledanyNazev = scanner.nextLine().trim();

        Zasobnik pomocny = new Zasobnik();
        Vybaveni nalezenyPredmet = null;

        // Odebíráme věci shora, dokud nenajdeme hledaný kus nebo nedojde zásobník
        while (!sane.isEmpty()) {
            Vybaveni aktualni = sane.pop();
            if (aktualni.getNazev().equalsIgnoreCase(hledanyNazev)) {
                nalezenyPredmet = aktualni;
                break; // Předmět vyjmeme (nevracíme ho zpět)
            } else {
                pomocny.push(aktualni);
            }
        }

        // Vrátíme dočasně odebrané věci zpět na saně ve stejném pořadí
        vratZPomocneho(pomocny);

        if (nalezenyPredmet != null) {
            System.out.println("Vybavení nalezeno a vyjmuto ze saní: " + nalezenyPredmet);
        } else {
            System.out.println("Vybavení '" + hledanyNazev + "' se na saních nenachází.");
        }
    }

    // 9. Odstraň poškozené zboží
    private static void odstranPoskozeneZbozi() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné.");
            return;
        }

        Zasobnik pomocny = new Zasobnik();
        List<String> vyrazeneNazvy = new ArrayList<>();
        double vyrazenaHmotnost = 0;

        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            if (prvek.isPoskozene()) {
                vyrazeneNazvy.add(prvek.getNazev());
                vyrazenaHmotnost += prvek.getHmotnost();
            } else {
                pomocny.push(prvek);
            }
        }

        vratZPomocneho(pomocny);

        if (vyrazeneNazvy.isEmpty()) {
            System.out.println("Na saních nebylo žádné poškozené vybavení.");
        } else {
            System.out.println("Vyřazené poškozené vybavení: " + String.join(", ", vyrazeneNazvy));
            System.out.printf("Celková hmotnost vyřazených věcí: %.2f kg%n", vyrazenaHmotnost);
        }
    }

    // 10. Zabal vybavení na záchrannou misi
    private static void zabalNaZachrannouMisi() {
        if (sane.isEmpty()) {
            System.out.println("Saně jsou prázdné, není co balit.");
            return;
        }

        double maxNosnost = nactiKladnyDouble("Zadej maximální nosnost saní pro záchrannou misi (kg): ");

        Zasobnik pomocny = new Zasobnik();
        List<Vybaveni> trvaleOdebrane = new ArrayList<>();
        double aktualniHmotnost = 0;
        int pocetBezpecnostnich = 0;

        // Krok 1: Odstraníme všechno poškozené vybavení a zjistíme stav nepoškozeného nákladu
        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            if (prvek.isPoskozene()) {
                trvaleOdebrane.add(prvek);
            } else {
                aktualniHmotnost += prvek.getHmotnost();
                if (prvek.getKategorie().equalsIgnoreCase("bezpečnostní")) {
                    pocetBezpecnostnich++;
                }
                pomocny.push(prvek);
            }
        }
        vratZPomocneho(pomocny);

        // Krok 2: Odebíráme vybavení shora, dokud hmotnost neklesne pod/na povolený limit,
        // ale musíme zachovat alespoň 1 kus z kategorie "bezpečnostní"
        int odebranoKvuliNosnosti = 0;

        while (!sane.isEmpty() && aktualniHmotnost > maxNosnost) {
            Vybaveni prvek = sane.pop();
            boolean jeBezpecnostni = prvek.getKategorie().equalsIgnoreCase("bezpečnostní");

            if (jeBezpecnostni && pocetBezpecnostnich == 1) {
                // Poslední bezpečnostní vybavení musíme na saních nechat -> odložíme bokem
                pomocny.push(prvek);
            } else {
                trvaleOdebrane.add(prvek);
                odebranoKvuliNosnosti++;
                aktualniHmotnost -= prvek.getHmotnost();
                if (jeBezpecnostni) {
                    pocetBezpecnostnich--;
                }
            }
        }

        // Vrátíme případné chráněné bezpečnostní vybavení zpět na saně
        vratZPomocneho(pomocny);

        // Krok 3: Zjistíme všechno bezpečnostní vybavení, které na saních zůstalo
        List<Vybaveni> zustaloBezpecnostni = new ArrayList<>();
        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            if (prvek.getKategorie().equalsIgnoreCase("bezpečnostní")) {
                zustaloBezpecnostni.add(prvek);
            }
            pomocny.push(prvek);
        }
        vratZPomocneho(pomocny);

        // Výpis výsledků mise
        System.out.println("\n--- VÝSLEDEK PŘÍPRAVY NA ZÁCHRANNOU MISI ---");
        if (aktualniHmotnost > maxNosnost) {
            System.out.println("UPOZORNĚNÍ: Ani po odebrání všeho ostatního nákladu se nepodařilo dostat pod limit, "
                    + "protože poslední bezpečnostní vybavení přesahuje nosnost!");
        }

        System.out.println("1. Trvale odebrané vybavení:");
        if (trvaleOdebrane.isEmpty()) {
            System.out.println("   (Žádné vybavení nebylo odebráno)");
        } else {
            for (Vybaveni v : trvaleOdebrane) {
                System.out.println("   - " + v);
            }
        }

        System.out.println("2. Počet kusů odstraněných kvůli omezené nosnosti: " + odebranoKvuliNosnosti);
        System.out.printf("3. Výsledná hmotnost nákladu: %.2f kg%n", aktualniHmotnost);

        System.out.println("4. Bezpečnostní vybavení, které zůstalo na saních:");
        if (zustaloBezpecnostni.isEmpty()) {
            System.out.println("   (Na saních nebylo žádné nepoškozené bezpečnostní vybavení)");
        } else {
            for (Vybaveni v : zustaloBezpecnostni) {
                System.out.println("   - " + v);
            }
        }
    }

    // --- Pomocné metody ---

    private static void vratZPomocneho(Zasobnik pomocny) {
        while (!pomocny.isEmpty()) {
            sane.push(pomocny.pop());
        }
    }

    private static double spocitejCelkovouHmotnost() {
        Zasobnik pomocny = new Zasobnik();
        double soucet = 0;

        while (!sane.isEmpty()) {
            Vybaveni prvek = sane.pop();
            soucet += prvek.getHmotnost();
            pomocny.push(prvek);
        }

        vratZPomocneho(pomocny);
        return soucet;
    }

    private static int nactiInt(String vyzva) {
        while (true) {
            System.out.print(vyzva);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Zadej prosím platné celé číslo.");
            }
        }
    }

    private static double nactiKladnyDouble(String vyzva) {
        while (true) {
            System.out.print(vyzva);
            try {
                double hodnota = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                if (hodnota >= 0) {
                    return hodnota;
                }
                System.out.println("Hodnota nesmí být záporná.");
            } catch (NumberFormatException e) {
                System.out.println("Zadej prosím platné číslo.");
            }
        }
    }

    private static boolean nactiBoolean(String vyzva) {
        while (true) {
            System.out.print(vyzva);
            String vstup = scanner.nextLine().trim().toLowerCase();
            if (vstup.equals("ano") || vstup.equals("a") || vstup.equals("true") || vstup.equals("1")) {
                return true;
            }
            if (vstup.equals("ne") || vstup.equals("n") || vstup.equals("false") || vstup.equals("0")) {
                return false;
            }
            System.out.println("Odpověz prosím 'ano' nebo 'ne'.");
        }
    }
}