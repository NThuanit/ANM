using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Numerics;
using System.IO;
using Microsoft.Win32;
using System.Security.Cryptography;
using Xceed.Words.NET;
using iText.Kernel.Pdf.Canvas.Parser;
using iText.Kernel.Pdf;
using iText.Kernel.Pdf.Canvas.Parser.Listener;
using iText.Layout;

namespace Elgamal
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            rd_td.IsChecked = true;
            btMaHoa.IsEnabled = false;
            btGiaiMa.IsEnabled = false;
        }

        #region code mã hóa elgamal
        // so nguyen to p de test 
        // 134078079299425970995740249982058461274793658205923933777235614437217640300735
        // 115792089237316195423570985008687907853269984665640564039457584007913129639747
        public BigInteger EsoP, EsoQ, E_So_G_A, EsoA, EsoX, EsoD, EsoK;
        public string SoC1, SoC2;
        public string pathSaveInfor = @"E:\University\fileInfor.txt";
        private bool isResetting = false;
        public int danhDau = 0;
        private void rd_tc_Checked(object sender, RoutedEventArgs e)
        {
            //So_A.IsEnabled = So_D.IsEnabled = So_K.IsEnabled = So_P.IsEnabled = So_X.IsEnabled = true;
            bt_taoKhoa.Content = "Tạo khóa tùy chọn";
            reset();
            bt_taoKhoa.IsEnabled = true;
            //So_A.Text = So_D.Text = so_K.Text = So_P.Text = So_X.Text = so_Y.Text = string.Empty;
        }

        private void rd_td_Checked(object sender, RoutedEventArgs e)
        {
            //So_A.IsEnabled = So_D.IsEnabled = So_K.IsEnabled = So_P.IsEnabled = So_X.IsEnabled = false;
            reset();
            bt_taoKhoa.Content = "Tạo khóa ngẫu nhiên";
            bt_taoKhoa.IsEnabled = true;
            // So_A.Text = So_D.Text = so_K.Text = So_P.Text = So_X.Text = so_Y.Text = string.Empty;
        }

        private void bt_taoKhoamoi_Click(object sender, RoutedEventArgs e)
        {
            reset();
            // So_A.Text = So_D.Text = so_K.Text = So_P.Text = So_X.Text = so_Y.Text = string.Empty;
        }

        private void reset()
        {
            isResetting = true; // Bắt đầu quá trình reset

            So_A.Text = So_D.Text = So_K.Text = So_P.Text = So_X.Text = txt_So_C1.Text = txt_So_C2.Text = string.Empty;
            txtBanRo.Text = txt_banGiaima.Text = txt_banMaHoaNhanDuoc.Text = txt_maHoaBanRo.Text = string.Empty;

            isResetting = false; // Kết thúc quá trình reset
        }
        private bool E_kiemTraPTSinh(BigInteger so_kt, BigInteger E_SoP_, BigInteger E_soQ_)
        {
            BigInteger soMu = E_SoP_ / E_soQ_;
            BigInteger ketQuaKT = E_LuyThuaModulo_(so_kt, soMu, E_SoP_);

            return ketQuaKT != 1;
        }

        // "Hàm kiểm tra hai số nguyên tố cùng nhau"
        private bool nguyenToCungNhau(BigInteger ai, BigInteger bi)
        {
            bool ktx_;
            // giải thuật Euclid;
            BigInteger temp;
            while (bi != 0)
            {
                temp = ai % bi;
                ai = bi;
                bi = temp;
            }
            if (ai == 1) { ktx_ = true; }
            else ktx_ = false;
            return ktx_;
        }
        // tính a^b mod n
        public BigInteger E_LuyThuaModulo_(BigInteger a, BigInteger b, BigInteger n)
        {
            //Sử dụng thuật toán "bình phương nhân"
            //Chuyển b sang hệ nhị phân
            BigInteger[] d = new BigInteger[1000];
            int k = 0;
            do
            {
                d[k] = b % 2;
                k++;
                b = b / 2;
            }
            while (b != 0);
            //Quá trình lấy dư
            BigInteger f = 1;
            for (int i = k - 1; i >= 0; i--)
            {
                f = (f * f) % n;
                if (d[i] == 1)
                    f = (f * a) % n;
            }
            return f;
        }
        //"Hàm kiểm tra nguyên tố dùng rabin-miller
        private bool RabinMillerTest(BigInteger n, int k)
        {
            if (n < 2)
                return false;
            if (n != 2 && n % 2 == 0)
                return false;

            BigInteger d = n - 1;
            int r = 0;

            while (d % 2 == 0)
            {
                d /= 2;
                r += 1;
            }
            Random rng = new Random();
            for (int i = 0; i < k; i++)
            {
                BigInteger a = RandomBigInteger(2, n - 2, rng);
                BigInteger x = BigInteger.ModPow(a, d, n);
                if (x == 1 || x == n - 1)
                    continue;

                bool continueLoop = false;
                for (int j = 0; j < r - 1; j++)
                {
                    x = BigInteger.ModPow(x, 2, n);
                    if (x == 1)
                        return false;
                    if (x == n - 1)
                    {
                        continueLoop = true;
                        break;
                    }
                }
                if (!continueLoop)
                    return false;
            }
            return true;
        }
        private BigInteger RandomBigInteger(BigInteger minValue, BigInteger maxValue, Random rng)
        {
            byte[] bytes = maxValue.ToByteArray();
            BigInteger value;

            do
            {
                rng.NextBytes(bytes);
                bytes[bytes.Length - 1] &= 0x7F; // Đảm bảo giá trị không âm
                value = new BigInteger(bytes);
            } while (value < minValue || value > maxValue);

            return value;
        }
        public bool E_kiemTraNguyenTo(BigInteger so_kt)
        {
            return RabinMillerTest(so_kt, 10);
        }

        public static bool E_kiemTraUocCuaSoP(BigInteger a, BigInteger b)
        {
            return a % b == 0;
        }
        // random so ngau nhien tu a den b
        public static BigInteger GenerateRandomBigInteger(BigInteger a, BigInteger b)
        {
            if (a > b)
                throw new ArgumentException("a must be less than or equal to b");
            using (RNGCryptoServiceProvider rng = new RNGCryptoServiceProvider())
            {
                BigInteger range = b - a + 1; // [a, b] inclusive
                int bitLength = range.ToByteArray().Length * 8;

                BigInteger randomNumber;
                do
                {
                    byte[] randomBytes = new byte[bitLength / 8 + 1];
                    rng.GetBytes(randomBytes);
                    randomBytes[randomBytes.Length - 1] &= 0x7F; // Đảm bảo giá trị không âm
                    randomNumber = new BigInteger(randomBytes);
                }
                while (randomNumber >= range || randomNumber < 0);

                return randomNumber + a;
            }
        }
        // random so ngau nhien tu 128 bit den 256 bit
        public static BigInteger RandomBigInteger_P()
        {
            using (RNGCryptoServiceProvider rng = new RNGCryptoServiceProvider())
            {
                Random rnd = new Random();
                int bitLength = rnd.Next(128, 257);
                int byteLength = (bitLength + 7) / 8;
                BigInteger randomNumber;
                do
                {
                    byte[] randomBytes = new byte[byteLength];
                    rng.GetBytes(randomBytes);
                    int excessBits = byteLength * 8 - bitLength;
                    randomBytes[randomBytes.Length - 1] &= (byte)(0xFF >> excessBits);

                    randomNumber = new BigInteger(randomBytes);
                }
                while (randomNumber < 0);
                return randomNumber;
            }
        }
        private BigInteger GenerateValidQ(BigInteger EsoP)
        {
            BigInteger EsoQ;
            do
            {
                EsoQ = GenerateRandomBigInteger(2, EsoP - 1);
            }
            while (!E_kiemTraNguyenTo(EsoQ));
            return EsoQ;
        }

        private BigInteger GenerateValidG(BigInteger EsoP, BigInteger EsoQ)
        {
            BigInteger E_So_G_A;
            do
            {
                E_So_G_A = GenerateRandomBigInteger(2, EsoP - 1);
            }
            while (!E_kiemTraPTSinh(E_So_G_A, EsoP, EsoQ));
            return E_So_G_A;
        }

        private BigInteger GenerateValidX(BigInteger EsoP, BigInteger EsoQ, BigInteger E_So_G_A)
        {
            BigInteger EsoX;
            do
            {
                EsoX = GenerateRandomBigInteger(2, EsoP - 2);
            }
            while (EsoX == EsoQ || EsoX == E_So_G_A);
            return EsoX;
        }

        private BigInteger GenerateValidK(BigInteger EsoP, BigInteger EsoX, BigInteger EsoQ, BigInteger E_So_G_A)
        {
            BigInteger EsoK;
            do
            {
                EsoK = GenerateRandomBigInteger(2, EsoP - 2);
            }
            while (EsoK == EsoX || EsoK == EsoA || EsoK == EsoQ || EsoK == E_So_G_A || !nguyenToCungNhau(EsoK, EsoP - 1));
            return EsoK;
        }
        private void TaoKhoa_click()
        {
            EsoQ = E_So_G_A = EsoA = EsoX = EsoD = EsoK = 0;
            EsoQ = GenerateValidQ(EsoP);
            E_So_G_A = GenerateValidG(EsoP, EsoQ);
            EsoA = E_LuyThuaModulo_(E_So_G_A, (EsoP - 1) / EsoQ, EsoP);
            EsoX = GenerateValidX(EsoP, EsoQ, E_So_G_A);
            EsoD = E_LuyThuaModulo_(EsoA, EsoX, EsoP);
            EsoK = GenerateValidK(EsoP, EsoX, EsoQ, E_So_G_A);
        }
        //luu ban ma
        private void btFile_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                // Kiểm tra xem có nội dung cần lưu không
                if (string.IsNullOrWhiteSpace(txt_maHoaBanRo.Text))
                {
                    MessageBox.Show("Lỗi bản mã", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }

                // Tạo SaveFileDialog để người dùng chọn định dạng và vị trí lưu file
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.Filter = "Text file (*.txt)|*.txt|DOCX file (*.docx)|*.docx";
                saveFileDialog.DefaultExt = "txt";

                bool? result = saveFileDialog.ShowDialog();

                if (result == true)
                {
                    // Lấy đường dẫn file được chọn
                    string path = saveFileDialog.FileName;

                    // Lưu nội dung dựa trên định dạng file
                    string extension = Path.GetExtension(path).ToLower();

                    switch (extension)
                    {
                        case ".txt":
                            SaveAsTextFile(path, txt_maHoaBanRo.Text);
                            break;
                        case ".docx":
                            SaveAsDocxFile(path, txt_maHoaBanRo.Text);
                            break;
                        default:
                            MessageBox.Show("Định dạng file không được hỗ trợ", "Lỗi", MessageBoxButton.OK, MessageBoxImage.Error);
                            return;
                    }

                    MessageBox.Show($"Lưu file thành công ở {path}", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Information);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Quá trình thất bại: {0}", ex.ToString());
            }
        }

        private void SaveAsTextFile(string path, string content)
        {
            using (StreamWriter sw = new StreamWriter(path))
            {
                sw.WriteLine(content);
            }
        }

        private void SaveAsDocxFile(string path, string content)
        {
            var doc = DocX.Create(path);
            doc.InsertParagraph(content);
            doc.Save();
        }
        public void SaveInfor()
        {
            string path = pathSaveInfor;
            try
            {
                if (File.Exists(path))
                {
                    File.Delete(path);
                }

                using (StreamWriter sw = new StreamWriter(path))
                {
                    sw.WriteLine("X: " + So_X.Text);
                    sw.WriteLine("P: " + So_P.Text);
                    sw.WriteLine("A: " + So_A.Text);
                    sw.WriteLine("D: " + So_D.Text);
                    sw.WriteLine("K: " + So_K.Text);
                    sw.WriteLine("C1: " + txt_So_C1.Text);
                    sw.WriteLine("C2: " + txt_So_C2.Text);
                    sw.WriteLine("Ban ro: " + txt_maHoaBanRo.Text);
                    sw.WriteLine("Ban ma: " + txt_banMaHoaNhanDuoc.Text);

                    //MessageBox.Show($"Lưu file thành công ở {pathSave}", "Thông Báo ", MessageBoxButton.OK, MessageBoxImage.Information);
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("The process failed: {0}", ex.ToString());
            }
        }
        // mo file cho ban ro
        private void btOpen_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Title = "Mở File";
            dlg.Filter = "Text file (*.txt)|*.txt|DOCX file (*.docx)|*.docx";
            dlg.DefaultExt = "txt";
            if (dlg.ShowDialog() == true)
            {
                string tmp = "";
                string path = dlg.FileName;
                string extension = System.IO.Path.GetExtension(path);
                if (string.IsNullOrEmpty(extension))
                {
                    MessageBox.Show("The path does not have a file name extension.", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                }
                else
                {
                    if (extension == ".txt")
                    {
                        using (StreamReader sr = new StreamReader(path))
                        {
                            while (sr.Peek() >= 0)
                            {
                                tmp += sr.ReadLine() + "\n"; // Đọc từng dòng và thêm vào tmp
                            }
                        }
                        txtBanRo.Text = tmp;
                    }
                    else if (extension == ".doc" || extension == ".docx")
                    {
                        try
                        {
                            using (DocX document = DocX.Load(path))
                            {
                                tmp = document.Text; // Lấy toàn bộ nội dung văn bản
                            }
                            txtBanRo.Text = tmp;
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Error reading the Word document: " + ex.Message, "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                        }
                    }
                    else
                    {
                        MessageBox.Show($"Vui lòng chọn file txt hoặc doc/docx {extension}", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                }
            }
        }


        // mo file cho ban ma
        private void btMoFile_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Title = "Mở File";
            dlg.Filter = "Text file (*.txt)|*.txt|DOCX file (*.docx)|*.docx";
            dlg.DefaultExt = "txt";
            if (dlg.ShowDialog() == true)
            {
                string tmp = "";
                string path = dlg.FileName;
                string extension = System.IO.Path.GetExtension(path);
                if (string.IsNullOrEmpty(extension))
                {
                    MessageBox.Show("The path does not have a file name extension.", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                }
                else
                {
                    if (extension == ".txt")
                    {
                        using (StreamReader sr = new StreamReader(path))
                        {
                            while (sr.Peek() >= 0)
                            {
                                tmp += sr.ReadLine();
                            }
                        }
                        txt_banMaHoaNhanDuoc.Text = tmp;
                        btGiaiMa.IsEnabled = true;
                    }
                    else if (extension == ".doc" || extension == ".docx")
                    {
                        try
                        {
                            using (DocX document = DocX.Load(path))
                            {
                                tmp = document.Text;
                            }
                            txt_banMaHoaNhanDuoc.Text = tmp;
                            btGiaiMa.IsEnabled = true;
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Error reading the Word document: " + ex.Message, "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                        }
                    }
                    else
                    {
                        MessageBox.Show("Vui lòng chọn file txt hoặc doc/docx", "Thông Báo", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                }
            }
        }
        private void btChuyen_Click(object sender, RoutedEventArgs e)
        {
            txt_banMaHoaNhanDuoc.Text = txt_maHoaBanRo.Text;
            btGiaiMa.IsEnabled = true;
            danhDau = 2;
        }

        private void text_changedC2(object sender, TextChangedEventArgs e)
        {
            if (!isResetting && danhDau != 1 && txt_So_C2.Text != SoC2)
            {
                MessageBox.Show("Nội dung trong C2 đã bị thay đổi!. Vui lòng khởi tạo khóa mới!", "Cảnh báo", MessageBoxButton.OK, MessageBoxImage.Warning);
                reset();
            }
        }

        private void text_changedC1(object sender, TextChangedEventArgs e)
        {
            if (!isResetting && danhDau != 1 && txt_So_C1.Text != SoC1)
            {
                MessageBox.Show("Nội dung trong C1 đã bị thay đổi!. Vui lòng khởi tạo khóa mới!", "Cảnh báo", MessageBoxButton.OK, MessageBoxImage.Warning);
                reset();
            }
        }
        private void bt_taoKhoa_Click(object sender, RoutedEventArgs e)
        {
            if (rd_td.IsChecked == true && rd_tc.IsChecked == false)
            {
                // thực hiện thao tác tạo khóa ngẫu nhiên 
                reset();

                // chọn số nguyên tố ngẫu nhiên P
                EsoP = 0;
                do
                {
                    EsoP = RandomBigInteger_P();
                }
                while (E_kiemTraNguyenTo(EsoP) == false);

                TaoKhoa_click();
                So_P.Text = EsoP.ToString();
                So_A.Text = EsoA.ToString();
                So_X.Text = EsoX.ToString();
                So_D.Text = EsoD.ToString();
                So_K.Text = EsoK.ToString();

                bt_taoKhoa.Content = "Tạo khóa ngẫu nhiên mới";

            }
            else
            {
                if (rd_td.IsChecked == false && rd_tc.IsChecked == true)//(rd_tudongchon_.Checked == false && rd_tuychon_.Checked == true)
                {
                    // thực hiện thao tác tạo khóa tùy chọn 
                    if (So_P.Text == "")
                    {
                        MessageBox.Show("Phải nhập số P ", "Thông Báo ", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                    else
                    {
                        EsoP = BigInteger.Parse(So_P.Text);
                        if (E_kiemTraNguyenTo(EsoP) == false)
                        {
                            MessageBox.Show("Phải chọn P là số nguyên tố ", "Thông Báo ", MessageBoxButton.OK, MessageBoxImage.Error);
                            So_P.Focus();
                        }
                        else
                            if (EsoP < 1000000000)
                        {
                            MessageBox.Show("Số P quá nhỏ! Nhập số khác ", "Thông Báo ", MessageBoxButton.OK, MessageBoxImage.Error);
                            So_P.Focus();
                        }
                        else
                        {
                            TaoKhoa_click();
                            So_P.Text = EsoP.ToString();
                            So_A.Text = EsoA.ToString();
                            So_X.Text = EsoX.ToString();
                            So_D.Text = EsoD.ToString();
                            So_K.Text = EsoK.ToString();
                            bt_taoKhoa.IsEnabled = false;
                            //bt_taokhoaTuychonMoi.Visible = true;
                        }
                    }
                }
            }
            danhDau = 1;
            btMaHoa.IsEnabled = true;
            btGiaiMa.IsEnabled = false;
        }


        public string E_MaHoa(string ChuoiVao)
        {
            //Chuyen xau thanh ma Unicode         
            byte[] mhE_temp1 = Encoding.Unicode.GetBytes(ChuoiVao);
            string base64 = Convert.ToBase64String(mhE_temp1);
            // Chuyển xâu thành mã Unicode dạng số          
            BigInteger[] mh_temp2 = new BigInteger[base64.Length];
            for (int i = 0; i < base64.Length; i++)
            {
                mh_temp2[i] = (int)base64[i];
            }
            //Mảng a chứa các kí tự sẽ  mã hóa
            BigInteger[] C1 = new BigInteger[mh_temp2.Length];
            BigInteger[] C2 = new BigInteger[mh_temp2.Length];
            // thực hiện mã hóa: z = (d^k * m ) mod p
            string so_c1 = E_LuyThuaModulo_(EsoA, EsoK, EsoP).ToString();
            string so_c2 = "";
            for (int i = 0; i < mh_temp2.Length; i++)
            {

                C1[i] = E_LuyThuaModulo_(EsoA, EsoK, EsoP);
                C2[i] = ((mh_temp2[i] % EsoP) * (E_LuyThuaModulo_(EsoD, EsoK, EsoP))) % EsoP;
                so_c2 += C2[i].ToString() + "\n";
            }
            //Hiển thị số C1 và số C2
            txt_So_C1.Text = so_c1.Trim();
            txt_So_C2.Text = so_c2.Trim();
            //luu C1 va C2
            SoC1 = so_c1.Trim();
            SoC2 = so_c2.Trim();
            //Chuyển sang kiểu kí tự trong bảng mã Unicode
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < C2.Length; i++)
            {
                str.Append(C1[i]).Append(",").Append(C2[i]).Append(";");
            }
            // Mã hóa chuỗi kết quả thành Base-64
            byte[] E_data1 = Encoding.Unicode.GetBytes(str.ToString());
            string BanMaHoa = Convert.ToBase64String(E_data1);
            return BanMaHoa;
            //Chuỗi mã hóa dài bởi vì ban đầu chuyển bản rõ -> chuỗi base64 sau đó mới mã hóa
        }
        public string E_GiaiMa(string ChuoiMaHoa)
        {
            // Bước 1: Giải mã chuỗi Base64 thành chuỗi Unicode
            byte[] E_data1 = Convert.FromBase64String(ChuoiMaHoa);
            string Egm_giaima = Encoding.Unicode.GetString(E_data1);
            // Tách C1 và C2 từ chuỗi
            string[] pairs = Egm_giaima.Split(';');
            BigInteger[] C1 = new BigInteger[pairs.Length - 1];
            BigInteger[] C2 = new BigInteger[pairs.Length - 1];
            for (int i = 0; i < pairs.Length - 1; i++)
            {
                string[] pair = pairs[i].Split(',');
                C1[i] = BigInteger.Parse(pair[0]);
                C2[i] = BigInteger.Parse(pair[1]);
            }

            BigInteger[] M = new BigInteger[C1.Length];
            for (int i = 0; i < C1.Length; i++)
            {
                BigInteger s = E_LuyThuaModulo_(C1[i], (EsoP - 1 - EsoX), EsoP);
                M[i] = (C2[i] * s) % EsoP;
            }
            // Chuyển đổi từ mảng số sang chuỗi
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < M.Length; i++)
            {
                str.Append((char)M[i]);
            }

            // Chuyển chuỗi Unicode thành chuỗi gốc
            byte[] data2 = Convert.FromBase64String(str.ToString());
            string BanGiaiMa = Encoding.Unicode.GetString(data2);
            return BanGiaiMa;

        }
        private void btMaHoa_Click(object sender, RoutedEventArgs e)
        {
            if (danhDau != 1)
            {
                MessageBox.Show("Bạn chưa chọn khóa!", "Thông báo", MessageBoxButton.OK, MessageBoxImage.Error);
            }
            else
            {
                if (txtBanRo.Text == string.Empty)
                {
                    MessageBox.Show("Bạn chưa nhập chuỗi cần mã hóa! Hoặc lựa chọn mở file mã hóa", "Thông báo", MessageBoxButton.OK, MessageBoxImage.Error);

                }
                else
                {
                    if (danhDau == 1)
                    {
                        txt_maHoaBanRo.Text = string.Empty;

                        string Egm_chuoiMaHoa = E_MaHoa(txtBanRo.Text);
                        txt_maHoaBanRo.Text = Egm_chuoiMaHoa;
                        danhDau = 2;
                        btMaHoa.IsEnabled = false;
                        btOpen.IsEnabled = false;
                    }
                }
            }
        }
        private void btGiaiMa_Click(object sender, RoutedEventArgs e)
        {
            if (txt_banMaHoaNhanDuoc.Text == txt_maHoaBanRo.Text)
            {
                txt_banGiaima.Text = string.Empty;
                txt_banGiaima.Text = E_GiaiMa(txt_banMaHoaNhanDuoc.Text);
                danhDau = 1;
                btMaHoa.IsEnabled = false;
                btGiaiMa.IsEnabled = true;
            }
            else
            {
                MessageBox.Show("Bản mã nhận được khác với bản mã gốc. Vui lòng nhập lại!", "Thông báo!");
                danhDau = 2;
            }

            SaveInfor();
        }

        private void btThoat_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void btTaoBanRoMoi_Click(object sender, RoutedEventArgs e)
        {
            reset();
            btMaHoa.IsEnabled = false;
        }

        #endregion
    }
}