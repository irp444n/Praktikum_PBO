import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class LaporanNilai {

    static final String NAMA_FILE = "laporan_nilai.txt";
    static final double BATAS_LULUS = 70.0;

    // Baca bilangan bulat > 0, ulangi sampai valid
    static int bacaJumlah(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n > 0) {
                    return n;
                }
                System.out.println("Jumlah harus lebih dari 0.");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa bilangan bulat.");
            }
        }
    }

    // Baca nilai 0-100, menerima koma maupun titik sebagai pemisah desimal
    static double bacaNilai(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double nilai = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                if (nilai >= 0 && nilai <= 100) {
                    return nilai;
                }
                System.out.println("Nilai harus di antara 0 dan 100.");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    // Baca nama, tidak boleh kosong
    static String bacaNama(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String nama = sc.nextLine().trim();
            if (!nama.isEmpty()) {
                return nama;
            }
            System.out.println("Nama tidak boleh kosong.");
        }
    }

    static String tentukanStatus(double nilai) {
        return nilai >= BATAS_LULUS ? "Lulus" : "Tidak Lulus";
    }

    // Langkah 1: input konsol + tulis ke file
    static void tulisLaporan(Scanner sc, int jumlah) throws IOException {
        double total = 0;
        int lulus = 0;
        int tidakLulus = 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NAMA_FILE))) {
            bw.write("=== LAPORAN NILAI SISWA ===");
            bw.newLine();

            for (int i = 1; i <= jumlah; i++) {
                System.out.println("\nSiswa ke-" + i);
                String nama = bacaNama(sc, "Nama  : ");
                double nilai = bacaNilai(sc, "Nilai : ");
                String status = tentukanStatus(nilai);

                total += nilai;
                if (nilai >= BATAS_LULUS) {
                    lulus++;
                } else {
                    tidakLulus++;
                }

                bw.write(String.format(Locale.US, "%d. %s | Nilai: %.2f | Status: %s",
                        i, nama, nilai, status));
                bw.newLine();
            }

            bw.write("---------------------------");
            bw.newLine();
            bw.write(String.format(Locale.US, "Rata-rata nilai kelas : %.2f", total / jumlah));
            bw.newLine();
            bw.write("Jumlah siswa lulus    : " + lulus);
            bw.newLine();
            bw.write("Jumlah tidak lulus    : " + tidakLulus);
            bw.newLine();
        } // bw otomatis ditutup (flush + close)
    }

    // Langkah 2: baca file baris demi baris
    static void bacaLaporan() throws IOException {
        System.out.println("\n===== ISI FILE " + NAMA_FILE + " =====");
        try (BufferedReader br = new BufferedReader(new FileReader(NAMA_FILE))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                System.out.println(baris);
            }
        } // br otomatis ditutup
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int jumlah = bacaJumlah(sc, "Masukkan jumlah siswa: ");
            tulisLaporan(sc, jumlah);
            bacaLaporan();
        } catch (IOException e) { // Langkah 3: exception handling
            System.err.println("Terjadi kesalahan file: " + e.getMessage());
        }
    }
}