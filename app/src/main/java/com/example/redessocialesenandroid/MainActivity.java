package com.example.redessocialesenandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView txtNombre;
    TextView txtCorreo;
    ProfilePictureView imgPerfil;
    ShareButton shareButton;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificiar();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        txtNombre= (TextView) findViewById(R.id.txtNombre);
        txtCorreo=(TextView) findViewById(R.id.txtCorreo);
        imgPerfil=(ProfilePictureView) findViewById(R.id.imgPerfil);
        if (AccessToken.getCurrentAccessToken()!=null){
            Profile profile= Profile.getCurrentProfile();
            txtNombre.setText(profile.getName());
            imgPerfil.setProfileId(profile.getId());
        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {



            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Inicio de sesion Correcto", Toast.LENGTH_SHORT).show();
                Profile profile= Profile.getCurrentProfile();
                txtNombre.setText(profile.getName());
                imgPerfil.setProfileId(profile.getId());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Inicio de sesion Cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Error en Inicio de sesion", Toast.LENGTH_SHORT).show();

            }
        });

        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        final SharePhotoContent content2 = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareButton = (ShareButton)findViewById(R.id.btnShare);
        shareButton.setShareContent(content2);
        shareDialog = new ShareDialog(this);

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verificiar(){
        int Permisos = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (Permisos == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Ya tienes permiso",Toast.LENGTH_SHORT).show();;
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1000);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}