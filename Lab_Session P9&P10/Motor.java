public class Motor extends Kendaraan {
    private String jenisMotor;

    public Motor(String namaKendaraan, double tarifHarian, String jenisMotor) {
        super(namaKendaraan, tarifHarian);
        setJenisMotor(jenisMotor);
    }

    public String getJenisMotor() {
        return jenisMotor;
    }

    public void setJenisMotor(String jenisMotor) {
        if (jenisMotor == null || jenisMotor.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis motor tidak boleh kosong.");
        }
        this.jenisMotor = jenisMotor;
    }

    @Override
    public double hitungBiaya(int lamaHari) {
        validasiLamaSewa(lamaHari);
        return getTarifHarian() * lamaHari;
    }

    @Override
    public void tampilkanInfoSewa(int lamaHari) {
        System.out.println("Jenis: Motor | Kendaraan: " + getNamaKendaraan()
                + " | Tipe: " + jenisMotor
                + " | Total: Rp" + hitungBiaya(lamaHari));
    }
}
