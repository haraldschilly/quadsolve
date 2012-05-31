package at.univie.mat.quadsolve;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class QuadSolveActivity extends Activity implements TextWatcher, SeekBar.OnSeekBarChangeListener {

  private TextView sol1, sol2, txtA, txtB, txtC;
  private SeekBar  seekA, seekB, seekC;
  private double   a, b, c;

  private void bind() {
    sol1 = (TextView) findViewById(R.id.sol1);
    sol2 = (TextView) findViewById(R.id.sol2);

    txtA = (TextView) findViewById(R.id.txtA);
    txtB = (TextView) findViewById(R.id.txtB);
    txtC = (TextView) findViewById(R.id.txtC);

    seekA = (SeekBar) findViewById(R.id.seekBarA);
    seekB = (SeekBar) findViewById(R.id.seekBarB);
    seekC = (SeekBar) findViewById(R.id.seekBarC);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    bind();

    txtA.addTextChangedListener(this);
    txtB.addTextChangedListener(this);
    txtC.addTextChangedListener(this);

    seekA.setOnSeekBarChangeListener(this);
    seekB.setOnSeekBarChangeListener(this);
    seekC.setOnSeekBarChangeListener(this);
  }

  /**
   * this method gets all the values and solves the equation!
   * 
   */
  private void solve() {
    if (a == 0) {
      if (b == 0) {
        if (c == 0) {
          sol1.setText("alles");
          sol2.setText("");
        }
        sol1.setText("nichts");
        sol2.setText("");
        return;
      }

      // a==0, b != 0
      sol1.setText(String.format("%.4f", -c / b));
      sol2.setText("-");
      return;
    }

    double d = b * b - 4 * a * c;
    if (d < 0) {
      sol1.setText("complex");
      sol2.setText("");
    } else {
      d = Math.sqrt(d);
      sol1.setText("x1: " + (-b - d) / (2 * a));
      sol2.setText("x2: " + (-b + d) / (2 * a));
    }
  }

  @Override
  public void afterTextChanged(Editable tbox) {
    try {
      a = Double.parseDouble(txtA.getText().toString());
      b = Double.parseDouble(txtB.getText().toString());
      c = Double.parseDouble(txtC.getText().toString());
      solve();
    } catch (NumberFormatException nfe) {
      Log.wtf("QS", "needs to be doubles!");
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int val, boolean fromUser) {
    double v = (val - seekBar.getMax() / 2) / 10.0; // scale to max
    if (fromUser) {
      if (seekBar == seekA) {
        txtA.setText(Double.toString(v));
      } else if (seekBar == seekB) {
        txtB.setText(Double.toString(v));
      } else if (seekBar == seekC) {
        txtC.setText(Double.toString(v));
      }
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
  }
}