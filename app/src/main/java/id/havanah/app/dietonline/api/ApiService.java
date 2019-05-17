package id.havanah.app.dietonline.api;

/**
 * Created by farhan at 23:54
 * on 30/03/2019.
 * Havanah Team, ID.
 */
public class ApiService {
    private static String base_url = "https://dion.co.id/api";
//    private static String base_url = "http://192.168.10.16/dion/api";
    public static String auth = base_url + "/auth";
    public static String login = base_url + "/auth/login.php";
    public static String register = base_url + "/auth/register.php";
    public static String updatePersonal = base_url + "/auth/update_personal.php";
    public static String updateAccount = base_url + "/auth/update_account.php";
    public static String updateMedical = base_url + "/auth/update_medical.php";
    public static String createTransaction = base_url + "/transaction/post.php";
    public static String createTransactionWeightLoss = base_url + "/transaction/post_diet_mayo.php";
    public static String createTransactionSpecialPackage = base_url + "/transaction/post_diet_khusus.php";
    public static String updatePaid = base_url + "/transaction/update_paid.php";
    public static String updateDone = base_url + "/transaction/update_done.php";
    public static String fetchTransactionData = base_url + "/transaction/fetch_data.php";
    public static String fetchTransactionAmount = base_url + "/transaction/fetch_amount.php";
    public static String fetchByInvoice = base_url + "/transaction/fetch_by_invoice.php";
    public static String demo = base_url + "/demo";
}
