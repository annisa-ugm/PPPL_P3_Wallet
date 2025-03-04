import org.junit.jupiter.api.*;
import java.util.List;

class WalletTest {
    private static Wallet wallet;
    @BeforeAll
    static void setupClass() {
        System.out.print("Koneksi ke server....");
        wallet = new Wallet();
    }

    @BeforeEach
    void setupMethod() {
        wallet.addMoney(3000);
        wallet.getTotalMoney();
        wallet.setOwner("Iza");
    }

    @Test
    public void testSetOwner() {
        wallet.setOwner("Omar");
        Assertions.assertEquals("Omar", wallet.getOwner(), "Owner harus sesuai dengan yang di-set");
    }

    @Test
    public void testChangeOwner() {
        wallet.setOwner("Lucas");
        Assertions.assertEquals("Lucas", wallet.getOwner(), "Owner harus sesuai dengan yang di-set");

        // Perubahan owner
        wallet.setOwner("Yao");
        Assertions.assertEquals("Yao", wallet.getOwner(), "Owner harus bisa diubah ke nama lain");
    }

    @Test
    public void testSetEmptyOwner() {
        wallet.setOwner("");
        Assertions.assertNotEquals("", wallet.getOwner(), "Owner tidak  bisa di-set sebagai string kosong");
    }

    @Test
    public void testSetNullOwner() {
        wallet.setOwner(null);
        Assertions.assertNotNull(wallet.getOwner(), "Owner tidak  bisa di-set sebagai null");
    }

    @Test
    public void testSetOwnerWithSpecialCharacters() {
        wallet.setOwner("Jun3 j0l!");
        Assertions.assertEquals("Jun3 j0l!", wallet.getOwner(), "Owner harus bisa menerima karakter spesial");
    }

    @Test
    public void testSetLongOwnerName() {
        String longName = "B".repeat(1000);
        wallet.setOwner(longName);
        Assertions.assertEquals(longName, wallet.getOwner(), "Owner harus bisa menangani nama panjang");
    }

    @Test
    public void testAddAndRemoveCard() {
        wallet.addCard("NPWP");
        wallet.addCard("SIM");

        System.out.println();
        System.out.println("Kartu yang ada di dalam wallet saat ini: " + wallet.getCards());
        List<String> cards = wallet.getCards();
        Assertions.assertTrue(cards.contains("NPWP"), "Ada kartu NPWP di wallet");
        Assertions.assertTrue(cards.contains("SIM"), "Ada kartu SIM di wallet");

        boolean isRemoved = wallet.removeCard("NPWP");
        System.out.println("Kartu yang ada di dalam wallet saat ini setelah NPWP dihapus: " + wallet.getCards());
        Assertions.assertTrue(isRemoved, "Kartu NPWP telah dihapus dari wallet");
        Assertions.assertFalse(wallet.getCards().contains("NPWP"), "Tidak ada kartu NPWP di wallet");
    }

    @Test
    public void testAddDuplicateCard() {
        wallet.addCard("KTP");
        System.out.println();
        System.out.println("Kartu yang ada di dalam wallet saat ini : " + wallet.getCards());
        wallet.addCard("KTP"); // KTP ditambahkan lagi
        System.out.println("Kartu yang ada di dalam wallet saat ini setelah KTP ditambahkan lagi : " + wallet.getCards());

        List<String> cards = wallet.getCards();
        Assertions.assertEquals(2, cards.size(), "Kartu duplikat harus bisa ditambahkan ke dalam dompet");
    }

    @Test
    public void testRemoveNonExistentCard() {
        wallet.addCard("ATM");

        boolean isRemoved = wallet.removeCard("KTP"); // KTP belum ditambahkan
        Assertions.assertFalse(isRemoved, "Harus gagal menghapus kartu yang tidak ada di dompet");
    }

    @Test
    public void testAddAndRemoveEmptyCard() {
        wallet.addCard("");

        Assertions.assertFalse(wallet.getCards().contains(""), "Kartu dengan string kosong tidak bisa ditambahkan");

        boolean isRemoved = wallet.removeCard("");
        Assertions.assertFalse(isRemoved, "Kartu dengan string kosong tidak dapat dihapus karena tidak mungkin ada di dalam list");
        Assertions.assertFalse(wallet.getCards().contains(""), "Kartu kosong tidak ada sejak awal");
    }

