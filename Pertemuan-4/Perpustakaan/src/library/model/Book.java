package library.model;

/**
 * Class Book merepresentasikan sebuah buku dalam sistem perpustakaan.
 *
 * Konsep OOP yang didemonstrasikan di sini:
 * - Class & Object      : Book adalah class, setiap buku yang dibuat adalah object.
 * - Variabel (field)    : judul, penulis, tahunTerbit, kategori, statusKetersediaan.
 * - Constructor         : Book(...) digunakan untuk inisialisasi object baru.
 * - Method              : getter/setter dan toString().
 * - Tipe data primitive : tahunTerbit (int), statusKetersediaan (boolean).
 * - Tipe data reference : judul, penulis, kategori (String -> class/reference type).
 */
public class Book {

    // ===== Variabel (field) instance =====
    private String judul;              // reference type
    private String penulis;            // reference type
    private int tahunTerbit;           // primitive type
    private String kategori;           // reference type
    private boolean statusKetersediaan; // primitive type -> true = tersedia, false = dipinjam

    // ===== Constructor =====
    // Constructor dipakai untuk menginisialisasi setiap object Book yang baru dibuat.
    // Saat buku baru ditambahkan, secara default statusnya "tersedia" (true).
    public Book(String judul, String penulis, int tahunTerbit, String kategori) {
        this.judul = judul;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.kategori = kategori;
        this.statusKetersediaan = true; // buku baru selalu tersedia
    }

    // Constructor overload (opsional), jika status ketersediaan ingin ditentukan langsung.
    public Book(String judul, String penulis, int tahunTerbit, String kategori, boolean statusKetersediaan) {
        this.judul = judul;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.kategori = kategori;
        this.statusKetersediaan = statusKetersediaan;
    }

    // ===== Getter & Setter (method) =====
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public boolean isStatusKetersediaan() {
        return statusKetersediaan;
    }

    public void setStatusKetersediaan(boolean statusKetersediaan) {
        this.statusKetersediaan = statusKetersediaan;
    }

    /**
     * Method cocokBerdasarkanKataKunci mendemonstrasikan manipulasi String:
     * menggunakan toLowerCase() dan contains() untuk pencarian yang tidak
     * peka huruf besar/kecil (case-insensitive), baik pada judul maupun kategori.
     */
    public boolean cocokBerdasarkanKataKunci(String kataKunci) {
        String kunci = kataKunci.toLowerCase(); // manipulasi String #1
        boolean cocokJudul = this.judul.toLowerCase().contains(kunci);
        boolean cocokKategori = this.kategori.toLowerCase().contains(kunci);
        return cocokJudul || cocokKategori; // struktur kondisional logis (OR)
    }

    @Override
    public String toString() {
        // Manipulasi String #2: menggunakan String.format untuk menyusun tampilan rapi.
        String status = statusKetersediaan ? "Tersedia" : "Dipinjam";
        return String.format("%-30s | %-20s | %-4d | %-15s | %s",
                judul, penulis, tahunTerbit, kategori, status);
    }
}
