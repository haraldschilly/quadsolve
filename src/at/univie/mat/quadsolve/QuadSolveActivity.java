/**
 * QuadSolve - simple android demo app solving a quadratic equation
 * Copyright 2012: Harald Schilly <harald.schilly@univie.ac.at>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.univie.mat.quadsolve;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class QuadSolveActivity extends Activity implements TextWatcher, SeekBar.OnSeekBarChangeListener {

  // scaling of the seek bars
  private final static double SCALE_DIV = 10.0;

  // UI elements
  private TextView            sol1, sol2, txtA, txtB, txtC;
  private SeekBar             seekA, seekB, seekC;

  // internal state
  private double              a, b, c;

  private void bind() {
    // get ui objects
    sol1 = (TextView) findViewById(R.id.sol1);
    sol2 = (TextView) findViewById(R.id.sol2);

    txtA = (TextView) findViewById(R.id.txtA);
    txtB = (TextView) findViewById(R.id.txtB);
    txtC = (TextView) findViewById(R.id.txtC);

    seekA = (SeekBar) findViewById(R.id.seekBarA);
    seekB = (SeekBar) findViewById(R.id.seekBarB);
    seekC = (SeekBar) findViewById(R.id.seekBarC);

    // connect via event listeners
    txtA.addTextChangedListener(this);
    txtB.addTextChangedListener(this);
    txtC.addTextChangedListener(this);

    seekA.setOnSeekBarChangeListener(this);
    seekB.setOnSeekBarChangeListener(this);
    seekC.setOnSeekBarChangeListener(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    bind();
  }

  /**
   * this method solves the equation based on the state of a,b and c!
   */
  private void solve() {
    if (Math.abs(a) < 1e-10) {
      if (Math.abs(b) < 1e-10) {
        if (Math.abs(c) < 1e-10) {
          sol1.setText("alles");
          sol2.setText("");
          return;
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

  /**
   * called after a textbox is modified. all three boxes are parsed.
   */
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

    seekA.setProgress((int) (SCALE_DIV * a + (seekA.getMax() / 2)));
    seekB.setProgress((int) (SCALE_DIV * b + (seekB.getMax() / 2)));
    seekC.setProgress((int) (SCALE_DIV * c + (seekC.getMax() / 2)));
  }

  /**
   * called when the slider is moved. note, that it needs to be scaled and the
   * scaling has to match with the one in {@link #afterTextChanged(Editable)}.
   */
  @Override
  public void onProgressChanged(SeekBar seekBar, int val, boolean fromUser) {
    double v = (val - seekBar.getMax() / 2) / SCALE_DIV; // scale to max

    if (fromUser) { // important check, otherwise infinite loop
      if (seekBar == seekA) {
        txtA.setText(Double.toString(v));
      } else if (seekBar == seekB) {
        txtB.setText(Double.toString(v));
      } else if (seekBar == seekC) {
        txtC.setText(Double.toString(v));
      }
    }
  }

  // the following empty methods are from the listener interfaces

  @Override
  public void onStartTrackingTouch(SeekBar arg0) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar arg0) {
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
  }
}