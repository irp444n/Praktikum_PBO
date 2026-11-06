public class Main {
    public static void main(String[] args) {
        // Membuat empat objek dari dua class turunan.
        Mobil mobil1 = new Mobil("Toyota Avanza", 350000, 7);
        Mobil mobil2 = new Mobil("Honda Brio", 300000, 5);
        Motor motor1 = new Motor("Honda Vario", 100000, "Matic");
        Motor motor2 = new Motor("Yamaha NMAX", 150000, "Matic");

        System.out.println("=== SISTEM RENTAL KENDARAAN ===");

        // Contoh method overloading.
        System.out.println("\n1. Contoh pemanggilan method overloading");
        mobil1.sewa(2);
        motor1.sewa(3, true);

        // Runtime polymorphism: referensi superclass menyimpan objek subclass.
        System.out.println("\n2. Contoh runtime polymorphism");
        Kendaraan[] daftarKendaraan = { mobil1, mobil2, motor1, motor2 };

        for (Kendaraan kendaraan : daftarKendaraan) {
            kendaraan.tampilkanInfoSewa(2);
        }

        // Contoh penggunaan getter dan setter.
        System.out.println("\n3. Contoh encapsulation");
        System.out.println("Tarif awal " + mobil2.getNamaKendaraan()
                + ": Rp" + mobil2.getTarifHarian());

        mobil2.setTarifHarian(320000);
        System.out.println("Tarif setelah diubah: Rp" + mobil2.getTarifHarian());

        System.out.println("\nProgram selesai.");
    }
}
