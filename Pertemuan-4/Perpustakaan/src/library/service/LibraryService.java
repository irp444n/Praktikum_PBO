package library.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import library.model.Book;
import library.model.Member;
import library.exception.BookNotFoundException;
import library.exception.BookAlreadyBorrowedException;
import library.exception.BorrowLimitExceededException;

/**
 * LibraryService berisi seluruh logika bisnis perpustakaan:
 * manajemen buku, manajemen anggota, transaksi pinjam/kembali, serta analisis.
 *
 * Konsep yang didemonstrasikan di file ini:
 * - Package         : bagian dari package library.service, memisahkan logika dari model & main.
 * - ArrayList       : koleksi Book dan riwayat transaksi.
 * - HashMap         : penyimpanan Member (key=id) dan penghitung frekuensi pinjam per buku/kategori.
 * - Exception       : BookNotFoundException, BookAlreadyBorrowedException, BorrowLimitExceededException.
 * - Assertion       : validasi data anggota sebelum transaksi (assert).
 * - Looping & kondisional efektif untuk pencarian dan analisis.
 */
public class LibraryService {

    private static final int BATAS_MAKS_PINJAM = 3; // konstanta primitive int

    // ===== Struktur data utama =====
    private ArrayList<Book> koleksiBuku;            // reference type: semua buku di perpustakaan
    private HashMap<String, Member> anggota;        // reference type: key = id anggota, value = object Member
    private ArrayList<String> riwayatTransaksi;     // reference type: log setiap transaksi (String)
    private HashMap<String, Integer> frekuensiPinjamBuku; // key = judul buku (lowercase), value = jumlah dipinjam

    public LibraryService() {
        this.koleksiBuku = new ArrayList<>();
        this.anggota = new HashMap<>();
        this.riwayatTransaksi = new ArrayList<>();
        this.frekuensiPinjamBuku = new HashMap<>();
    }

    // =========================================================
    // 1. MANAJEMEN DATA BUKU
    // =========================================================

    public void tambahBuku(Book book) {
        koleksiBuku.add(book);
    }

    public ArrayList<Book> getKoleksiBuku() {
        return koleksiBuku;
    }

    public void tambahAnggota(Member member) {
        anggota.put(member.getId(), member);
    }

    public HashMap<String, Member> getAnggota() {
        return anggota;
    }

    // =========================================================
    // 2. PENCARIAN & ANALISIS BUKU
    // =========================================================

    /**
     * Mencari buku berdasarkan judul ATAU kategori.
     * Wajib menggunakan toLowerCase() dan contains() (dilakukan di dalam
     * Book.cocokBerdasarkanKataKunci, lihat class Book).
     *
     * Looping: menggunakan for-each untuk menelusuri seluruh koleksi buku.
     */
    public ArrayList<Book> cariBuku(String kataKunci) {
        ArrayList<Book> hasil = new ArrayList<>();
        for (Book b : koleksiBuku) {           // looping efektif (for-each)
            if (b.cocokBerdasarkanKataKunci(kataKunci)) { // kondisional
                hasil.add(b);
            }
        }
        return hasil;
    }

    /**
     * Menghitung jumlah buku pada setiap kategori menggunakan looping + HashMap.
     */
    public HashMap<String, Integer> hitungJumlahPerKategori() {
        HashMap<String, Integer> hasil = new HashMap<>();
        for (Book b : koleksiBuku) {
            String kategori = b.getKategori();
            // Pola looping efektif: getOrDefault menghindari pengecekan containsKey manual
            int jumlahSaatIni = hasil.getOrDefault(kategori, 0);
            hasil.put(kategori, jumlahSaatIni + 1);
        }
        return hasil;
    }

    /**
     * Mencari object Book berdasarkan judul persis (dipakai internal saat transaksi).
     * Melempar BookNotFoundException (checked exception) jika tidak ditemukan.
     */
    private Book cariBukuPersisBerdasarkanJudul(String judul) throws BookNotFoundException {
        for (Book b : koleksiBuku) {
            // Manipulasi String: equalsIgnoreCase supaya pencarian tidak case-sensitive
            if (b.getJudul().equalsIgnoreCase(judul)) {
                return b;
            }
        }
        // Kondisi wajib #1: buku tidak ditemukan
        throw new BookNotFoundException("Buku dengan judul \"" + judul + "\" tidak ditemukan di koleksi.");
    }

    // =========================================================
    // 3 & 4. PROSES PEMINJAMAN & PENGEMBALIAN
    // =========================================================

