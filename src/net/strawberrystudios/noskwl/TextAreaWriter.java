/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.swing.JTextArea;

/**
 *
 * @author giddyc
 */
public class TextAreaWriter extends Writer {

    private JTextArea txtArea;

    public TextAreaWriter(JTextArea txtArea) {
        super();
        this.txtArea = txtArea;
    }

    public void append(String str) {
        txtArea.append(str);
    }

    public void println(String str) {
        txtArea.append(str + "\n");
    }

    @Override
    public void flush() {
        this.setText("");

    }

    public void setText(String str) {
        txtArea.setText(str);
    }

    @Override
    public void write(int i) throws IOException {
        txtArea.setText(((char) i) + "");
    }

    public void write(byte[] bArray) throws IOException {
        for (byte b : bArray) {
            txtArea.setText(((char) b) + "");
        }
    }

    @Override
    public void write(char[] chars, int i, int i1) throws IOException {
        for (int a = i; a <= i1; a++) {
            txtArea.append(chars[a] + "");
        }
    }

    @Override
    public void write(String str) {
        txtArea.append(str);
    }

    @Override
    public void close() throws IOException {
        txtArea = null;
    }

}
