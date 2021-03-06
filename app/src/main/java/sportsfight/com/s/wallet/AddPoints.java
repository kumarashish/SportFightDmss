package sportsfight.com.s.wallet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.sql.BatchUpdateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import sportsfight.com.s.R;
import sportsfight.com.s.adapter.DashboardItemAdapter;
import sportsfight.com.s.common.AppController;
import sportsfight.com.s.common.Common;
import sportsfight.com.s.interfaces.WebApiResponseCallback;
import sportsfight.com.s.launchingmodule.PaymentGateway;
import sportsfight.com.s.mainmodule.Dashboard;
import sportsfight.com.s.mainmodule.Profile;
import sportsfight.com.s.model.TransactionModel;
import sportsfight.com.s.util.Contants;
import sportsfight.com.s.util.Util;

/**
 * Created by Ashish.Kumar on 07-02-2018.
 */

public class AddPoints extends Activity implements View.OnClickListener,WebApiResponseCallback {
    AppController controller;
    @BindView(R.id.back)
    Button back;
    @BindView(R.id.addPoints_Edt)
    EditText points_Edt;
    @BindView(R.id.currentPoints)
    TextView points_Tv;
    @BindView(R.id.amountPayable)
    TextView amountPayable;
    @BindView(R.id.pay)
    Button pay;
    int apiCall=0;
    int AddPoints=1,getAcessToken=2,getCheckSum=3,getTranactionCheckSum=4,validateTransaction=5;
    Dialog dialog;
    String instamojoAcessToken="";
    String orderId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_points);
        controller=(AppController)getApplicationContext();
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
        points_Tv.setText(Integer.toString(controller.getProfile().getTotalPoints()));
        amountPayable.setText("");
        points_Edt.setTypeface(controller.getDetailsFont());
        pay.setTypeface(controller.getDetailsFont());
        instamojoAcessToken=controller.getPaymentGatewayToken();
        points_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 1) {
                    amountPayable.setText("Total Amount payable : Rs."+Integer.parseInt(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public boolean isEditTextValidated() {
        if (points_Edt.getText().length() > 0) {
            int points = Integer.parseInt(points_Edt.getText().toString());
            if (points < 10) {
                Toast.makeText(this, "Coins should be greater than 10", Toast.LENGTH_SHORT).show();
            } else {
                if (points % 10 != 0) {
                    Toast.makeText(this, "Coins should be multiple of 10", Toast.LENGTH_SHORT).show();
                } else {
                    if (points > 10000) {

                        Toast.makeText(this, "Coins should be less than 10001", Toast.LENGTH_SHORT).show();

                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                onBackPressed();
                break;
            case R.id.pay:
                if(isEditTextValidated()==true) {
//                    if (instamojoAcessToken != null) {
//                            callOrder();
//
//                    } else {
//                        if (Util.isNetworkAvailable(AddPoints.this)) {
//                            dialog = Util.showPogress(AddPoints.this);
//                            //controller.getApiCall().postData(Common.getAddPoints_Url,getAddPointJSON().toString(),this);
//                            apiCall = getAcessToken;
//                            controller.getApiCall().getAcessToken(Common.paymentGatewayUrlLive, this);
//                        }
//                    }
                    generateChecksum(view);
                }

                break;

        }

    }

    public void callUpdatePayment(final String orderId, final String transactionId, final String paymentId) {
        if (Util.isNetworkAvailable(AddPoints.this)) {

            dialog = Util.showPogress(AddPoints.this);
            {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String paymentStatus = controller.getApiCall().isPaymentValid(Common.getPaymentStatusUrl(paymentId), instamojoAcessToken);
                        if (isPaymentSucess(paymentStatus) == true) {
                            UpdatePoints(orderId, transactionId, paymentId,true);
                        } else {
                            Util.cancelDialog(AddPoints.this, dialog);
                        }
                    }
                });
                t.start();
            }
        }
    }

    public boolean isPaymentSucess(String response) {
        boolean status = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getBoolean("status");
            if (status == false) {
                JSONObject failure = jsonObject.getJSONObject("failure");
                Util.showToast(AddPoints.this, failure.getString("message"));
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return status;
    }

    public void UpdatePoints(final String orderId, final String transactionId, final String paymentId, final boolean paymentStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = Util.showPogress(AddPoints.this);
                controller.getApiCall().postData(Common.getAddPoints_Url, getAddPointJSON(orderId, transactionId, paymentId,paymentStatus).toString(),controller.getPrefManager().getUserToken(), AddPoints.this);
                apiCall = AddPoints;
            }
        });


    }
    @Override
    public void onSucess(String value) {
        if (dialog != null) {
            dialog.cancel();
        }
        if (apiCall == getAcessToken) {
            if (!value.contains("null") || (value != null)) {
                getAcessToken(value);
            }
        } else if (apiCall == getCheckSum) {
            if (value.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    final String checksum = jsonObject.getString("CHECKSUMHASH");
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StartTransaction(checksum);
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        } else if (apiCall == getTranactionCheckSum) {
            if (value.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    final String checksum = jsonObject.getString("CHECKSUMHASH");
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                validateTransaction(checksum);
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        } else if (apiCall == validateTransaction) {

            if (value.length() > 0) {
                try {
                    TransactionModel model=new TransactionModel(value);
                    UpdatePoints(model.getORDERID(),model.getTXNID(),model.getBANKTXNID(),true);

                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        }  else if(apiCall==AddPoints){
            Util.showToast(AddPoints.this, Util.getMessage(value));
            if (Util.getStatus(value) == true) {
                controller.setPoints(controller.getProfile().getTotalPoints() + Integer.parseInt(points_Edt.getText().toString()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        points_Edt.setText("");
                        amountPayable.setText("");
                        points_Tv.setText(Integer.toString(controller.getProfile().getTotalPoints()));
                    }
                });
            }
        }
    }

    @Override
    public void onError(String value) {
        if(dialog!=null)
        {
            dialog.cancel();
        }
        if (Util.isSessionExpired(value)) {
            controller.logout();
            Util.Logout(AddPoints.this);
        }
        Util.showToast(AddPoints.this,Util.getMessage(value));
    }

    public JSONObject getAddPointJSON(String orderID, String transactionId, String paymentId,boolean paymentStatus) {
        JSONObject jsonObject = new JSONObject();
        int amount = Integer.parseInt(points_Edt.getText().toString()) / 10;
        try {
            jsonObject.put("RegistrationId", controller.getProfile().getUserId());
            jsonObject.put("TransactionId", transactionId);
            jsonObject.put("Amount", amount);
            jsonObject.put("TransactionTime", Util.getCurrentDate());
            jsonObject.put("OrderId", orderID);
            jsonObject.put("PaymentId", paymentId);
            jsonObject.put("DeviceId", Util.getDeviceID(AddPoints.this));
            jsonObject.put("PaymentStatus", paymentStatus);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }




    /************paytm******************************************/


    private void initOrderId() {
        long r = System.currentTimeMillis();
        orderId = "SportsFight" + r;
    }

    public void generateChecksum(View view)
    {   initOrderId();
        apiCall=getCheckSum;
        dialog = Util.showPogress(this);
        controller.getApiCall().getCheckSum(Common.BaseUrl1+"paytm/GenerateChecksum.aspx",orderId,Integer.toString(controller.getProfile().getUserId()),points_Edt.getText().toString(),this);
    }

    public void getTransactionCheckSum() {
        apiCall = getTranactionCheckSum;
        dialog = Util.showPogress(this);
        controller.getApiCall().getCheckSum(Common.BaseUrl1+"paytm/GenerateChecksumforTransactions.aspx", orderId,Integer.toString(controller.getProfile().getUserId()),points_Edt.getText().toString(), this);
    }

    public void validateTransaction(String checksum) {
        apiCall = validateTransaction;
        dialog = Util.showPogress(this);
        controller.getApiCall().postData(Common.validateTransactionUrl, validateTransactionJson(checksum).toString(), "", this);
    }


    public JSONObject validateTransactionJson(String checkSum) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("ORDERID", orderId);
            jsonObject.put("MID", Contants.MID);
            jsonObject.put("CHECKSUMHASH", checkSum);

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return jsonObject;
    }

    public  void getTransactionStatus(String orderId,String checksum)
    {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID",  Contants.MID);
        paramMap.put("ORDER_ID",  orderId);


    }
    public void StartTransaction(String checksum) {
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();
        // these are mandatory parameters
        paramMap.put("CALLBACK_URL", Contants.CALLBACKURL+orderId);
        paramMap.put("CHANNEL_ID", Contants.CHANNEL_ID);
        paramMap.put("CHECKSUMHASH", checksum);
        paramMap.put("CUST_ID",Integer.toString( controller.getProfile().getUserId()));
        paramMap.put("INDUSTRY_TYPE_ID", Contants.INDUSTRY_TYPE_ID);
        paramMap.put("MID",  Contants.MID);
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("TXN_AMOUNT",points_Edt.getText().toString());
        paramMap.put("WEBSITE",  Contants.WEBSITE);

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        String message=inResponse.getString("RESPMSG");
                        String code=inResponse.getString("RESPCODE");
                        if (code.equalsIgnoreCase("01")) {
                            getTransactionCheckSum();
                        } else {
                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {


                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(AddPoints.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();

                    }

                });
    }

//    @Override
//    public void onSucess(String value) {
//        if (dialog != null) {
//            dialog.cancel();
//        }
//
//        if (apiCall == getCheckSum) {
//            if (value.length() > 0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(value);
//                    final String checksum = jsonObject.getString("CHECKSUMHASH");
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                StartTransaction(checksum);
//                            }
//                        });
//                    }
//                } catch (Exception ex) {
//                    ex.fillInStackTrace();
//                }
//            }
//        } else if (apiCall == getTranactionCheckSum) {
//            if (value.length() > 0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(value);
//                    final String checksum = jsonObject.getString("CHECKSUMHASH");
//                    {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                validateTransaction(checksum);
//                            }
//                        });
//                    }
//                } catch (Exception ex) {
//                    ex.fillInStackTrace();
//                }
//            }
//        }
//        else{
//            if(value.length()>0)
//            {
//                TransactionModel model=new TransactionModel(value);
//                Util.showToast(AddPoints.this,model.getRESPMSG());
//            }
//
//        }
//    }

    /**********************payment gateway***********************/
    public void startRequest(Order order) {
        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(Order order, Exception error) {
                //dismiss the dialog if showed

                // Make sure the follwoing code is called on UI thread to show Toasts or to
                //update UI elements
                if (error != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(dialog!=null)
                            {
                                dialog.cancel();
                            }
                        }
                    });
                    if (error instanceof Errors.ConnectionError) {
                        Util.showToast(AddPoints.this,"No internet connection");
                        Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerError) {
                        Util.showToast(AddPoints.this,"Server Error. Try again");
                        Log.e("App", "Server Error. Try again");
                    } else if (error instanceof Errors.AuthenticationError) {
                        Util.showToast(AddPoints.this,"Access token is invalid or expired");
                        Log.e("App", "Access token is invalid or expired");
                    } else if (error instanceof Errors.ValidationError) {
                        // Cast object to validation to pinpoint the issue
                        Errors.ValidationError validationError = (Errors.ValidationError) error;
                        if (!validationError.isValidTransactionID()) {
                            Util.showToast(AddPoints.this,"Transaction ID is not Unique");
                            Log.e("App", "Transaction ID is not Unique");
                            return;
                        }
                        if (!validationError.isValidRedirectURL()) {
                            Log.e("App", "Redirect url is invalid");
                            return;
                        }


                        if (!validationError.isValidWebhook()) {

                            return;
                        }

                        if (!validationError.isValidPhone()) {
                            Util.showToast(AddPoints.this,"Buyer's Phone Number is invalid/empty");
                            Log.e("App", "Buyer's Phone Number is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidEmail()) {
                            Util.showToast(AddPoints.this,"Buyer's Email is invalid/empty");
                            Log.e("App", "Buyer's Email is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidAmount()) {
                            Util.showToast(AddPoints.this,"Amount is either less than Rs.9 or has more than two decimal places");
                            Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                            return;
                        }
                        if (!validationError.isValidName()) {
                            Util.showToast(AddPoints.this,"Buyer's Name is required");
                            Log.e("App", "Buyer's Name is required");
                            return;
                        }
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog!=null)
                        {
                            dialog.cancel();
                        }
                    }
                });
                startPreCreatedUI(order);
            }
        });

        request.execute();
    }

    public void showDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = Util.showPogress(AddPoints.this);
            }
        });


    }
    private void startPreCreatedUI(Order order){
        //Using Pre created UI
        showDialog();
        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public void callOrder() {
        if (dialog != null) {
            dialog.cancel();
        }
        if (controller.getProfile() != null) {
            Order order = new Order(instamojoAcessToken, getOrderNumber(), controller.getProfile().getUserName(), controller.getProfile().getEmail(), controller.getProfile().getMobile(), Integer.toString(Integer.parseInt(points_Edt.getText().toString())/10), "Add Points");
            if (validateOrder(order)) {
                startRequest(order);
            } else {
                Util.showToast(AddPoints.this, "Invalid Order Details");
            }
        }


    }

    public void getAcessToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            instamojoAcessToken = jsonObject.getString("access_token");
            controller.setPaymentGatewayToken(instamojoAcessToken);
            callOrder();
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public String getOrderNumber() {
        int min = 10000;
        int max = 999999999;
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return Integer.toString(i1);
    }



    public boolean validateOrder(Order order) {
        if (!order.isValid()) {
            if (!order.isValidName()) {
                Log.e("App", "Buyer name is invalid");
               Util.showToast(AddPoints.this,"Buyer name is invalid");
            }

            if (!order.isValidEmail()) {
                Log.e("App", "Buyer email is invalid");
                Util.showToast(AddPoints.this,"Buyer email is invalid");
            }

            if (!order.isValidPhone()) {
                Log.e("App", "Buyer phone is invalid");
                Util.showToast(AddPoints.this,"Buyer phone is invalid");
            }

            if (!order.isValidAmount()) {
                Log.e("App", "Amount is invalid");
                Util.showToast(AddPoints.this,"Amount is invalid");

            }

            if (!order.isValidDescription()) {
                Log.e("App", "description is invalid");
                Util.showToast(AddPoints.this,"description is invalid");

            }

            if (!order.isValidTransactionID()) {
                Log.e("App", "Transaction ID is invalid");
                Util.showToast(AddPoints.this,"Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()) {
                Log.e("App", "Redirection URL is invalid");
                Util.showToast(AddPoints.this, "Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                Util.showToast(AddPoints.this,"Webhook URL is invalid");

            }

            return false;
        } else {
            return true;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);
            if (dialog != null) {
                dialog.cancel();
            }
            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                callUpdatePayment(orderID, transactionID, paymentID);
            } else {
                Util.showToast(AddPoints.this, "Your Payment Was Unsucessfull");
                //Oops!! Payment was cancelled
            }
        }
    }
}