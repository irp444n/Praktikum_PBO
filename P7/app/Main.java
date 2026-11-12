package app;

import model.Mahasiswa;
import model.NilaiInvalidException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String FILE_LAPORAN = "laporan_nilai.txt";

    public static void main(String[] args) {
        ArrayList<Mahasiswa> daftar = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            int jumlah = bacaJumlah(scanner);

            for (int i = 1; i <= jumlah; i++) {
                System.out.println("\n--- Mahasiswa ke-" + i + " ---");
                daftar.add(bacaMahasiswa(scanner));
            }
        }

        double total = 0;
        int jumlahLulus = 0;
        for (Mahasiswa mahasiswa : daftar) {
            total += mahasiswa.getNilai();
            if (mahasiswa.isLulus()) {
                jumlahLulus++;
            }
        }
        double rataRata = total / daftar.size();
        int jumlahTidakLulus = daftar.size() - jumlahLulus;

        tampilkan(daftar, rataRata, jumlahLulus, jumlahTidakLulus);
        simpan(daftar, rataRata, jumlahLulus, jumlahTidakLulus);
    }

    private static String bacaInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int bacaJumlah(Scanner scanner) {
        while (true) {
            try {
                int jumlah = Integer.parseInt(bacaInput(scanner, "Jumlah mahasiswa: "));
                if (jumlah > 0) {
                    return jumlah;
                }
                System.out.println("[Error] Jumlah harus lebih dari 0.");
            } catch (NumberFormatException e) {
                System.out.println("[Error] Masukkan bilangan bulat.");
            }
        }
    }

    private static Mahasiswa bacaMahasiswa(Scanner scanner) {
        while (true) {
            String nama = bacaInput(scanner, "Nama  : ");
            String nim = bacaInput(scanner, "NIM   : ");
            String inputNilai = bacaInput(scanner, "Nilai : ");
            try {
                double nilai = Double.parseDouble(inputNilai);
                return new Mahasiswa(nama, nim, nilai);
            } catch (NumberFormatException e) {
                System.out.println("[Error] Nilai harus berupa angka.");
            } catch (NilaiInvalidException | IllegalArgumentException | AssertionError e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }
    }

    private static void tampilkan(ArrayList<Mahasiswa> daftar, double rataRata,
                                  int jumlahLulus, int jumlahTidakLulus) {
        System.out.println("\n===== REKAP NILAI MAHASISWA =====");
        for (Mahasiswa mahasiswa : daftar) {
            System.out.println(mahasiswa);
        }
        System.out.printf("Jumlah: %d | Rata-rata: %.2f | Lulus: %d | Tidak lulus: %d%n",
                daftar.size(), rataRata, jumlahLulus, jumlahTidakLulus);
    }

    private static void simpan(ArrayList<Mahasiswa> daftar, double rataRata,
                               int jumlahLulus, int jumlahTidakLulus) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_LAPORAN))) {
            writer.write("===== LAPORAN NILAI MAHASISWA =====");
            writer.newLine();
            writer.write(String.format("%-12s %-25s %6s  %-2s %s", "NIM", "NAMA", "NILAI", "GR", "STATUS"));
            writer.newLine();
            for (Mahasiswa mahasiswa : daftar) {
                writer.write(mahasiswa.toString());
                writer.newLine();
            }
            writer.newLine();
            writer.write(String.format(
                    "Jumlah mahasiswa : %d%nRata-rata kelas  : %.2f%n"
                            + "Jumlah lulus     : %d%nJumlah tdk lulus : %d%n",
                    daftar.size(), rataRata, jumlahLulus, jumlahTidakLulus));
        } catch (IOException e) {
            System.out.println("[Error] Gagal menulis file: " + e.getMessage());
            return;
        }
        System.out.println("\nLaporan tersimpan di " + FILE_LAPORAN);
    }
}
