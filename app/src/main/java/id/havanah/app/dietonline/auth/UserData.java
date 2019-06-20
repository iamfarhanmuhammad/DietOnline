package id.havanah.app.dietonline.auth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import id.havanah.app.dietonline.app.AppController;
import id.havanah.app.dietonline.helper.SQLiteHandler;

/**
 * Created by farhan at 08:47
 * on 08/04/2019.
 * Havanah Team, ID.
 */
public class UserData {
    private String uid;
    private String username;
    private String email;
    private String name;
    private String nickname;
    private String city;
    private String subdistrict;
    private String address;
    private String phone;
    private String birth_date;
    private String date_birth;
    private String month_birth;
    private String year_birth;
    private String age;
    private String gender;
    private String weight;
    private String height;
    private String bmi;
    private String prohibition;
    private String created_at;
    private String updated_at;

    private SQLiteHandler db = new SQLiteHandler(AppController.getContext());
    private HashMap<String, String> user = db.getUserDetails();

    public String getUid() {
        return uid = user.get("uid");
    }

    public String getUsername() {
        return username = user.get("username");
    }

    public String getEmail() {
        return email = user.get("email");
    }

    public String getName() {
        return name = user.get("name");
    }

    public String getNickname() {
        return nickname = user.get("nickname");
    }

    public String getCity() {
        return city = user.get("city");
    }

    public String getSubdistrict() {
        return subdistrict = user.get("subdistrict");
    }

    public String getAddress() {
        return address = user.get("address");
    }

    public String getPhone() {
        return phone = user.get("phone");
    }

    public String getBirth_date() {
        return birth_date = user.get("birth_date");
    }

    public String getDate_birth() throws ParseException {
        String s = getBirth_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date d = sdf.parse(s);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int date = c.get(Calendar.DATE);
        return String.valueOf(date);
    }

    public String getMonth_birth() throws ParseException {
        String s = getBirth_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date d = sdf.parse(s);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int month = c.get(Calendar.MONTH) + 1;
        return String.valueOf(month);
    }

    public String getYear_birth() throws ParseException {
        String s = getBirth_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date d = sdf.parse(s);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public String getAge() throws ParseException {
        String s = getBirth_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date d = sdf.parse(s);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        int ageInt = age;
        return Integer.toString(ageInt);
    }

    public String getGender() {
        return gender = user.get("gender");
    }

    public String getWeight() {
        return weight = user.get("weight");
    }

    public String getHeight() {
        return height = user.get("height");
    }

    public String getBmi() {
        String h = getHeight();
        String w = getWeight();
        double height = Double.parseDouble(h) / 100;
        double weight = Double.parseDouble(w);
        double bmi = weight / ((height * height));
        return String.format(Locale.US, "%.2f", bmi);
    }

    public String getProhibition() {
        return prohibition = user.get("prohibition");
    }

    public String getCreated_at() {
        return created_at = user.get("created_at");
    }

    public String getUpdated_at() {
        return updated_at = user.get("updated_at");
    }

}