    /**
     * Meminjam buku untuk seorang anggota.
     *
     * Assertion (assert) dipakai di sini untuk memvalidasi bahwa data anggota
     * benar-benar valid (id tidak kosong dan anggota memang terdaftar) SEBELUM
     * transaksi dilakukan. Assertion hanya aktif jika program dijalankan dengan
     * flag -ea; ini bukan pengganti exception handling untuk error yang memang
     * diharapkan terjadi (mis. input user salah), melainkan untuk menjaga
     * invariant/asumsi internal program.
     *
     * @throws BookNotFoundException          jika judul buku tidak ada di koleksi
     * @throws BookAlreadyBorrowedException   jika buku sedang berstatus dipinjam
     * @throws BorrowLimitExceededException   jika anggota sudah meminjam >= 3 buku
     */
    public void pinjamBuku(String idAnggota, String judulBuku)
            throws BookNotFoundException, BookAlreadyBorrowedException, BorrowLimitExceededException {

        Member member = anggota.get(idAnggota);

        // ===== ASSERTION: validasi data anggota sebelum transaksi =====
        assert idAnggota != null && !idAnggota.trim().isEmpty() : "ID anggota tidak boleh kosong";
        assert member != null : "Anggota dengan id '" + idAnggota + "' tidak terdaftar di sistem";

        // Validasi "normal" (bukan assumption internal) tetap pakai pengecekan biasa,
        // karena anggota tidak ditemukan adalah kondisi yang wajar bisa terjadi dari input user
        // ketika program dijalankan tanpa -ea (assert tidak aktif).
        if (member == null) {
            throw new BookNotFoundException("Anggota dengan id \"" + idAnggota + "\" tidak ditemukan.");
        }

        Book buku = cariBukuPersisBerdasarkanJudul(judulBuku); // bisa melempar BookNotFoundException

        // Kondisi wajib #2: buku sudah dipinjam
        if (!buku.isStatusKetersediaan()) {
            throw new BookAlreadyBorrowedException("Buku \"" + buku.getJudul() + "\" sedang dipinjam oleh anggota lain.");
        }

        // Kondisi wajib #3: anggota meminjam lebih dari batas maksimal (3 buku)
        if (member.jumlahBukuDipinjam() >= BATAS_MAKS_PINJAM) {
            throw new BorrowLimitExceededException(
                    "Anggota \"" + member.getNama() + "\" sudah meminjam " + BATAS_MAKS_PINJAM + " buku, tidak bisa meminjam lagi.");
        }

        // Semua validasi lolos -> proses peminjaman
        buku.setStatusKetersediaan(false);
        member.tambahPinjaman(buku);

        // Catat riwayat transaksi
        riwayatTransaksi.add("PINJAM|" + idAnggota + "|" + buku.getJudul());

        // Update frekuensi pinjam untuk analisis buku paling sering dipinjam
        String kunciJudul = buku.getJudul().toLowerCase(); // manipulasi String
        int frekuensiSaatIni = frekuensiPinjamBuku.getOrDefault(kunciJudul, 0);
        frekuensiPinjamBuku.put(kunciJudul, frekuensiSaatIni + 1);
    }

    /**
     * Mengembalikan buku yang dipinjam oleh seorang anggota.
     */
    public void kembalikanBuku(String idAnggota, String judulBuku) throws BookNotFoundException {
        Member member = anggota.get(idAnggota);

        // ===== ASSERTION: validasi data anggota sebelum transaksi pengembalian =====
        assert idAnggota != null && !idAnggota.trim().isEmpty() : "ID anggota tidak boleh kosong";
        assert member != null : "Anggota dengan id '" + idAnggota + "' tidak terdaftar di sistem";

        if (member == null) {
            throw new BookNotFoundException("Anggota dengan id \"" + idAnggota + "\" tidak ditemukan.");
        }

        Book buku = cariBukuPersisBerdasarkanJudul(judulBuku);

        // Kondisional: pastikan buku ini memang sedang dipinjam oleh anggota tsb
        if (!member.getDaftarPinjaman().contains(buku)) {
            throw new BookNotFoundException(
                    "Anggota \"" + member.getNama() + "\" tidak sedang meminjam buku \"" + buku.getJudul() + "\".");
        }

        buku.setStatusKetersediaan(true);
        member.hapusPinjaman(buku);
        riwayatTransaksi.add("KEMBALI|" + idAnggota + "|" + buku.getJudul());
    }

    // =========================================================
    // 5. ANALISIS AKTIVITAS / LAPORAN
    // =========================================================

