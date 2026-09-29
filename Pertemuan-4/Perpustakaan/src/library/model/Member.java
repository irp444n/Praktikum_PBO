package library.model;

import java.util.ArrayList;

/**
 * Class Member merepresentasikan anggota perpustakaan.
 *
 * Konsep OOP yang didemonstrasikan:
 * - Class & Object   : Member adalah class, setiap anggota adalah object.
 * - Variabel         : id, nama, daftarPinjaman.
 * - Constructor      : Member(id, nama).
 * - Tipe data reference: ArrayList<Book> daftarPinjaman (koleksi object Book yang sedang dipinjam).
 */
public class Member {

    // ===== Variabel (field) instance =====
    private String id;                        // reference type
    private String nama;                      // reference type
    private ArrayList<Book> daftarPinjaman;    // reference type -> koleksi buku yang sedang dipinjam anggota ini

    // ===== Constructor =====
    public Member(String id, String nama) {
        this.id = id;
        this.nama = nama;
        this.daftarPinjaman = new ArrayList<>(); // saat anggota baru dibuat, daftar pinjaman kosong
    }

    // ===== Getter & Setter =====
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public ArrayList<Book> getDaftarPinjaman() {
        return daftarPinjaman;
    }

    /**
     * Menambahkan buku ke daftar pinjaman anggota ini.
     * Batas maksimal peminjaman (3 buku) divalidasi di LibraryService,
     * bukan di sini, supaya class Member tetap fokus menyimpan state saja.
     */
    public void tambahPinjaman(Book book) {
        daftarPinjaman.add(book);
    }

    public void hapusPinjaman(Book book) {
        daftarPinjaman.remove(book);
    }

    public int jumlahBukuDipinjam() {
        return daftarPinjaman.size(); // tipe primitive int sebagai hasil
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - sedang meminjam %d buku", id, nama, jumlahBukuDipinjam());
    }
}
