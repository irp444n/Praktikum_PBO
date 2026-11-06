public class Mobil extends Kendaraan {
    private int kapasitasPenumpang;

    public Mobil(String namaKendaraan, double tarifHarian, int kapasitasPenumpang) {
        super(namaKendaraan, tarifHarian);
        setKapasitasPenumpang(kapasitasPenumpang);
    }

    public int getKapasitasPenumpang() {
        return kapasitasPenumpang;
    }

    public void setKapasitasPenumpang(int kapasitasPenumpang) {
        if (kapasitasPenumpang <= 0) {
            throw new IllegalArgumentException("Kapasitas penumpang harus lebih dari 0.");
        }
        this.kapasitasPenumpang = kapasitasPenumpang;
    }

    @Override
    public double hitungBiaya(int lamaHari) {
        validasiLamaSewa(lamaHari);
        return getTarifHarian() * lamaHari;
    }

    @Override
    public void tampilkanInfoSewa(int lamaHari) {
        System.out.println("Jenis: Mobil | Kendaraan: " + getNamaKendaraan()
                + " | Kapasitas: " + kapasitasPenumpang + " orang"
                + " | Total: Rp" + hitungBiaya(lamaHari));
    }
}
