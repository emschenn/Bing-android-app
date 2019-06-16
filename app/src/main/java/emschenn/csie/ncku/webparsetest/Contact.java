package emschenn.csie.ncku.webparsetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Contact extends AppCompatActivity {
    private Toolbar toolbar;
    private Button button;
    private EditText editText;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.editText2);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setPadding(15,0,0,0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Comments");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, MainActivity.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.enter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                if(!TextUtils.isEmpty(s)){
                    String id = databaseReference.push().getKey();
                    Comments comments = new Comments(s);
                    databaseReference.child(id).setValue(comments);
                    Toast.makeText(Contact.this,"發送成功！感謝你的意見回饋！",Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(Contact.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