    /**
     * Mencari judul buku yang paling sering dipinjam.
     * Looping efektif menelusuri entry HashMap untuk mencari nilai maksimum.
     */
    public String bukuPalingSeringDipinjam() {
        String judulTerpopuler = null;
        int frekuensiTertinggi = 0;

        for (Map.Entry<String, Integer> entry : frekuensiPinjamBuku.entrySet()) {
            if (entry.getValue() > frekuensiTertinggi) {
                frekuensiTertinggi = entry.getValue();
                judulTerpopuler = entry.getKey();
            }
        }

        if (judulTerpopuler == null) {
            return "Belum ada transaksi peminjaman.";
        }

        // Manipulasi Character: mengubah huruf pertama setiap kata jadi kapital (Title Case)
        // karena judulTerpopuler disimpan dalam bentuk lowercase di frekuensiPinjamBuku.
        String judulRapi = kapitalisasiSetiapKata(judulTerpopuler);

        return judulRapi + " (" + frekuensiTertinggi + " kali dipinjam)";
    }

    /**
     * Manipulasi Character #1: mengubah karakter pertama tiap kata menjadi
     * huruf besar menggunakan Character.toUpperCase() dan Character.isWhitespace().
     */
    private String kapitalisasiSetiapKata(String teks) {
        StringBuilder hasil = new StringBuilder();
        boolean awalKata = true;

        for (int i = 0; i < teks.length(); i++) {
            char c = teks.charAt(i); // manipulasi String -> charAt()

            if (awalKata && Character.isLetter(c)) {
                hasil.append(Character.toUpperCase(c)); // manipulasi Character
                awalKata = false;
            } else {
                hasil.append(c);
                if (Character.isWhitespace(c)) { // manipulasi Character
                    awalKata = true;
                }
            }
        }
        return hasil.toString();
    }

    /**
     * Anggota paling aktif = anggota dengan jumlah transaksi (pinjam) terbanyak
     * berdasarkan riwayatTransaksi.
     */
    public String anggotaPalingAktif() {
        HashMap<String, Integer> jumlahTransaksiPerAnggota = new HashMap<>();

        for (String log : riwayatTransaksi) {
            // Manipulasi String #2 (tambahan): split() untuk memecah log transaksi
            String[] bagian = log.split("\\|");
            if (bagian.length >= 2 && bagian[0].equals("PINJAM")) {
                String idAnggotaLog = bagian[1];
                int jumlahSaatIni = jumlahTransaksiPerAnggota.getOrDefault(idAnggotaLog, 0);
                jumlahTransaksiPerAnggota.put(idAnggotaLog, jumlahSaatIni + 1);
            }
        }

        String idPalingAktif = null;
        int jumlahTertinggi = 0;
        for (Map.Entry<String, Integer> entry : jumlahTransaksiPerAnggota.entrySet()) {
            if (entry.getValue() > jumlahTertinggi) {
                jumlahTertinggi = entry.getValue();
                idPalingAktif = entry.getKey();
            }
        }

        if (idPalingAktif == null) {
            return "Belum ada transaksi peminjaman.";
        }

        Member member = anggota.get(idPalingAktif);
        String nama = (member != null) ? member.getNama() : idPalingAktif;
        return nama + " (" + jumlahTertinggi + " kali meminjam)";
    }

    /**
     * Kategori paling populer = kategori dengan total peminjaman terbanyak
     * (dihitung dari riwayat transaksi PINJAM, dicocokkan ke kategori bukunya).
     */
    public String kategoriPalingPopuler() {
        HashMap<String, Integer> jumlahPerKategori = new HashMap<>();

        for (String log : riwayatTransaksi) {
            String[] bagian = log.split("\\|");
            if (bagian.length >= 3 && bagian[0].equals("PINJAM")) {
                String judulLog = bagian[2];
                for (Book b : koleksiBuku) {
                    if (b.getJudul().equalsIgnoreCase(judulLog)) {
                        String kategori = b.getKategori();
                        int jumlahSaatIni = jumlahPerKategori.getOrDefault(kategori, 0);
                        jumlahPerKategori.put(kategori, jumlahSaatIni + 1);
                        break; // sudah ketemu bukunya, hentikan loop pencarian ini
                    }
                }
            }
        }

        String kategoriTerpopuler = null;
        int jumlahTertinggi = 0;
        for (Map.Entry<String, Integer> entry : jumlahPerKategori.entrySet()) {
            if (entry.getValue() > jumlahTertinggi) {
                jumlahTertinggi = entry.getValue();
                kategoriTerpopuler = entry.getKey();
            }
        }

        return (kategoriTerpopuler == null) ? "Belum ada transaksi peminjaman." : kategoriTerpopuler;
    }

    public int getTotalTransaksiPinjam() {
        int total = 0;
        for (String log : riwayatTransaksi) {
            if (log.startsWith("PINJAM")) {
                total++;
            }
        }
        return total;
    }

    public int getBatasMaksPinjam() {
        return BATAS_MAKS_PINJAM;
    }
}
