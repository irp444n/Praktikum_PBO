package library.exception;

/**
 * Custom exception #2.
 * Dilempar ketika seorang anggota mencoba meminjam buku lebih dari
 * batas maksimal yang diizinkan (3 buku).
 */
public class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
