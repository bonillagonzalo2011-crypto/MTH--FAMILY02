package com.mthfamily.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Button startButton = new Button(this);
        startButton.setText("Iniciar MTH FAMILY Overlay");
        startButton.setOnClickListener(v -> checkOverlayPermission());
        setContentView(startButton);
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                startFloatingService();
            }
        } else {
            startFloatingService();
        }
    }

    private void startFloatingService() {
        Intent intent = new Intent(MainActivity.this, FloatingService.class);
        startService(intent);
        Toast.makeText(this, "Servicio flotante iniciado", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "¡Permiso concedido!", Toast.LENGTH_SHORT).show();
                    startFloatingService();
                } else {
                    Toast.makeText(this, "Permiso denegado para mostrar sobre otras apps", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
