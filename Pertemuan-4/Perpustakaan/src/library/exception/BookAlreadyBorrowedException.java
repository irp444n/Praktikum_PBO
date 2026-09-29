package library.exception;

/**
 * Custom exception #3 (tambahan, melengkapi 3 kondisi wajib pada soal:
 * buku tidak ditemukan, buku sudah dipinjam, dan anggota melebihi batas pinjam).
 *
 * Dilempar ketika buku yang ingin dipinjam ternyata statusnya sudah "Dipinjam".
 */
public class BookAlreadyBorrowedException extends Exception {
    public BookAlreadyBorrowedException(String message) {
        super(message);
    }
}
