package model;

public class Mahasiswa {
    private static final double FINAL_PASSING_GRADE = 70.0;

    private final String nama;
    private final String nim;
    private final double nilai;

    public Mahasiswa(String nama, String nim, double nilai) throws NilaiInvalidException {
        String namaBersih = nama.trim().toUpperCase();
        String nimBersih = nim.trim();

        // Assertion aktif saat program dijalankan dengan opsi -ea.
        assert !nimBersih.isEmpty() : "NIM tidak boleh kosong!";

        // Tetap validasi NIM jika assertion tidak diaktifkan.
        if (nimBersih.isEmpty()) {
            throw new IllegalArgumentException("NIM tidak boleh kosong!");
        }
        if (namaBersih.isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        for (char c : nimBersih.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("NIM hanya boleh berisi angka!");
            }
        }
        if (!Double.isFinite(nilai) || nilai < 0 || nilai > 100) {
            throw new NilaiInvalidException("Nilai harus 0-100, input: " + nilai);
        }

        this.nama = namaBersih;
        this.nim = nimBersih;
        this.nilai = nilai;
    }

    public double getNilai() {
        return nilai;
    }

    private String getGrade() {
        if (nilai >= 85) {
            return "A";
        } else if (nilai >= 75) {
            return "B";
        } else if (nilai >= FINAL_PASSING_GRADE) {
            return "C";
        } else {
            return "D";
        }
    }

    public boolean isLulus() {
        return nilai >= FINAL_PASSING_GRADE;
    }

    @Override
    public String toString() {
        return String.format("%-12s %-25s %6.2f  %-2s %s",
                nim, nama, nilai, getGrade(), isLulus() ? "LULUS" : "TIDAK LULUS");
    }
}
