package com.mokkalokka.alcoholprice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    public static String resultat = "";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        MobileAds.initialize(this, "ca-app-pub-8381692107773407~8903040079");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void openResultDialog() {
        ResultDialog resultDialog = new ResultDialog();
        resultDialog.show(getSupportFragmentManager(), "Result");
    }

    public void openInfoDialog() {
        InfoDialog resultDialog = new InfoDialog();
        resultDialog.show(getSupportFragmentManager(), "Info");
    }

    public void onInfo(View v){
        this.closeKeyboard();
        openInfoDialog();
    }


    public void onCalculate(View v) {
        this.closeKeyboard();
        Konverter nyKonvertering = new Konverter();
        EditText txtAlkoholProsent1 = (EditText) findViewById(R.id.txtAlkoholProsent1);
        EditText txtAlkoholProsent2 = (EditText) findViewById(R.id.txtAlkoholProsent2);
        EditText txtVolum1 = (EditText) findViewById(R.id.txtVolum1);
        EditText txtVolum2 = (EditText) findViewById(R.id.txtVolum2);
        EditText txtPris1 = (EditText) findViewById(R.id.txtPris1);
        EditText txtPris2 = (EditText) findViewById(R.id.txtPris2);


        //Converting to double values
        try {
            Double alkoholProsent1 = Double.parseDouble(txtAlkoholProsent1.getText().toString());
            Double alkoholProsent2 = Double.parseDouble(txtAlkoholProsent2.getText().toString());
            Double volum1 = Double.parseDouble(txtVolum1.getText().toString());
            Double volum2 = Double.parseDouble(txtVolum2.getText().toString());
            Double pris1 = Double.parseDouble(txtPris1.getText().toString());
            Double pris2 = Double.parseDouble(txtPris2.getText().toString());


            resultat = nyKonvertering.sammenLign(
                    alkoholProsent1,  volum1, pris1, alkoholProsent2, volum2,pris2);
            //txtResultat.setText(resultat);
            openResultDialog();


        } catch (NumberFormatException e){
            //Handle error here
        }
    }


    class Konverter {
        public double alkoholProsentEksempel = 100.0;
        public double volumEksempel = 1.0;
        DecimalFormat df = new DecimalFormat("####0.00");

        public String regnUt(double alkoholProsent, double volum, double pris) {
            double faktor1 = alkoholProsentEksempel / alkoholProsent;
            double faktor2 = volumEksempel / volum;
            double resultat = faktor1 * faktor2 * pris;
            return "1l pure alcohol (100%)\n would cost: " + df.format(resultat) + "kr";
        }

        public String sammenLign(double alkoholProsent1, double volum1, double pris1, double alkoholProsent2, double volum2, double pris2) {
            double faktor1 = alkoholProsentEksempel / alkoholProsent2;
            double faktor2 = volumEksempel / volum2;
            double resultat2 = faktor1 * faktor2 * pris2;

            double faktor3 = alkoholProsentEksempel / alkoholProsent1;
            double faktor4 = volumEksempel / volum1;
            double resultat1 = faktor3 * faktor4 * pris1;

            String ut;
            if (resultat1 > resultat2) {
                double total = ((resultat1 / resultat2) - 1) * 100;

                ut = "Beverage number 2 is " + df.format(total) + "% cheaper than number 1";
            } else if (resultat1 == resultat2) {
                ut = "Same price";
            } else {
                double total = ((resultat2 / resultat1) - 1) * 100;
                ut = "Beverage number 1 is " + df.format(total) + "% cheaper than number 2";
            }

            return ut;


        }


    }


}
