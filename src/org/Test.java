package org;

import javax.swing.*;
import java.awt.BorderLayout;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class Test extends JFrame {

	private static final long serialVersionUID = 1L;

	

    public static void main(String[] args) {
    	String path = "C:/Windows/System32/drivers/etc/HOSTS";
    	System.out.println(path.substring(path.lastIndexOf("/")+1));
    }

}