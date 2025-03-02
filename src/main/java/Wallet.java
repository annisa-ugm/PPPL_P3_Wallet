import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String owner;
    private List<String> cards;
    public List<Integer> getUangLembaran() {
        return uangLembaran;
    }
    private List<Integer> uangLembaran;
    public List<Integer> getUangKoin() {
        return uangKoin;
    }
    private List<Integer> uangKoin;


    public Wallet() {
        this.cards = new ArrayList<>();
        this.uangLembaran = new ArrayList<>();
        this.uangKoin = new ArrayList<>();
    }

    public void setOwner(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            return; // jika owner kosong atau null, akan diabaikan
        }
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }

    public void addCard(String card) {
        if (card == null || card.trim().isEmpty()) {
            return;
        }
        cards.add(card);
    }
    public boolean removeCard(String card) {
        return cards.remove(card);
    }
    public List<String> getCards() {
        return new ArrayList<>(cards);
    }


    public void addMoney(int amount) {
        if (amount > 0) {
            if (amount >= 1000) {
                uangLembaran.add(amount);
            } else {
                uangKoin.add(amount);
            }
        }
    }
    public boolean withdrawMoney(int amount) {
        if (amount <= 0) {
            return false;
        }

        int totalMoney = getTotalMoney();
        if (amount > totalMoney) {
            return false;
        }

        int remaining = amount;
        uangLembaran.sort((a, b) -> b - a); // uang diurutkan dari terbesar ke terkecil
        uangKoin.sort((a, b) -> b - a);

        List<Integer> tempUangLembaran = new ArrayList<>(uangLembaran);
        List<Integer> tempUangKoin = new ArrayList<>(uangKoin);
        List<Integer> toRemoveLembaran = new ArrayList<>();
        List<Integer> toRemoveKoin = new ArrayList<>();

        // Logika penarikan dari uang lembaran
        for (int uang : tempUangLembaran) {
            if (uang <= remaining) {
                remaining -= uang;
                toRemoveLembaran.add(uang);
            }
            if (remaining == 0) break;
        }

        // penarikan uang koin jika masih ada sisa dari penarikan uang lembaran
        for (int uang : tempUangKoin) {
            if (uang <= remaining) {
                remaining -= uang;
                toRemoveKoin.add(uang);
            }
            if (remaining == 0) break;
        }

        // Jika remaining = 0, uang asli dari wallet akan dihapus
        if (remaining == 0) {
            uangLembaran.removeAll(toRemoveLembaran);
            uangKoin.removeAll(toRemoveKoin);
            return true;
        }

        return false; // Jika gagal, maka tidak ada perubahan jumlah uang di dompet
    }

    public int getTotalMoney() {
        return uangLembaran.stream().mapToInt(Integer::intValue).sum() +
                uangKoin.stream().mapToInt(Integer::intValue).sum();
    }

    public void clearWallet() {
        cards.clear();
        uangKoin.clear();
        uangLembaran.clear();
    }
}