    @Test
    public void testRemoveAllCards() {
        wallet.addCard("ATM BCA");
        wallet.addCard("Kartu Kredit");
        wallet.addCard("SIM");

        wallet.removeCard("ATM BCA");
        wallet.removeCard("Kartu Kredit");
        wallet.removeCard("SIM");

        Assertions.assertTrue(wallet.getCards().isEmpty(), "Semua kartu harus tidak ada di dalam wallet setelah dihapus");
    }


    @Test
    public void testAddMoney() {
        wallet.addMoney(5000);
        wallet.addMoney(500);
        wallet.addMoney(2000);
        wallet.addMoney(100);

        Assertions.assertEquals(10600, wallet.getTotalMoney(), "Total uang harus sesuai dengan yang ditambahkan");
    }

    @Test
    public void testAddMoneyCategorization() {
        wallet.addMoney(4000); // Lembaran
        wallet.addMoney(500);  // Koin
        wallet.addMoney(100);  // Koin

        Assertions.assertEquals(2, wallet.getUangLembaran().size(), "Harus ada 2 uang lembaran");
        Assertions.assertEquals(2, wallet.getUangKoin().size(), "Harus ada 2 uang koin");
    }

    @Test
    public void testAddZeroMoney() {
        wallet.addMoney(0);

        Assertions.assertEquals(3000, wallet.getTotalMoney(), "Tidak boleh menambahkan uang 0");
    }

    @Test
    public void testAddNegativeMoney() {
        wallet.addMoney(-1000);

        Assertions.assertEquals(3000, wallet.getTotalMoney(), "Uang negatif tidak boleh ditambahkan");
    }

    @Test
    public void testWithdrawMoney() {
        wallet.addMoney(5000);
        wallet.addMoney(2000);
        wallet.addMoney(1000);
        wallet.addMoney(500);
        wallet.addMoney(100);

        boolean isWithdrawn = wallet.withdrawMoney(600);
        Assertions.assertTrue(isWithdrawn);
        Assertions.assertEquals(11000, wallet.getTotalMoney(), "Total uang harus berkurang sesuai dengan jumlah yang ditarik");
    }

    @Test
    public void testWithdrawUnmatchableAmount() {
        wallet.addMoney(5000);
        wallet.addMoney(2000);

        boolean isWithdrawn = wallet.withdrawMoney(1500);
        Assertions.assertFalse(isWithdrawn, "Harus gagal menarik uang karena tidak ada pecahan yang cocok");
        Assertions.assertEquals(10000, wallet.getTotalMoney(), "Total uang tidak boleh berubah jika gagal menarik");
    }

    @Test
    public void testWithdrawAllMoney() {
        wallet.addMoney(5000);
        wallet.addMoney(2000);
        wallet.addMoney(1000);

        boolean isWithdrawn = wallet.withdrawMoney(11000);
        Assertions.assertTrue(isWithdrawn, "Harus bisa menarik seluruh saldo");
        Assertions.assertEquals(0, wallet.getTotalMoney(), "Dompet harus kosong setelah menarik semua uang");
    }

    @Test
    public void testWithdrawMoreThanAvailable() {
        wallet.addMoney(10000);

        boolean isWithdrawn = wallet.withdrawMoney(15000);
        Assertions.assertFalse(isWithdrawn, "Harus gagal menarik lebih dari saldo");
        Assertions.assertEquals(13000, wallet.getTotalMoney(), "Saldo tidak boleh berubah jika gagal menarik");
    }

    @Test
    public void testWithdrawZeroOrNegative() {
        wallet.addMoney(5000);

        Assertions.assertFalse(wallet.withdrawMoney(0), "Harus gagal menarik uang 0");
        Assertions.assertFalse(wallet.withdrawMoney(-1000), "Harus gagal menarik uang berjumlah negatif");
        Assertions.assertEquals(8000, wallet.getTotalMoney(), "Saldo tidak boleh berubah jika gagal menarik");
    }

    @Test
    public void testClearWallet() {
        wallet.addCard("SIM");
        wallet.addMoney(5000);

        wallet.clearWallet();

        Assertions.assertTrue(wallet.getCards().isEmpty(), "Dompet harus kosong dari kartu setelah clear");
        Assertions.assertEquals(0, wallet.getTotalMoney(), "Saldo dompet harus nol setelah clear");
    }


    @AfterEach
    void tearDownTest() {
        System.out.println();
        System.out.println("Total uang setelah test: " + wallet.getTotalMoney());
        System.out.println("Nama owner saat ini: " + wallet.getOwner());
        wallet.clearWallet();
    }

    @AfterAll
    static void cleanup() {
        wallet.clearWallet();
    }

}

