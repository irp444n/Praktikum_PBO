package library.exception;

/**
 * Custom exception #1.
 * Dilempar ketika buku yang dicari/dipinjam/dikembalikan tidak ditemukan
 * di dalam koleksi perpustakaan.
 *
 * Meng-extend Exception (checked exception) sehingga method yang melempar
 * exception ini WAJIB mendeklarasikan "throws" dan pemanggilnya wajib
 * menangani (try-catch) atau meneruskannya.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
