package smartext.com.smscapture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alonm on 7/17/15.
 */
public class NotificationResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MessageDialog.Builder().addReportButton(false).setMessage("Want to have a coupon??!").setReportTitle("Coupon").show(this);

    }
}
