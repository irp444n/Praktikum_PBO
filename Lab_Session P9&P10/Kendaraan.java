public abstract class Kendaraan implements DapatDisewa {
    private String namaKendaraan;
    private double tarifHarian;

    public Kendaraan(String namaKendaraan, double tarifHarian) {
        this.namaKendaraan = namaKendaraan;
        setTarifHarian(tarifHarian);
    }

    // Getter dan setter untuk menerapkan encapsulation.
    public String getNamaKendaraan() {
        return namaKendaraan;
    }

    public void setNamaKendaraan(String namaKendaraan) {
        if (namaKendaraan == null || namaKendaraan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama kendaraan tidak boleh kosong.");
        }
        this.namaKendaraan = namaKendaraan;
    }

    public double getTarifHarian() {
        return tarifHarian;
    }

    public void setTarifHarian(double tarifHarian) {
        if (tarifHarian <= 0) {
            throw new IllegalArgumentException("Tarif harian harus lebih dari 0.");
        }
        this.tarifHarian = tarifHarian;
    }

    // Abstract method: perhitungan biaya ditentukan oleh subclass.
    public abstract double hitungBiaya(int lamaHari);

    // Method overloading: nama sama, parameter berbeda.
    public void sewa(int lamaHari) {
        validasiLamaSewa(lamaHari);
        System.out.println("Biaya sewa " + namaKendaraan + " selama "
                + lamaHari + " hari: Rp" + hitungBiaya(lamaHari));
    }

    public void sewa(int lamaHari, boolean menggunakanAsuransi) {
        validasiLamaSewa(lamaHari);

        double biaya = hitungBiaya(lamaHari);
        if (menggunakanAsuransi) {
            biaya += 25000 * lamaHari;
        }

        System.out.println("Biaya sewa " + namaKendaraan + " selama "
                + lamaHari + " hari"
                + (menggunakanAsuransi ? " dengan asuransi" : " tanpa asuransi")
                + ": Rp" + biaya);
    }

    protected void validasiLamaSewa(int lamaHari) {
        if (lamaHari <= 0) {
            throw new IllegalArgumentException("Lama sewa harus minimal 1 hari.");
        }
    }
}
