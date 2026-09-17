import java.util.Scanner;

public class MainStudent {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Meminta jumlah siswa
        System.out.print("Masukkan jumlah siswa: ");
        int jumlahSiswa = scanner.nextInt();
        scanner.nextLine();

        // Array untuk menyimpan objek Student
        Student[] daftarSiswa = new Student[jumlahSiswa];

        // Input data setiap siswa
        for (int i = 0; i < jumlahSiswa; i++) {
            System.out.println("\nData siswa ke-" + (i + 1) + ":");

            System.out.print("Masukkan nama: ");
            String nama = scanner.nextLine();

            System.out.print("Masukkan nilai: ");
            double nilai = scanner.nextDouble();
            scanner.nextLine();

            // Membuat objek Student
            daftarSiswa[i] = new Student(nama, nilai);
        }

        // Mengecek kelulusan semua siswa
        for (int i = 0; i < daftarSiswa.length; i++) {
            daftarSiswa[i].checkPassed();
        }

        // Variabel untuk perhitungan
        double totalNilai = 0;
        int jumlahLulus = 0;
        int jumlahTidakLulus = 0;

        // Menampilkan data semua siswa
        System.out.println();

        for (int i = 0; i < daftarSiswa.length; i++) {
            Student siswa = daftarSiswa[i];

            String status;

            if (siswa.passed) {
                status = "Lulus";
            } else {
                status = "Tidak Lulus";
            }

            System.out.println(
                "Siswa: " + siswa.name +
                " - Nilai: " + siswa.score +
                " - " + status
            );

            totalNilai += siswa.score;

            if (siswa.passed) {
                jumlahLulus++;
            } else {
                jumlahTidakLulus++;
            }
        }

        // Menghitung rata-rata
        double rataRata = totalNilai / daftarSiswa.length;

        // Menampilkan hasil akhir
        System.out.println();
        System.out.printf("Rata-rata nilai kelas: %.2f%n", rataRata);
        System.out.println("Jumlah siswa lulus: " + jumlahLulus);
        System.out.println("Jumlah siswa tidak lulus: " + jumlahTidakLulus);

        scanner.close();
    }
}